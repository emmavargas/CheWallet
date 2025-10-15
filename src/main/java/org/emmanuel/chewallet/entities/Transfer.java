package org.emmanuel.chewallet.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
@Entity
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String transactionId;

    private String destinationCvu;
    private Double amount;
    private String description;
    private String status;
    private LocalDateTime date;

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
