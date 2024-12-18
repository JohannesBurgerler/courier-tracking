package com.migrosonlinecase.couriertracking.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.migrosonlinecase.couriertracking.dao.impl.JsonCourierDao;
import com.migrosonlinecase.couriertracking.model.Courier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class JsonCourierDaoTest {
    @Autowired
    private JsonCourierDao jsonCourierDao;

    @Autowired
    private ObjectMapper objectMapper;

    private Map<Integer, ArrayList<Courier>> testData;
    private LocalDateTime baseTime;

    @BeforeEach
    void setUp() {
        baseTime = LocalDateTime.now();
        testData = new HashMap<>();
        ArrayList<Courier> locations = new ArrayList<>();
        locations.add(new Courier(1, 40.9923307, 29.1244229, baseTime));
        testData.put(1, locations);
    }

    @Test
    void shouldSaveAndLoadCouriers() {
        // Save test data
        jsonCourierDao.saveCouriers(testData);

        // Load and verify
        Map<Integer, ArrayList<Courier>> loaded = jsonCourierDao.loadCouriers();
        assertEquals(testData.size(), loaded.size());
        assertTrue(loaded.containsKey(1));
        assertEquals(testData.get(1).size(), loaded.get(1).size());
    }
}