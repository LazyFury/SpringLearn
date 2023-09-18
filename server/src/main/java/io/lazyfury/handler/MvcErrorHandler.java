package io.lazyfury.handler;

import io.lazyfury.utils.error.ErrorException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;

@ControllerAdvice(basePackages = "io.lazyfury.mall.controller")
public class MvcErrorHandler {

    @ExceptionHandler(ErrorException.class)
    public void handleErrorException(ErrorException e, HttpServletResponse response) throws IOException {
        System.out.println("cacth mvc global error");
        response.sendError(e.code.code, e.message);
    }
}
