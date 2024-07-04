package com.security.secureusermanagement.controller;

import static com.security.secureusermanagement.utils.Constants.AUTH_BASE_PATH;
import static com.security.secureusermanagement.utils.Constants.SIGN_IN_PATH;
import static com.security.secureusermanagement.utils.Constants.SIGN_UP_PATH;
import static com.security.secureusermanagement.utils.Constants.USER_ROLE_NOT_SET;

import com.security.secureusermanagement.dao.role.Role;
import com.security.secureusermanagement.dao.role.RoleName;
import com.security.secureusermanagement.dao.user.User;
import com.security.secureusermanagement.dto.ApiResponse;
import com.security.secureusermanagement.dto.JwtAuthenticationResponse;
import com.security.secureusermanagement.dto.LoginRequest;
import com.security.secureusermanagement.dto.SignUpRequest;
import com.security.secureusermanagement.exception.AppException;
import com.security.secureusermanagement.exception.SecureUserManagementException;
import com.security.secureusermanagement.repository.RoleRepository;
import com.security.secureusermanagement.repository.UserRepository;
import com.security.secureusermanagement.security.JwtTokenProvider;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping(AUTH_BASE_PATH)
public class AuthController {

  private final AuthenticationManager authenticationManager;
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider jwtTokenProvider;

  public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository,
      RoleRepository roleRepository, PasswordEncoder passwordEncoder,
      JwtTokenProvider jwtTokenProvider) {
    this.authenticationManager = authenticationManager;
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtTokenProvider = jwtTokenProvider;
  }

  @PostMapping(SIGN_IN_PATH)
  public ResponseEntity<JwtAuthenticationResponse> authenticateUser(
      @Valid @RequestBody LoginRequest loginRequest) {
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.getUsernameOrEmail(),
            loginRequest.getPassword()));
    SecurityContextHolder.getContext().setAuthentication(authentication);
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
        .toList();
    String jwt = jwtTokenProvider.generateTokenFromUsername(
        (UserDetails) authentication.getPrincipal());
    return ResponseEntity.ok(new JwtAuthenticationResponse(userDetails.getUsername(), roles, jwt));
  }

  @PostMapping(SIGN_UP_PATH)
  public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
    if (Boolean.TRUE.equals(userRepository.existsByUsername(signUpRequest.getUsername()))) {
      throw new SecureUserManagementException(HttpStatus.BAD_REQUEST, "Username is already taken");
    }
    if (Boolean.TRUE.equals(userRepository.existsByEmail(signUpRequest.getEmail()))) {
      throw new SecureUserManagementException(HttpStatus.BAD_REQUEST, "Email is already taken");
    }
    String firstName = signUpRequest.getFirstName().toLowerCase();
    String lastName = signUpRequest.getLastName().toLowerCase();
    String username = signUpRequest.getUsername().toLowerCase();
    String email = signUpRequest.getEmail().toLowerCase();
    String password = passwordEncoder.encode(signUpRequest.getPassword());
    User user = new User(firstName, lastName, username, email, password);
    List<Role> roles = new ArrayList<>();
    if (userRepository.count() == 0) {
      roles.add(roleRepository.findByName(RoleName.ROLE_USER)
          .orElseThrow(() -> new AppException(USER_ROLE_NOT_SET)));
      roles.add(roleRepository.findByName(RoleName.ROLE_ADMIN)
          .orElseThrow(() -> new AppException(USER_ROLE_NOT_SET)));
    } else {
      roles.add(roleRepository.findByName(RoleName.ROLE_USER)
          .orElseThrow(() -> new AppException(USER_ROLE_NOT_SET)));
    }
    user.setRoles(roles);
    User result = userRepository.save(user);
    URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/users/{userId}")
        .buildAndExpand(result.getId()).toUri();
    return ResponseEntity.created(location)
        .body(new ApiResponse(Boolean.TRUE, String.format("%s registered successfully", username)));
  }
}
