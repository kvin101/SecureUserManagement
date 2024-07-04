package com.security.secureusermanagement.exception;

import com.security.secureusermanagement.dto.ApiResponse;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@Setter
@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class AccessDeniedException extends RuntimeException {

  private static final long serialVersionUID = 1L;
  @Getter
  private ApiResponse apiResponse;
  private String message;

  public AccessDeniedException(ApiResponse apiResponse) {
    this.apiResponse = apiResponse;
  }

  public AccessDeniedException(ApiResponse apiResponse, String message) {
    this.apiResponse = apiResponse;
    this.message = message;
  }
}
