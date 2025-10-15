package org.emmanuel.chewallet.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Random;

@NoArgsConstructor
@Data
@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, length = 22)
    private String cvu;

    @Column(unique = true)
    private String alias;

    private Double balance = 0.0;

    @OneToMany(mappedBy = "account")
    private List<Transfer> transfers;

    @OneToMany(mappedBy = "account")
    private List<Deposit> deposits;

    @PrePersist
    public void generarCvuAlias() {
        if (this.cvu == null) {
            String prefijo = "001";
            long timestamp = System.currentTimeMillis();
            String timestampStr = String.valueOf(timestamp).substring(0, 12);
            Random random = new Random();
            int randomNum = random.nextInt(9000) + 1000;
            StringBuilder sb = new StringBuilder();
            sb.append(prefijo).append(timestampStr).append(randomNum);
            while (sb.length() < 22) {
                sb.append("0");
            }
            this.cvu = sb.toString();
        }
        if (this.alias == null) {
            String prefijo = "user";
            long timestamp = System.currentTimeMillis();
            String timestampStr = String.valueOf(timestamp).substring(8, 13);
            Random random = new Random();
            int randomNum = random.nextInt(900) + 100;
            StringBuilder sb = new StringBuilder();
            sb.append(prefijo).append(timestampStr).append(randomNum);
            this.alias = sb.toString();
        }
    }


}
