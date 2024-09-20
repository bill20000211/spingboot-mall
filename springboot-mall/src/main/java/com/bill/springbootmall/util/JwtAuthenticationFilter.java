package com.bill.springbootmall.util;

import com.bill.springbootmall.model.MyUserDetails;
import com.bill.springbootmall.service.impl.MyUserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private MyUserDetailService myUserDetailService;

    private static final String HEADER_AUTH = "Authorization";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader(HEADER_AUTH);
        if (!ObjectUtils.isEmpty(authHeader)) {
            String accessToken = authHeader.replace("Bearer ", "");
            String username = JwtUtil.getEmailFromToken(accessToken);

            MyUserDetails myUserDetails = myUserDetailService.loadUserByUsername(username);
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(
                            username, myUserDetails.getPassword(), myUserDetails.getAuthorities());
            SecurityContextHolder.getContext()
                    .setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

}
