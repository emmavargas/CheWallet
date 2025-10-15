package org.emmanuel.chewallet.repositories;

import org.emmanuel.chewallet.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account,Long> {
}
