package org.vim.pokeproject.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "access")
public class Access {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idAccess", nullable = false)
    private Integer id;

    @Column(name = "accessed_at")
    private Instant accessedAt;

    @Column(name = "client_ip", length = 100)
    private String clientIp;

}