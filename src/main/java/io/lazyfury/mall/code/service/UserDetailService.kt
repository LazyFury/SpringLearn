package io.lazyfury.mall.code.service

import io.lazyfury.mall.code.repository.CustomUserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailService(private val userRepository:CustomUserRepository):UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails {
        if(username == null)throw UsernameNotFoundException("Username cannot be null")
        val user = userRepository.findByUsername(username) ?: throw UsernameNotFoundException("");
        return user;
    }
}