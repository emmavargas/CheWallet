package org.emmanuel.chewallet.repositories;

import org.emmanuel.chewallet.entities.Account;
import org.emmanuel.chewallet.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Optional<User> findByAccount(Account account);
}
