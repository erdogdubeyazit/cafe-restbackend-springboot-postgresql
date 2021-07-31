package com.beb.coffeeshop.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletResponse;

import com.beb.coffeeshop.model.Currency;
import com.beb.coffeeshop.presentation.payload.admin.ToppingPayload;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ToppingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetAll_should_fail_withoutAuthentication() throws Exception {
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.get("/api/toppings");

        ResultActions resultActions = mockMvc.perform(mockHttpServletRequestBuilder);
        MvcResult mvcResult = resultActions.andReturn();

        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, mvcResult.getResponse().getStatus());
    }

    @Test
    @WithMockUser(authorities = { "ROLE_TEST" })
    public void getAll_with_undefined_role_should_fail() throws Exception {
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.get("/api/toppings");

        ResultActions resultActions = mockMvc.perform(mockHttpServletRequestBuilder);
        MvcResult mvcResult = resultActions.andReturn();

        assertEquals(HttpServletResponse.SC_FORBIDDEN, mvcResult.getResponse().getStatus());
    }

    @Test
    @WithMockUser(username = "TEST_USER", authorities = { "ROLE_USER" })
    public void create_with_ROLE_USER_should_fail() throws Exception {
        ToppingPayload payload = new ToppingPayload();
        payload.setName("TEST");
        payload.setCurrency(Currency.EUR.name());
        payload.setPrice(BigDecimal.TEN);

        ObjectMapper objectMapper = new ObjectMapper();

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.post("/api/toppings")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(payload));

        ResultActions resultActions = mockMvc.perform(mockHttpServletRequestBuilder);
        resultActions.andExpect(MockMvcResultMatchers.status().isForbidden());

    }

    @Test
    @WithMockUser(username = "TEST_USER", authorities = { "ROLE_ADMIN" })
    public void create_with_wrong_payload_should_fail() throws Exception {
        ToppingPayload payload = new ToppingPayload();
        ObjectMapper objectMapper = new ObjectMapper();

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.post("/api/toppings")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(payload));

        ResultActions resultActions = mockMvc.perform(mockHttpServletRequestBuilder);
        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    @WithMockUser(username = "TEST_USER", authorities = { "ROLE_USER" })
    public void update_with_ROLE_USER_should_fail() throws Exception {
        ToppingPayload payload = new ToppingPayload();
        payload.setName("TEST");
        payload.setCurrency(Currency.EUR.name());
        payload.setPrice(BigDecimal.TEN);

        ObjectMapper objectMapper = new ObjectMapper();

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.patch("/api/toppings/1")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(payload));

        ResultActions resultActions = mockMvc.perform(mockHttpServletRequestBuilder);
        resultActions.andExpect(MockMvcResultMatchers.status().isForbidden());

    }

    @Test
    @WithMockUser(username = "TEST_USER", authorities = { "ROLE_ADMIN" })
    public void update_with_wrong_method_should_fail() throws Exception {
        ToppingPayload payload = new ToppingPayload();
        payload.setName("TEST");
        payload.setCurrency(Currency.EUR.name());
        payload.setPrice(BigDecimal.TEN);

        ObjectMapper objectMapper = new ObjectMapper();

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.post("/api/toppings/1")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(payload));

        ResultActions resultActions = mockMvc.perform(mockHttpServletRequestBuilder);
        resultActions.andExpect(MockMvcResultMatchers.status().isMethodNotAllowed());

    }

    @Test
    @WithMockUser(username = "TEST_USER", authorities = { "ROLE_ADMIN" })
    public void update_with_wrong_payload_should_fail() throws Exception {
        ToppingPayload payload = new ToppingPayload();
        ObjectMapper objectMapper = new ObjectMapper();

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.patch("/api/toppings/1")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(payload));

        ResultActions resultActions = mockMvc.perform(mockHttpServletRequestBuilder);
        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    @WithMockUser(username = "TEST_USER", authorities = { "ROLE_USER" })
    public void delete_with_ROLE_USER_should_fail() throws Exception {
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.delete("/api/toppings/1");

        ResultActions resultActions = mockMvc.perform(mockHttpServletRequestBuilder);
        resultActions.andExpect(MockMvcResultMatchers.status().isForbidden());

    }

}
