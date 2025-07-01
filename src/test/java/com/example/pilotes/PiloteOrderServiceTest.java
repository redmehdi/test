package com.example.pilotes;

import com.example.pilotes.model.Client;
import com.example.pilotes.model.PiloteOrder;
import com.example.pilotes.repository.PiloteOrderRepository;
import com.example.pilotes.service.PiloteOrderService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class PiloteOrderServiceTest {

    @Mock
    PiloteOrderRepository repository;

    @InjectMocks
    PiloteOrderService service;

    private PiloteOrder buildOrder() {
        Client client = new Client();
        client.setFirstName("A");
        client.setLastName("B");
        client.setPhone("1");

        PiloteOrder order = new PiloteOrder();
        order.setClient(client);
        order.setAddress("addr");
        order.setPilotes(5);
        return order;
    }

    @Test
    void createSetsCalculatedFields() {
        when(repository.save(any(PiloteOrder.class))).thenAnswer(i -> i.getArgument(0));

        PiloteOrder result = service.create(buildOrder());

        assertNotNull(result.getOrderNumber());
        assertNotNull(result.getCreatedAt());
        assertEquals(new BigDecimal("6.65"), result.getTotal());
        verify(repository).save(any(PiloteOrder.class));
    }

    @Test
    void updateWithinWindowUpdatesOrder() {
        PiloteOrder existing = buildOrder();
        existing.setId(1L);
        existing.setCreatedAt(LocalDateTime.now());
        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any(PiloteOrder.class))).thenAnswer(i -> i.getArgument(0));

        PiloteOrder updates = new PiloteOrder();
        updates.setAddress("new");
        updates.setPilotes(10);

        Optional<PiloteOrder> result = service.update(1L, updates);

        assertTrue(result.isPresent());
        assertEquals("new", result.get().getAddress());
        assertEquals(new BigDecimal("13.30"), result.get().getTotal());
        verify(repository).save(any(PiloteOrder.class));
    }

    @Test
    void updateAfterWindowReturnsEmpty() {
        PiloteOrder existing = buildOrder();
        existing.setId(1L);
        existing.setCreatedAt(LocalDateTime.now().minusMinutes(10));
        when(repository.findById(1L)).thenReturn(Optional.of(existing));

        Optional<PiloteOrder> result = service.update(1L, new PiloteOrder());

        assertTrue(result.isEmpty());
        verify(repository, never()).save(any());
    }
}
