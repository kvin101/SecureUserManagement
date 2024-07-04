package com.security.secureusermanagement.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UserSummary {

  @JsonIgnore
  private Long id;
  private String username;
  private String firstName;
  private String lastName;
}
