package org.emmanuel.chewallet.controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.emmanuel.chewallet.dtos.ApiErrorDto;
import org.emmanuel.chewallet.dtos.authenticationDto.LoginDto;
import org.emmanuel.chewallet.dtos.authenticationDto.RegisterDto;
import org.emmanuel.chewallet.services.AuthService;
import org.emmanuel.chewallet.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto loginDto, HttpServletResponse response) {
         try {
            String token = authService.generateToken(loginDto);
            Cookie cookie = new Cookie("jwt", token);
            cookie.setHttpOnly(true);
            cookie.setSecure(false);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60);
            response.addCookie(cookie);

            Map<String, String> responseBody = Map.of("message", "login successful", "username", loginDto.username());
            return ResponseEntity.ok().body(responseBody);
         } catch (BadCredentialsException e) {
             ApiErrorDto apiErrorDto = new ApiErrorDto(
                     java.time.LocalDateTime.now(),
                     HttpStatus.BAD_REQUEST.value(),
                        "user o password incorrect",
                        Map.of("login", "Usuario o contraseña incorrectos")
             );
             return ResponseEntity.badRequest().body(apiErrorDto);
         }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDto registerDto){
        var user = userService.register(registerDto);
        Map<String, Object> response = Map.of("message", user);
        return ResponseEntity.ok().body(response);
    }
}
