package org.emmanuel.chewallet.repositories;

import java.util.Optional;

import org.emmanuel.chewallet.entities.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long>{
    Optional<PasswordResetToken> findByToken(String token);
}
