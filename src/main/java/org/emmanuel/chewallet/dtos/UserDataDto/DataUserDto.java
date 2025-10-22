package org.emmanuel.chewallet.dtos.UserDataDto;

public record DataUserDto(
        String name,
        String lastname,
        String username,
        String dni,
        String email,
        String phone,
        String cvu,
        String alias
) {
}
