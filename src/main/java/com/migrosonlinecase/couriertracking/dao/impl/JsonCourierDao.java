package com.migrosonlinecase.couriertracking.dao.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.migrosonlinecase.couriertracking.dao.IJsonCourierDao;
import com.migrosonlinecase.couriertracking.model.Courier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Data access object for persisting courier location data to JSON files.
 * Handles saving and loading courier locations using a file-based storage system.
 */
@Component
public class JsonCourierDao implements IJsonCourierDao {
    private static final Logger logger = LoggerFactory.getLogger(JsonCourierDao.class);
    private final ObjectMapper objectMapper;
    private final static String filePath = "src/main/resources/static/couriers.json";


    public JsonCourierDao(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    /**
     * Persists courier location data to JSON file.
     * Overwrites existing data with the current state of the courier map.
     */
    @Override
    public void saveCouriers(Map<Integer, ArrayList<Courier>> courierMap) {
        try {
            objectMapper.writeValue(new File("src/main/resources/static/couriers.json"), courierMap);
            logger.debug("Saved the new JSON file for courierMap to src/main/resources/static/couriers.json");
        }catch(IOException e){
            logger.debug("Failed while writing to file src/main/resources/static/couriers.json");
            throw new RuntimeException("Failed while writing to file couriers.json", e);
        }
    }

    /**
     * Loads all courier location data from the JSON file.
     * @return Map of courier IDs to their location histories
     */
    @Override
    public Map<Integer, ArrayList<Courier>> loadCouriers() {
        try {
            return objectMapper.readValue(
                    new File(filePath),
                    new TypeReference<HashMap<Integer, ArrayList<Courier>>>() {
                    });
        } catch (IOException e) {
            logger.debug("Failed while loading from file src/main/resources/static/couriers.json");
            throw new RuntimeException("Failed while reading from file couriers.json", e);
        }
    }

}
