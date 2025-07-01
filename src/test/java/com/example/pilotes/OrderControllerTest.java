package com.example.pilotes;

import com.example.pilotes.model.Client;
import com.example.pilotes.model.PiloteOrder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;

    @Test
    void createAndUpdateOrder() throws Exception {
        Client client = new Client();
        client.setFirstName("John");
        client.setLastName("Doe");
        client.setPhone("123");

        PiloteOrder order = new PiloteOrder();
        order.setClient(client);
        order.setPilotes(5);
        order.setAddress("addr");

        String body = mapper.writeValueAsString(order);
        String response = mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        PiloteOrder saved = mapper.readValue(response, PiloteOrder.class);

        saved.setAddress("new");
        String updateBody = mapper.writeValueAsString(saved);
        mockMvc.perform(put("/orders/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").value("new"));
    }

    @Test
    void searchRequiresAuth() throws Exception {
        mockMvc.perform(get("/orders/search?name=a"))
                .andExpect(status().isUnauthorized());
    }
}
