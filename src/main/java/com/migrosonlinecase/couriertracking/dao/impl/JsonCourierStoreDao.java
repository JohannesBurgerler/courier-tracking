package com.migrosonlinecase.couriertracking.dao.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.migrosonlinecase.couriertracking.dao.IJsonCourierStoreDao;
import com.migrosonlinecase.couriertracking.model.CourierStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Loads all courier location data from the JSON file.
 * @return Map of courier IDs to their location histories
 */
@Component
public class JsonCourierStoreDao implements IJsonCourierStoreDao {
    private static final Logger logger = LoggerFactory.getLogger(JsonCourierStoreDao.class);
    private final ObjectMapper objectMapper;
    private final static String filePath = "src/main/resources/static/courierstores.json";


    public JsonCourierStoreDao(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    /**
     * Saves store entry records to JSON file.
     * Overwrites existing data with current state.
     */
    @Override
    public void saveCourierStores(Map<Integer, ArrayList<CourierStore>> courierStoreMap) {
        try {
            objectMapper.writeValue(new File("src/main/resources/static/courierstores.json"), courierStoreMap);
            logger.debug("Saved the new JSON file for courierStoreMap to src/main/resources/static/courierstores.json");
        }catch(IOException e){
            logger.debug("Failed while writing to file src/main/resources/static/courierstores.json");
            throw new RuntimeException("Failed while writing to file courierstores.json", e);
        }
    }

    /**
     * Loads all store entry records from JSON file.
     * @return Map of courier IDs to their store entry histories
     */
    @Override
    public Map<Integer, ArrayList<CourierStore>> loadCourierStores() {
        try {
            return objectMapper.readValue(
                    new File(filePath),
                    new TypeReference<HashMap<Integer, ArrayList<CourierStore>>>() {
                    });
        } catch (IOException e) {
            logger.debug("Failed while loading from file src/main/resources/static/courierstores.json");
            throw new RuntimeException("Failed while reading from file courierstores.json", e);
        }
    }

}
