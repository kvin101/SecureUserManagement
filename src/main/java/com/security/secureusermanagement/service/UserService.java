package com.security.secureusermanagement.service;

import com.security.secureusermanagement.dao.user.User;
import com.security.secureusermanagement.dto.ApiResponse;
import com.security.secureusermanagement.dto.InfoRequest;
import com.security.secureusermanagement.dto.UserIdentityAvailability;
import com.security.secureusermanagement.dto.UserProfile;
import com.security.secureusermanagement.dto.UserSummary;
import com.security.secureusermanagement.security.UserPrincipal;

public interface UserService {

  UserSummary getCurrentUser(UserPrincipal currentUser);

  UserIdentityAvailability checkUsernameAvailability(String username);

  UserIdentityAvailability checkEmailAvailability(String email);

  UserProfile getUserProfile(String username);

  User addUser(User user);

  User updateUser(User newUser, String username, UserPrincipal currentUser);

  ApiResponse deleteUser(String username, UserPrincipal currentUser);

  UserProfile setOrUpdateInfo(UserPrincipal currentUser, InfoRequest infoRequest);

}