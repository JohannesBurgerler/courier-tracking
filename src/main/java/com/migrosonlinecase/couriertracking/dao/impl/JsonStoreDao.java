package com.migrosonlinecase.couriertracking.dao.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.migrosonlinecase.couriertracking.dao.IJsonStoreDao;
import com.migrosonlinecase.couriertracking.exception.business.StoreNotFoundException;
import com.migrosonlinecase.couriertracking.model.Store;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;


import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Data access object for managing store location data.
 * Loads and provides access to static store information.
 */
@Component
public class JsonStoreDao implements IJsonStoreDao {
    private final ObjectMapper objectMapper;
    private Map<String, Store> storeMap = new HashMap<String, Store>();
    String filePath = "src/main/resources/static/stores.json";

    public JsonStoreDao() {
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Loads store data from JSON file on startup.
     * Maps store data by store name for quick access.
     */
    @PostConstruct
    public void loadStores(){
        String filePath = "src/main/resources/static/stores.json";

        try{
            List<Store> storeList = objectMapper.readValue(
                    new File(filePath),
                    new TypeReference<List<Store>>(){}
            );

            storeMap = storeList.stream().collect(Collectors.toMap(
                    Store::getName,
                    store -> store,
                    (existing, replacement) -> existing,
                    LinkedHashMap::new));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns all available store locations.
     */
    public ArrayList<Store> getAllStores(){
        return new ArrayList<>(storeMap.values());
    }

    /**
     * Retrieves store information by store name.
     */
    public Store getStoreByName(String storeName) {
        return Optional.ofNullable(storeMap.get(storeName))
                .orElseThrow(() -> new StoreNotFoundException(storeName));
    }

    /**
     * Checks if a store exists with the given name.
     */
    public boolean hasStore(String name){
        return storeMap.containsKey(name);
    }
}
