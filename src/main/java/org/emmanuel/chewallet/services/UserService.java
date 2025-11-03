package org.emmanuel.chewallet.services;

import org.emmanuel.chewallet.dtos.UserDataDto.ResumeUserDto;
import org.emmanuel.chewallet.dtos.UserDataDto.UpdateAliasRequestDto;
import org.emmanuel.chewallet.dtos.UserDataDto.UpdateAliasResponseDto;
import org.emmanuel.chewallet.dtos.authenticationDto.RegisterDto;
import org.emmanuel.chewallet.dtos.authenticationDto.AuthSuccessDto;
import org.emmanuel.chewallet.dtos.UserDataDto.DataUserDto;
import org.emmanuel.chewallet.entities.Account;
import org.emmanuel.chewallet.entities.Profile;
import org.emmanuel.chewallet.entities.User;
import org.emmanuel.chewallet.exceptions.EmailAlreadyExistsException;
import org.emmanuel.chewallet.exceptions.PasswordMismatchException;
import org.emmanuel.chewallet.exceptions.UserAlreadyExistsException;
import org.emmanuel.chewallet.repositories.AccountRepository;
import org.emmanuel.chewallet.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final TransactionService transactionService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AccountRepository accountRepository, TransactionService transactionService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.accountRepository = accountRepository;
        this.transactionService = transactionService;
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
        account.setBalance(0.0F);
        profile.setName(registerDto.name());
        profile.setLastname(registerDto.lastname());
        profile.setDateOfBirth(registerDto.birthdate());
        profile.setPhoneNumber(registerDto.phone());

        user.setProfile(profile);
        user.setAccount(account);
        account.setUser(user);
        user.setDni(registerDto.dni());
        user.setUsername(registerDto.username());
        user.setEmail(registerDto.email());
        user.setPassword(passwordEncoder.encode(registerDto.password()));
        user.setActive(true);
        var userRegister =  userRepository.save(user);
        return new AuthSuccessDto("Usuario registrado",userRegister.getUsername());
    }

    @Transactional(readOnly = true)
    public DataUserDto getUserInfo(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow();
        Account account = user.getAccount();
        Profile profile = user.getProfile();
        return new DataUserDto(
                profile.getName(),
                profile.getLastname(),
                user.getUsername(),
                user.getDni(),
                user.getEmail(),
                profile.getPhoneNumber(),
                account.getCvu(),
                account.getAlias()
        );
    }

    @Transactional
    public UpdateAliasResponseDto updateAlias(UpdateAliasRequestDto updateAliasRequestDto){
        if(accountRepository.existsByAlias(updateAliasRequestDto.newAlias())){
            throw new UserAlreadyExistsException("El alias ya existe");
        }
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow();
        Account  account = user.getAccount();
        account.setAlias(updateAliasRequestDto.newAlias());
        userRepository.save(user);
        return new UpdateAliasResponseDto("Alias actualizado con exito", account.getAlias());
    }

    @Transactional(readOnly = true)
    public ResumeUserDto resumeUser(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow();
        Profile profile = user.getProfile();
        Account account = user.getAccount();
        return new ResumeUserDto(
                profile.getName(),
                profile.getLastname(),
                account.getCvu(),
                account.getAlias(),
                account.getBalance(),
                transactionService.getHistoryTransactions(0,3).getContent()
        );
    }

}
