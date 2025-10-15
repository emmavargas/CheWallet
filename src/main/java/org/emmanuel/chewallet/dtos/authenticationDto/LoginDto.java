package org.emmanuel.chewallet.dtos.authenticationDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LoginDto(
        @NotNull
        @NotBlank
        @Size(min = 6, max = 20, message = "Usuario debe tener entre 6 y 20 caracteres")
        String username,
        @Size(min = 6, max = 20, message = "Usuario debe tener entre 6 y 20 caracteres")
        String password
) {
}
