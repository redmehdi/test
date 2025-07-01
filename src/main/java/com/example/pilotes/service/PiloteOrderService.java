package com.example.pilotes.service;

import com.example.pilotes.model.PiloteOrder;
import com.example.pilotes.repository.PiloteOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PiloteOrderService {
    private final PiloteOrderRepository repository;
    private static final BigDecimal PRICE_PER_PILOTE = new BigDecimal("1.33");

    @Transactional
    public PiloteOrder create(PiloteOrder order) {
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setTotal(PRICE_PER_PILOTE.multiply(BigDecimal.valueOf(order.getPilotes())));
        order.setCreatedAt(LocalDateTime.now());
        return repository.save(order);
    }

    @Transactional
    public Optional<PiloteOrder> update(Long id, PiloteOrder updates) {
        return repository.findById(id).map(existing -> {
            if (existing.getCreatedAt().plusMinutes(5).isBefore(LocalDateTime.now())) {
                return null;
            }
            existing.setAddress(updates.getAddress());
            existing.setPilotes(updates.getPilotes());
            existing.setTotal(PRICE_PER_PILOTE.multiply(BigDecimal.valueOf(updates.getPilotes())));
            return repository.save(existing);
        });
    }

    public List<PiloteOrder> searchByClientName(String name) {
        List<PiloteOrder> list = repository.findByClientFirstNameContainingIgnoreCase(name);
        list.addAll(repository.findByClientLastNameContainingIgnoreCase(name));
        return list;
    }
}
