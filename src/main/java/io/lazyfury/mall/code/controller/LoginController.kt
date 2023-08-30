package io.lazyfury.mall.code.controller

import io.lazyfury.mall.code.entity.User
import io.lazyfury.mall.code.repository.CustomUserRepository
import io.lazyfury.mall.code.service.UserDetailService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class LoginController {

    @Autowired
    lateinit var userRepository: CustomUserRepository;

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    lateinit var authenticationManager:AuthenticationManager

    @Autowired
    lateinit var userDetailsService: UserDetailService

    @GetMapping("/login")
    fun login():String{
        return "login"
    }

    class Authority(var role:String):GrantedAuthority {
        override fun getAuthority(): String {
            return role;
        }
    }

    @Override
    @GetMapping("/login_post") fun login(req:HttpServletRequest,@RequestParam username:String?,@RequestParam password:String?):String{
        if(username != null && password != null){
            System.out.printf("%s: %s\n",username,password);
            var userDetails = userDetailsService.loadUserByUsername(username)
            var auth = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(username,password,userDetails.authorities)
            )
            System.out.printf("is auth %s\n",auth.isAuthenticated)
            var context = SecurityContextHolder.getContext();
            req.session.setAttribute(SPRING_SECURITY_CONTEXT_KEY,context)
            context.authentication = auth;
            return "redirect:/"

        }
        return "error"
    }

    @GetMapping("/signup") fun signup():String{
        return "signup"
    }

    @PostMapping("/signup") fun signup(@RequestParam username:String,@RequestParam password:String):String{
        if(userRepository.existsByUsername(username)){
            throw IllegalStateException("exists by username")
        }
        val user = User(username = username, password = passwordEncoder.encode(password), id = null, email = "");
        userRepository.save(user);
        return "redirect:/login"
    }

    @GetMapping("/forgot") fun forgot():String{return "forgot"}
}