package org.emmanuel.chewallet.services;

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

}
