package io.lazyfury.utils.error;

import lombok.Data;

@Data
public class ErrorCode {
    public int code;
    public String message;

    public ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
