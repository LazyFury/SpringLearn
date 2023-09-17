package io.lazyfury.mall.service

import io.lazyfury.mall.repository.CustomUserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailService(private val userRepository: CustomUserRepository) : UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails {
        if (username == null) throw UsernameNotFoundException("Username cannot be null")
        return userRepository.findByUsername(username) ?: throw UsernameNotFoundException("")
    }
}