package org.emmanuel.chewallet.repositories;


import org.emmanuel.chewallet.entities.Account;
import org.emmanuel.chewallet.entities.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    Optional<Transaction> findByExternalReference(String externalReference);

    Page<Transaction> findByAccount(Account account, Pageable pageable);

    @Query(
            value = """
        SELECT t.id AS transactionId,
               a.id AS accountId,
               p_dest.name AS destinationName,
               p_dest.lastname AS destinationLastname,
               p_origin.name AS originName,
               p_origin.lastname AS originLastname,
               t.amount AS amount,
               t.type AS transactionType,
               t.date AS transactionDate
        FROM transaction t
        JOIN account a ON t.account_id = a.id
        JOIN account acc_dest ON t.account_destionation__id = acc_dest.id
        JOIN app_user u_dest ON acc_dest.id = u_dest.account_id
        JOIN profile p_dest ON u_dest.profile_id = p_dest.id
        JOIN account acc_origin ON t.account_origin_id = acc_origin.id
        JOIN app_user u_origin ON acc_origin.id = u_origin.account_id
        JOIN profile p_origin ON u_origin.profile_id = p_origin.id
        WHERE a.id = :accountId
        """,
            countQuery = """
        SELECT COUNT(*)
        FROM transaction t
        JOIN account a ON t.account_id = a.id
        WHERE a.id = :accountId
        """,
            nativeQuery = true
    )
    Page<TransactionHistoryProjection> findHistoryByAccountId(@Param("accountId") Long accountId, Pageable pageable);

}
