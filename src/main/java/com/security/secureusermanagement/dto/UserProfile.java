package com.security.secureusermanagement.dto;

import com.security.secureusermanagement.dao.user.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfile {

  private Long id;
  private String username;
  private String firstName;
  private String lastName;
  private String email;
  private Address address;
  private String phone;
}
