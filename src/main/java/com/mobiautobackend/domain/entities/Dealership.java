package com.mobiautobackend.domain.entities;

import jakarta.persistence.*;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "DEALERSHIP")
public class Dealership {

    @Id
    @Column(name = "ID", unique = true, nullable = false)
    private String id;

    @Column(name = "TRADE_NAME", nullable = false)
    private String tradeName;

    @Column(name = "CNPJ", unique = true, nullable = false)
    private String cnpj;

    @OneToMany
    private List<Member> members;

    @Column(name = "CREATION_DATE", nullable = false)
    private ZonedDateTime creationDate;

    @PrePersist
    private void prePersist() {
        id = UUID.randomUUID().toString();
        creationDate = ZonedDateTime.now();
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getTradeName() {
        return tradeName;
    }

    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }
}

