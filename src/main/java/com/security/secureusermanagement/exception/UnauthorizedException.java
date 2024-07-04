package com.security.secureusermanagement.exception;

import com.security.secureusermanagement.dto.ApiResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  private ApiResponse apiResponse;

  @Getter
  private String message;

  public UnauthorizedException(ApiResponse apiResponse) {
    super();
    this.apiResponse = apiResponse;
  }

  public UnauthorizedException(String message) {
    super(message);
    this.message = message;
  }

}
