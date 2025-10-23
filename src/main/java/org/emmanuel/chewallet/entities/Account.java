package org.emmanuel.chewallet.entities;

import jakarta.persistence.*;

import java.util.List;
import java.util.Random;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, length = 22)
    private String cvu;

    @Column(unique = true, length = 15)
    private String alias;

    private Float balance = 0.0F;

    @OneToMany(mappedBy = "account")
    private List<Transaction> transactions;

    @PostPersist
    public void asignarCvuUnico() {
        this.cvu = String.format("%022d", this.id);
    }
    @PrePersist
    public void generarCvuAlias() {
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

    public Account() {
    }

    public String getCvu() {
        return cvu;
    }

    public void setCvu(String cvu) {
        this.cvu = cvu;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Float getBalance() {
        return balance;
    }

    public void setBalance(Float balance) {
        this.balance = balance;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
