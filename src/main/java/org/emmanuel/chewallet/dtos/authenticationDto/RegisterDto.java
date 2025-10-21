package org.emmanuel.chewallet.dtos.authenticationDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterDto(
        @NotBlank(message = "Usuario no puede estar vacío")
        @Size(min = 6, max = 20, message = "Usuario debe tener entre 6 y 20 caracteres")
        String username,
        @NotBlank
        @NotNull
        @Size(min = 8, max = 8, message = "DNI debe tener 8 caracteres")
        @Pattern(regexp = "\\d{8}", message = "DNI debe contener solo números")
        String dni,
        @NotBlank
        @NotNull
        @Size(min = 6, max = 50, message = "Email debe tener entre 6 y 50 caracteres")
        @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Email debe tener un formato válido")
        String email,
        @NotBlank
        @NotNull
        @Size(min = 8, max = 20, message = "Contraseña debe tener entre 8 y 20 caracteres")
        String password,
        @NotBlank
        @NotNull
        String repeatPassword,
        @NotBlank
        @NotNull
        String name,
        @NotBlank
        @NotNull
        String lastname,
        @NotBlank
        @NotNull
        String birthdate,
        @NotBlank
        @NotNull
        @Size(min = 8, message = "Teléfono debe tener al menos 8 caracteres")
        String phone
) {
}
