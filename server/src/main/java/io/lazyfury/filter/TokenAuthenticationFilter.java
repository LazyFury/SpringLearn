package io.lazyfury.filter;

import io.lazyfury.utils.error.ProjectErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getHeader("Authorization") != null) {
            var token = extractTokenFromHeader(request);
            System.out.printf("filter %s\n", token);
            if (token == null) {
                response.sendError(ProjectErrorCode.TOKEN_NOT_FOUND.code, "token not found");
                response.flushBuffer();
                return;
            }

        }

        filterChain.doFilter(request, response);

    }


    public String extractTokenFromHeader(HttpServletRequest request) {
        var header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            return null;
        }
        return header.substring(7);
    }
}
