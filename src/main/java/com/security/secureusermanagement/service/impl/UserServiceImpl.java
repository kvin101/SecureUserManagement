package com.security.secureusermanagement.service.impl;

import com.security.secureusermanagement.dao.role.Role;
import com.security.secureusermanagement.dao.role.RoleName;
import com.security.secureusermanagement.dao.user.Address;
import com.security.secureusermanagement.dao.user.User;
import com.security.secureusermanagement.dto.ApiResponse;
import com.security.secureusermanagement.dto.InfoRequest;
import com.security.secureusermanagement.dto.UserIdentityAvailability;
import com.security.secureusermanagement.dto.UserProfile;
import com.security.secureusermanagement.dto.UserSummary;
import com.security.secureusermanagement.exception.AccessDeniedException;
import com.security.secureusermanagement.exception.AppException;
import com.security.secureusermanagement.exception.BadRequestException;
import com.security.secureusermanagement.exception.ResourceNotFoundException;
import com.security.secureusermanagement.exception.UnauthorizedException;
import com.security.secureusermanagement.repository.RoleRepository;
import com.security.secureusermanagement.repository.UserRepository;
import com.security.secureusermanagement.security.UserPrincipal;
import com.security.secureusermanagement.service.UserService;
import com.security.secureusermanagement.utils.Constants;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  private final RoleRepository roleRepository;

  private final PasswordEncoder passwordEncoder;

  public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
      PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public UserSummary getCurrentUser(UserPrincipal currentUser) {
    return new UserSummary(currentUser.getId(), currentUser.getUsername(),
        currentUser.getFirstName(),
        currentUser.getLastName());
  }

  @Override
  public UserIdentityAvailability checkUsernameAvailability(String username) {
    Boolean isAvailable = !userRepository.existsByUsername(username);
    return new UserIdentityAvailability(isAvailable);
  }

  @Override
  public UserIdentityAvailability checkEmailAvailability(String email) {
    Boolean isAvailable = !userRepository.existsByEmail(email);
    return new UserIdentityAvailability(isAvailable);
  }

  @Override
  public UserProfile getUserProfile(String username) {
    User user = userRepository.getUserByName(username);
    return new UserProfile(user.getId(), user.getUsername(), user.getFirstName(),
        user.getLastName(), user.getEmail(), user.getAddress(),
        user.getPhone());
  }

  @Override
  public User addUser(User user) {
    if (Boolean.TRUE.equals(userRepository.existsByUsername(user.getUsername()))) {
      ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "Username is already taken");
      throw new BadRequestException(apiResponse);
    }

    if (Boolean.TRUE.equals(userRepository.existsByEmail(user.getEmail()))) {
      ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "Email is already taken");
      throw new BadRequestException(apiResponse);
    }

    List<Role> roles = new ArrayList<>();
    roles.add(
        roleRepository.findByName(RoleName.ROLE_USER)
            .orElseThrow(() -> new AppException(Constants.USER_ROLE_NOT_SET)));
    user.setRoles(roles);

    user.setPassword(passwordEncoder.encode(user.getPassword()));
    return userRepository.save(user);
  }

  @Override
  public User updateUser(User newUser, String username, UserPrincipal currentUser) {
    User user = userRepository.getUserByName(username);
    if (user.getId().equals(currentUser.getId())
        || currentUser.getAuthorities()
        .contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
      user.setFirstName(newUser.getFirstName());
      user.setLastName(newUser.getLastName());
      user.setPassword(passwordEncoder.encode(newUser.getPassword()));
      user.setAddress(newUser.getAddress());
      user.setPhone(newUser.getPhone());
      return userRepository.save(user);
    }
    ApiResponse apiResponse = new ApiResponse(Boolean.FALSE,
        "You don't have permission to update profile of: " + username);
    throw new UnauthorizedException(apiResponse);
  }

  @Override
  public ApiResponse deleteUser(String username, UserPrincipal currentUser) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new ResourceNotFoundException("User", "id", username));
    if (!user.getId().equals(currentUser.getId()) || !currentUser.getAuthorities()
        .contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
      ApiResponse apiResponse = new ApiResponse(Boolean.FALSE,
          "You don't have permission to delete profile of: " + username);
      throw new AccessDeniedException(apiResponse);
    }

    userRepository.deleteById(user.getId());

    return new ApiResponse(Boolean.TRUE, "You successfully deleted profile of: " + username);
  }

  @Override
  public UserProfile setOrUpdateInfo(UserPrincipal currentUser, InfoRequest infoRequest) {
    User user = userRepository.findByUsername(currentUser.getUsername())
        .orElseThrow(
            () -> new ResourceNotFoundException("User", "username", currentUser.getUsername()));
    Address address = new Address(infoRequest.getStreet(), infoRequest.getSuite(),
        infoRequest.getCity(),
        infoRequest.getZipcode());
    if (user.getId().equals(currentUser.getId())
        || currentUser.getAuthorities()
        .contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
      user.setAddress(address);
      user.setPhone(infoRequest.getPhone());
      User updatedUser = userRepository.save(user);

      return new UserProfile(updatedUser.getId(), updatedUser.getUsername(),
          updatedUser.getFirstName(), updatedUser.getLastName(),
          updatedUser.getEmail(), updatedUser.getAddress(), updatedUser.getPhone());
    }

    ApiResponse apiResponse = new ApiResponse(Boolean.FALSE,
        "You don't have permission to update users profile", HttpStatus.FORBIDDEN);
    throw new AccessDeniedException(apiResponse);
  }
}
