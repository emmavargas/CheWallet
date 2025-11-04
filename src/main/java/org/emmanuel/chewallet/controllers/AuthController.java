package org.emmanuel.chewallet.controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.emmanuel.chewallet.dtos.ApiErrorDto;
import org.emmanuel.chewallet.dtos.authenticationDto.LoginDto;
import org.emmanuel.chewallet.dtos.authenticationDto.RegisterDto;
import org.emmanuel.chewallet.dtos.recuperarDto.ForgotPasswordRequest;
import org.emmanuel.chewallet.dtos.recuperarDto.ResetPasswordRequest;
import org.emmanuel.chewallet.services.AuthService;
import org.emmanuel.chewallet.services.PasswordResetService;
import org.emmanuel.chewallet.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;



@RestController
@RequestMapping("/api/auth")
public class AuthController {
     
    private final AuthService authService;
    private final UserService userService;
    private final PasswordResetService passwordResetService;


    public AuthController(AuthService authService, UserService userService,  PasswordResetService passwordResetService) {
        this.authService = authService;
        this.userService = userService;
        this.passwordResetService = passwordResetService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto loginDto, HttpServletResponse response) {
         try {
            String token = authService.generateToken(loginDto);
            Cookie cookie = authService.createCookieWithToken(token);
            response.addCookie(cookie);
//             response.addHeader("Set-Cookie", cookie);

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

    @GetMapping("/check")
    public ResponseEntity<?> checkLogin(@CookieValue(name = "jwt", required = false) String token,  HttpServletResponse response) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "No autenticado"));
        }

        try {
            if (!authService.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Token inválido"));
            }
            String username = authService.getClaimsFromToken(token);
            String tokenRenovate = authService.refreshToken(token);
            Cookie cookie = authService.createCookieWithToken(tokenRenovate);
            response.addCookie(cookie);
//            response.addHeader("Set-Cookie", cookie);

            return ResponseEntity.ok(Map.of("message", "Autenticado", "username", username));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Token inválido"));
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody @Valid ForgotPasswordRequest request) {
        passwordResetService.createPasswordResetToken(request.email());
        return ResponseEntity.ok("Correo de recuperación enviado");
    }


    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
        passwordResetService.resetPassword(request.token(), request.newPassword());
        return ResponseEntity.ok("Contraseña actualizada correctamente");
    }

}
