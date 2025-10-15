package org.emmanuel.chewallet.services;

import org.emmanuel.chewallet.dtos.authenticationDto.LoginDto;
import org.emmanuel.chewallet.dtos.authenticationDto.RegisterDto;
import org.emmanuel.chewallet.dtos.authenticationDto.AuthSuccessDto;
import org.emmanuel.chewallet.entities.Account;
import org.emmanuel.chewallet.entities.Profile;
import org.emmanuel.chewallet.entities.User;
import org.emmanuel.chewallet.exceptions.EmailAlreadyExistsException;
import org.emmanuel.chewallet.exceptions.PasswordMismatchException;
import org.emmanuel.chewallet.exceptions.UserAlreadyExistsException;
import org.emmanuel.chewallet.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public AuthSuccessDto register(RegisterDto registerDto) {

        if(userRepository.existsByUsername(registerDto.username())) {
            throw new UserAlreadyExistsException("El usuario ya existe");
        }
        if(userRepository.existsByEmail(registerDto.email())) {
            throw new EmailAlreadyExistsException("El correo ya existe");
        }
        if(!registerDto.password().equals(registerDto.repeatPassword())) {
            throw new PasswordMismatchException("Las contraseñas no coinciden");
        }
        var user = new User();
        var profile = new Profile();
        var account = new Account();
        account.setBalance(0.0);
        profile.setName(registerDto.name());
        profile.setLastname(registerDto.lastname());
        profile.setDateOfBirth(registerDto.birthdate());
        profile.setPhoneNumber(registerDto.phone());

        user.setProfile(profile);
        user.setAccount(account);
        user.setDni(registerDto.dni());
        user.setUsername(registerDto.username());
        user.setEmail(registerDto.email());
        user.setPassword(passwordEncoder.encode(registerDto.password()));
        user.setActive(true);
        var userRegister =  userRepository.save(user);
        return new AuthSuccessDto("Usuario registrado",userRegister.getUsername());
    }



}
