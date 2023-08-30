package io.lazyfury.mall.code.controller

import jakarta.servlet.RequestDispatcher
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.servlet.ModelAndView

@Controller
class HomeController:ErrorController {

    @GetMapping("/")
    fun home():ModelAndView{
        val view = ModelAndView("index")
        view.addObject("name","A new application")
        return view
    }

    @GetMapping("/error")
    fun error(req:HttpServletRequest,res:HttpServletResponse):ModelAndView{
        val model = ModelAndView("error")
        model.addObject("statusCode",req.getAttribute(RequestDispatcher.ERROR_STATUS_CODE))
        return model
    }

    @GetMapping("/about") fun about():String{
        return "about"
    }
}