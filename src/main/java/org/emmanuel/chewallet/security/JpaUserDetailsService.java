package org.emmanuel.chewallet.security;

import org.emmanuel.chewallet.Enums.Roles;
import org.emmanuel.chewallet.entities.User;
import org.emmanuel.chewallet.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class JpaUserDetailsService implements UserDetailsService {

    public JpaUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private final UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user =userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .accountLocked(!user.isActive())
                .credentialsExpired(false)
                .accountExpired(false)
                .disabled(!user.isActive())
                .authorities(Roles.ROLE_USER.toString())
                .build();
    }
}
