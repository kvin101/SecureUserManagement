package com.security.secureusermanagement.exception;


import com.security.secureusermanagement.dto.ApiResponse;
import java.io.Serial;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
@Getter
public class ResourceNotFoundException extends RuntimeException {

  @Serial
  private static final long serialVersionUID = 1L;
  private final String resourceName;
  private final String fieldName;
  private final Object fieldValue;
  private transient ApiResponse apiResponse;

  public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
    super();
    this.resourceName = resourceName;
    this.fieldName = fieldName;
    this.fieldValue = fieldValue;
  }

  private void setApiResponse() {
    String message = String.format("%s not found with %s: '%s'", resourceName, fieldName,
        fieldValue);
    apiResponse = new ApiResponse(Boolean.FALSE, message);
  }
}
