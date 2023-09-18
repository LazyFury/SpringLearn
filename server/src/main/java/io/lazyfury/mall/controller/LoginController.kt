package io.lazyfury.mall.controller

import io.lazyfury.mall.entity.User
import io.lazyfury.mall.repository.CustomUserRepository
import io.lazyfury.mall.service.UserDetailService
import io.lazyfury.utils.error.ErrorException
import io.lazyfury.utils.error.ProjectErrorCode
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
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
    lateinit var userRepository: CustomUserRepository

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    lateinit var authenticationManager: AuthenticationManager

    @Autowired
    lateinit var userDetailsService: UserDetailService

    @GetMapping("/login")
    fun login(): String {
        return "login"
    }

    @Override
    @GetMapping("/login_post")
    fun login(
        req: HttpServletRequest,
        res: HttpServletResponse,
        @RequestParam username: String?,
        @RequestParam password: String?
    ): String {
        if (username != null && password != null) {
            System.out.printf("%s: %s\n", username, password)
            val userDetails = userDetailsService.loadUserByUsername(username)
            try {
                val auth = authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken(username, password, userDetails.authorities)
                )
                System.out.printf("is auth %s\n", auth.isAuthenticated)
                val context = SecurityContextHolder.getContext()
                req.session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, context)
                context.authentication = auth
                return "redirect:/"
            } catch (e: AuthenticationException) {
                System.out.printf("auth error %s\n", e.message)
                throw ErrorException(ProjectErrorCode.USER_PASSWORD_NOT_MATCH, "用户名或密码错误");
            }
        }
        return "error"
    }

    @GetMapping("/signup")
    fun signup(): String {
        return "signup"
    }

    @PostMapping("/signup")
    fun signup(@RequestParam username: String, @RequestParam password: String): String {
        if (userRepository.existsByUsername(username)) {
            throw IllegalStateException("exists by username")
        }
        val user = User(username = username, password = passwordEncoder.encode(password), id = null, email = "")
        userRepository.save(user)
        return "redirect:/login"
    }

    @GetMapping("/forgot")
    fun forgot(): String {
        return "forgot"
    }
}