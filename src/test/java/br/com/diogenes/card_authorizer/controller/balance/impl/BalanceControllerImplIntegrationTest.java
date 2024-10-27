package br.com.diogenes.card_authorizer.controller.balance.impl;

import br.com.diogenes.card_authorizer.repository.balance.BalanceRepository;
import br.com.diogenes.card_authorizer.repository.balance.entity.Balance;
import br.com.diogenes.card_authorizer.service.balance.BalanceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "spring.config.name=application-test")
@AutoConfigureMockMvc
@Transactional
class BalanceControllerImplIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BalanceRepository balanceRepository;

    @Mock
    private BalanceService balanceService;

    @InjectMocks
    private BalanceControllerImpl balanceController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        balanceRepository.deleteAll();
        Balance balance = new Balance().buildBalance("123", BigDecimal.valueOf(100), BigDecimal.valueOf(50), BigDecimal.valueOf(75));
        balanceRepository.save(balance);
    }

    @Test
    void testCreateBalance() throws Exception {
        String newBalanceJson = """
            {
                "account": "456",
                "meal": 150.0,
                "cash": 75.0,
                "food": 100.0
            }
            """;

        mockMvc.perform(post("/api/balance")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newBalanceJson))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.account", is("456")))
            .andExpect(jsonPath("$.meal", is(150.0)))
            .andExpect(jsonPath("$.cash", is(75.0)))
            .andExpect(jsonPath("$.food", is(100.0)));
    }

    @Test
    void testDepositToAccount_Success() throws Exception {
        ResultActions result = mockMvc.perform(put("/api/balance/123/deposit")
                .param("meal", "10")
                .param("cash", "20")
                .param("food", "15"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.meal", is(110)))
            .andExpect(jsonPath("$.cash", is(70)))
            .andExpect(jsonPath("$.food", is(90)));
    }

    @Test
    void testDepositToAccount_AccountNotFound() throws Exception {
        mockMvc.perform(put("/api/balance/999/deposit")
                .param("meal", "10")
                .param("cash", "20")
                .param("food", "15"))
            .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllBalances() throws Exception {
        mockMvc.perform(get("/api/balance")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].account", is("123")))
            .andExpect(jsonPath("$.content[0].meal", is(100)))
            .andExpect(jsonPath("$.content[0].cash", is(50)))
            .andExpect(jsonPath("$.content[0].food", is(75)));
    }

    @Test
    void testGetBalanceByAccount_Success() throws Exception {
        mockMvc.perform(get("/api/balance/123")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.account", is("123")))
            .andExpect(jsonPath("$.meal", is(100)))
            .andExpect(jsonPath("$.cash", is(50)))
            .andExpect(jsonPath("$.food", is(75)));
    }

    @Test
    void testGetBalanceByAccount_NotFound() throws Exception {
        mockMvc.perform(get("/api/balance/999")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }
}
