package org.emmanuel.chewallet.repositories;

import org.emmanuel.chewallet.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account,Long> {
    boolean existsByCvu(String cvu);
    boolean existsByAlias(String alias);
    Optional<Account> findByAlias(String alias);
}
