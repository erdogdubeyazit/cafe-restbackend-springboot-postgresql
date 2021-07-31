package com.beb.coffeeshop.controller;

import static org.junit.Assert.assertEquals;

import javax.servlet.http.HttpServletResponse;

import com.beb.coffeeshop.presentation.payload.order.AddBeverageToOrderPayload;
import com.beb.coffeeshop.presentation.payload.order.AddToppingToBeverage;
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
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void create_without_authentication_should_fail() throws Exception {
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.get("/api/orders");

        ResultActions resultActions = mockMvc.perform(mockHttpServletRequestBuilder);
        MvcResult mvcResult = resultActions.andReturn();

        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, mvcResult.getResponse().getStatus());
    }

    @Test
    public void get_order_without_authenticatiion_should_fail() throws Exception {
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders
                .get("/api/orders/orderNumber");

        ResultActions resultActions = mockMvc.perform(mockHttpServletRequestBuilder);
        resultActions.andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "TEST_USER", authorities = { "ROLE_TEST" })
    public void addBeverageToOrder_with_wrong_role_should_fail() throws Exception {
        AddBeverageToOrderPayload payload = new AddBeverageToOrderPayload();
        payload.setBeverageName("beverageName");
        payload.setCount(1);
        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders
                .post("/api/orders/test/beverages").content(objectMapper.writeValueAsString(payload))
                .contentType(MediaType.APPLICATION_JSON);

        ResultActions resultActions = mockMvc.perform(mockHttpServletRequestBuilder);
        resultActions.andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(username = "TEST_USER", authorities = { "ROLE_USER" })
    public void addBeverageToOrder_with_wrong_payload_should_fail() throws Exception {
        AddBeverageToOrderPayload payload = new AddBeverageToOrderPayload();
        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders
                .post("/api/orders/test/beverages").content(objectMapper.writeValueAsString(payload))
                .contentType(MediaType.APPLICATION_JSON);

        ResultActions resultActions = mockMvc.perform(mockHttpServletRequestBuilder);
        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void removeBeverageFromOrder_without_authentication_should_fail() throws Exception {
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders
                .delete("/api/orders/testOrderNumber/beverages/1");

        ResultActions resultActions = mockMvc.perform(mockHttpServletRequestBuilder);
        resultActions.andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "TEST_USER", authorities = { "ROLE_USER" })
    public void addToppingToBeverage_with_wrong_payload_should_fail() throws Exception {
        AddToppingToBeverage payload = new AddToppingToBeverage();
        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders
                .post("/api/orders/orderNumber/beverages/1/toppings").content(objectMapper.writeValueAsString(payload))
                .contentType(MediaType.APPLICATION_JSON);

        ResultActions resultActions = mockMvc.perform(mockHttpServletRequestBuilder);
        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    public void removeToppingFromBeverage_without_authentication_should_fail() throws Exception {
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders
                .delete("/api/orders/orderNumber/beverages/1/toppings/toppingName");

        ResultActions resultActions = mockMvc.perform(mockHttpServletRequestBuilder);
        resultActions.andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void completeOrder_without_authentication_should_fail() throws Exception {
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders
                .patch("/api/orders/orderNumber/complete");

        ResultActions resultActions = mockMvc.perform(mockHttpServletRequestBuilder);
        resultActions.andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void cancelOrder_without_authentication_should_fail() throws Exception {
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders
                .patch("/api/orders/orderNumber/cancel");

        ResultActions resultActions = mockMvc.perform(mockHttpServletRequestBuilder);
        resultActions.andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }
}
