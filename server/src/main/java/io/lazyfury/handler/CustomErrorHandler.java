package io.lazyfury.handler;

import io.lazyfury.utils.error.ErrorException;
import io.lazyfury.utils.response.JsonResponse;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.Null;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Component
@ControllerAdvice(basePackages = "io.lazyfury.mall.api")
public class CustomErrorHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public JsonResponse<Null> handleException(Exception e, HttpServletResponse response, HttpServletRequest request) {
        var message = (String) request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        var status = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (message != null && !message.isEmpty() && !message.isBlank())
            return JsonResponse.fail(status, message);

        if (e instanceof ChangeSetPersister.NotFoundException)
            return JsonResponse.fail(404, e.getMessage());
        if (e instanceof IllegalArgumentException)
            return JsonResponse.fail(400, e.getMessage());
        if (e instanceof HttpRequestMethodNotSupportedException)
            return JsonResponse.fail(405, e.getMessage());
        return JsonResponse.fail(500, e.getMessage());
    }

    @ExceptionHandler(ErrorException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public JsonResponse<Null> handleErrorException(ErrorException e) {
        System.out.println("cacth global error");
        return new JsonResponse<Null>(e.code);
    }
}
