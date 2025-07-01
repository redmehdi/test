package com.example.pilotes.controller;

import com.example.pilotes.model.PiloteOrder;
import com.example.pilotes.service.PiloteOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final PiloteOrderService service;

    @PostMapping
    public ResponseEntity<PiloteOrder> create(@RequestBody @Valid PiloteOrder order) {
        PiloteOrder saved = service.create(order);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PiloteOrder> update(@PathVariable Long id, @RequestBody @Valid PiloteOrder order) {
        Optional<PiloteOrder> updated = service.update(id, order);
        return updated.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public List<PiloteOrder> search(@RequestParam String name) {
        return service.searchByClientName(name);
    }
}
