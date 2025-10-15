package org.emmanuel.chewallet.repositories;

import org.emmanuel.chewallet.entities.Deposit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepositService extends JpaRepository<Deposit, Integer>
{
}
