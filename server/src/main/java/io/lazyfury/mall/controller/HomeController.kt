package io.lazyfury.mall.controller

import jakarta.servlet.RequestDispatcher
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.servlet.ModelAndView

@Controller
class HomeController : ErrorController {

    @GetMapping(value = ["", "/", "/index", "/home"])
    fun home(): ModelAndView {
        val view = ModelAndView("index")
        view.addObject("name", "A new application")
        return view
    }

    @Override
    @GetMapping("/error")
    fun error(req: HttpServletRequest, res: HttpServletResponse, err: Exception): ModelAndView {
        val model = ModelAndView("error")
        model.addObject("statusCode", req.getAttribute(RequestDispatcher.ERROR_STATUS_CODE))
        model.addObject("message", req.getAttribute(RequestDispatcher.ERROR_MESSAGE))
        return model
    }

    @GetMapping("/about")
    fun about(): String {
        return "about"
    }

    fun getErrorPath(): String {
        return "/error"
    }
}