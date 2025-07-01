package com.example.pilotes.repository;

import com.example.pilotes.model.PiloteOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PiloteOrderRepository extends JpaRepository<PiloteOrder, Long> {
    List<PiloteOrder> findByClientFirstNameContainingIgnoreCase(String firstName);
    List<PiloteOrder> findByClientLastNameContainingIgnoreCase(String lastName);
}
