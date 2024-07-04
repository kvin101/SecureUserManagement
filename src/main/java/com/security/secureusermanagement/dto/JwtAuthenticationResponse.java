package com.security.secureusermanagement.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtAuthenticationResponse {

  private String username;
  private List<String> roles;
  private String jwtToken;
}
