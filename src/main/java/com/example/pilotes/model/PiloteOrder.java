package com.example.pilotes.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
public class PiloteOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String orderNumber;

    @NotBlank
    private String address;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private Client client;

    @NotNull
    @Min(5)
    @Max(15)
    private Integer pilotes;

    private BigDecimal total;

    private LocalDateTime createdAt;
}
