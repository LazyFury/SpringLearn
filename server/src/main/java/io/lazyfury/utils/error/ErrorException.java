package io.lazyfury.utils.error;

import jakarta.servlet.ServletException;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ErrorException extends ServletException {
    public ErrorCode code;

    public String message;

    public ErrorException(ErrorCode code, String message) {
        this.code = code;
        this.message = message;
    }
}
