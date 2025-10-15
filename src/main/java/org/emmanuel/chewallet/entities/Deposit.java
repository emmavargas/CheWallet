package org.emmanuel.chewallet.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
@Entity
public class Deposit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String transactionId;
    private Float amount;
    private LocalDateTime date;
    private String status;
    private String externalReference;

    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;

    @PrePersist
    public void prePersist() {
        if (this.transactionId == null) {
            this.transactionId = "TX-" + java.util.UUID.randomUUID();
        }
    }

}
