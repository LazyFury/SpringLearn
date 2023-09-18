/*
 * Copyright (c) 2023. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package io.lazyfury.handler;

import io.lazyfury.utils.error.ErrorException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice(basePackages = "io.lazyfury.mall.controller")
public class MvcErrorHandler {

    @ExceptionHandler(ErrorException.class)
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView handleErrorException(ErrorException e, HttpServletResponse response) {
        var model = new ModelAndView("error");
        model.addObject("statusCode", e.code.code);
        model.addObject("message", e.getMessage());
        return model;
    }
}
