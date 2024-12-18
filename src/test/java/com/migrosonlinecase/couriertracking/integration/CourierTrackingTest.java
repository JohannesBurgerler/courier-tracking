package com.migrosonlinecase.couriertracking.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.migrosonlinecase.couriertracking.dao.IJsonStoreDao;
import com.migrosonlinecase.couriertracking.dto.CourierLocationRequest;
import com.migrosonlinecase.couriertracking.model.Store;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CourierTrackingTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IJsonStoreDao IJsonStoreDao;

    private ArrayList<Store> stores;
    private LocalDateTime baseTime;

    @BeforeEach
    void setUp() {
        stores = IJsonStoreDao.getAllStores();
        baseTime = LocalDateTime.now();
    }

    // Tests if system detects store entry when courier is exactly at store location
    @Test
    void shouldRecordStoreEntryWhenCourierAtExactStoreLocation() throws Exception {
        Store atasehir = stores.get(0);
        CourierLocationRequest request = new CourierLocationRequest(
                1,
                atasehir.getLat(),
                atasehir.getLng(),
                baseTime
        );

        mockMvc.perform(post("/v1/couriers/courier/location")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.storeEntranceRecorded").value(true))
                .andExpect(jsonPath("$.storeName").value(atasehir.getName()));
    }

    // Tests if system correctly ignores couriers outside the 100m radius
    // Around more than 100 meters in latitude
    @Test
    void shouldNotRecordStoreEntryWhenCourierBeyond100Meters() throws Exception {
        Store atasehir = stores.get(0);
        CourierLocationRequest request = new CourierLocationRequest(
                1,
                atasehir.getLat() + 0.001,
                atasehir.getLng(),
                baseTime
        );

        String requestJson = objectMapper.writeValueAsString(request);
        System.out.println("Request: " + requestJson);

        mockMvc.perform(post("/v1/couriers/courier/location")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.storeEntranceRecorded").value(false))
                .andExpect(jsonPath("$.storeName").isEmpty())
                .andDo(print());
    }

    // Verifies the 1-minute cooldown period between store entries
    @Test
    void shouldNotRecordReentryWithinOneMinute() throws Exception {
        Store atasehir = stores.get(0);
        int courierId = 6;

        // First entry
        CourierLocationRequest firstEntry = new CourierLocationRequest(
                courierId,
                atasehir.getLat(),
                atasehir.getLng(),
                baseTime
        );

        // First entry should be recorded
        mockMvc.perform(post("/v1/couriers/courier/location")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firstEntry)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.storeEntranceRecorded").value(true))
                .andDo(print());

        // Second entry within one minute
        CourierLocationRequest quickReentry = new CourierLocationRequest(
                courierId,
                atasehir.getLat(),
                atasehir.getLng(),
                baseTime.plusSeconds(30)
        );

        // Second entry should not be recorded as a store entrance
        mockMvc.perform(post("/v1/couriers/courier/location")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(quickReentry)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.storeEntranceRecorded").value(false))
                .andExpect(jsonPath("$.storeName").isEmpty())
                .andDo(print());
    }

    // Checks if distance is zero when courier has only one location
    @Test
    void shouldHaveZeroDistanceForSingleLocation() throws Exception {
        // Use a new courier ID
        int courierId = 5;
        Store atasehir = stores.get(0);
        CourierLocationRequest request = new CourierLocationRequest(
                courierId,
                atasehir.getLat(),
                atasehir.getLng(),
                baseTime
        );

        mockMvc.perform(post("/v1/couriers/courier/location")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        mockMvc.perform(get("/v1/couriers/courier/" + courierId + "/totaldistance"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalDistance").value(0.0));
    }

    // Verifies distance calculation when courier moves between locations
    @Test
    void shouldHaveNonZeroDistanceForDifferentLocations() throws Exception {
        Store atasehir = stores.get(0);
        Store novada = stores.get(1);

        // First location
        CourierLocationRequest firstLocation = new CourierLocationRequest(
                1,
                atasehir.getLat(),
                atasehir.getLng(),
                baseTime
        );

        mockMvc.perform(post("/v1/couriers/courier/location")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(firstLocation)));

        // Second location
        CourierLocationRequest secondLocation = new CourierLocationRequest(
                1,
                novada.getLat(),
                novada.getLng(),
                baseTime.plusMinutes(30)
        );

        mockMvc.perform(post("/v1/couriers/courier/location")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(secondLocation)));

        mockMvc.perform(get("/v1/couriers/courier/1/totaldistance"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalDistance").value(greaterThan(0.0)));
    }

    // Tests error handling for requests about unknown couriers
    @Test
    void shouldReturn404ForNonexistentCourier() throws Exception {
        mockMvc.perform(get("/v1/couriers/courier/999/totaldistance"))
                .andExpect(status().isNotFound());
    }

}