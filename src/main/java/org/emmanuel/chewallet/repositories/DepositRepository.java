package org.emmanuel.chewallet.repositories;

import org.emmanuel.chewallet.entities.Deposit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepositRepository extends JpaRepository<Deposit,Long> {
    Optional<Deposit> findByExternalReference(String externalReference);
}
