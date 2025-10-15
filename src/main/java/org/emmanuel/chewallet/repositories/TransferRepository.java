package org.emmanuel.chewallet.repositories;

import org.emmanuel.chewallet.entities.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferRepository extends JpaRepository<Transfer,Long> {
}
