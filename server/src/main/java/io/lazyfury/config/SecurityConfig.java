package io.lazyfury.config;


import io.lazyfury.filter.TokenAuthenticationFilter;
import io.lazyfury.mall.service.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity()
public class SecurityConfig {

    @Autowired
    UserDetailService userDetailService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    TokenAuthenticationFilter tokenAuthenticationFilter;


    @Bean
    public SecurityFilterChain configuration(HttpSecurity http, AuthenticationProvider provider) throws Exception {
        http.userDetailsService(userDetailService).csrf(AbstractHttpConfigurer::disable)
                .authenticationProvider(provider)
                .authorizeHttpRequests(
                        request -> request
                                .requestMatchers(
                                        new MvcRequestMatcher(new HandlerMappingIntrospector(), "/auth/**")
                                ).authenticated()

                                //user api
                                .requestMatchers("/api/**").hasRole("user")
                                //admin api
                                .requestMatchers("/admin/**").hasRole("admin")
                                .anyRequest().permitAll()

                )
                .addFilterAfter(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        ;
        return http.build();
    }


    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailService);
        provider.setPasswordEncoder(passwordEncoder);
        provider.setAuthoritiesMapper(authorities -> {
            System.out.println("get authorities");
            return AuthorityUtils.createAuthorityList("user", "admin");
        });
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationProvider provider) {
        /*return new ProviderManager(provider);*/
        return authentication -> {
            System.out.println(authentication.getCredentials());
            System.out.println(authentication.getPrincipal());
            System.out.println(authentication.getAuthorities());
            System.out.println(authentication.getDetails());

            var username = (String) authentication.getPrincipal();
            var userDetails = userDetailService.loadUserByUsername(username);
            if (!passwordEncoder.matches((String) authentication.getCredentials(), userDetails.getPassword())) {
                throw new BadCredentialsException("密码错误");
            }
            return authentication;
        };
    }

}
