package org.emmanuel.chewallet.services;

import jakarta.servlet.http.Cookie;
import org.emmanuel.chewallet.dtos.authenticationDto.LoginDto;
import org.emmanuel.chewallet.repositories.UserRepository;
import org.emmanuel.chewallet.security.JpaUserDetailsService;
import org.emmanuel.chewallet.security.JwtUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final JpaUserDetailsService jpaUserDetailsService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtils jwtUtils, JpaUserDetailsService jpaUserDetailsService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.jpaUserDetailsService = jpaUserDetailsService;
    }


    @Transactional(readOnly = true)
    public String generateToken(LoginDto loginDto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.username(),loginDto.password()));
        var userDetails = jpaUserDetailsService.loadUserByUsername(loginDto.username());
        return  jwtUtils.generateToken(userDetails);
    }
    public String refreshToken(String token) {
        var claims = jwtUtils.getClaims(token);
        String username = claims.getSubject();
        var userDetails = jpaUserDetailsService.loadUserByUsername(username);
        return jwtUtils.generateToken(userDetails);
    }

    @Transactional(readOnly = true)
    public String getClaimsFromToken(String token) {
        return jwtUtils.getClaims(token).getSubject();
    }

    @Transactional(readOnly = true)
    public boolean validateToken(String token) {
        return jwtUtils.validateToken(token);
    }

    @Transactional(readOnly = true)
    public String createCookieWithToken(String token) {
//        Cookie cookie = new Cookie("jwt", token);
//        cookie.setHttpOnly(true);
//        cookie.setSecure(false);
//        cookie.setPath("/");
//        cookie.setMaxAge(60 * 60);
//        return  cookie;
        return "jwt=" + token +
                "; Path=/" +
                "; HttpOnly" +
                "; Max-Age=" + (60 * 60) +
                "; SameSite=Strict";
    }
}
