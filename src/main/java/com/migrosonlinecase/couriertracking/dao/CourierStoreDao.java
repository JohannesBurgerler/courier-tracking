package com.migrosonlinecase.couriertracking.dao;

import com.migrosonlinecase.couriertracking.exception.business.CourierNotFoundException;
import com.migrosonlinecase.couriertracking.exception.technical.DataAccessException;
import com.migrosonlinecase.couriertracking.factory.DaoFactory;
import com.migrosonlinecase.couriertracking.model.CourierStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Data access object for managing courier store entries.
 * Tracks when couriers enter store premises and maintains entry history.
 */
@Component
public class CourierStoreDao {
    private static final Map<Integer, ArrayList<CourierStore>> courierStoreMap = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(CourierStoreDao.class);

    private final IJsonCourierStoreDao jsonCourierStoreDao;

    @Autowired
    public CourierStoreDao(DaoFactory daoFactory) {
        this.jsonCourierStoreDao = daoFactory.createCourierStoreDao();
    }

    /**
     * Records a new store entry for a courier.
     * Updates both in-memory and persistent storage.
     */
    public void addCourierStore(CourierStore courierStore){
        if (courierStore == null) {
            throw new IllegalArgumentException("CourierStore cannot be null");
        }
        try {
            ArrayList<CourierStore> existingList = courierStoreMap.computeIfAbsent(
                    courierStore.getCourierId(),
                    k -> new ArrayList<>()
            );
            existingList.add(courierStore);
            logger.debug("Added store entry for courier ID: {} at store: {}",
                    courierStore.getCourierId(),
                    courierStore.getStoreName()
            );
            jsonCourierStoreDao.saveCourierStores(courierStoreMap);

        } catch (Exception e) {
            String message = String.format("Failed to add store entry for courier ID: %d",
                    courierStore.getCourierId());
            logger.error(message, e);
            throw new DataAccessException("add", "courier store", e);
        }
    }

    /**
     * Gets all store entries for a specific courier.
     * @throws CourierNotFoundException if courier has no entries
     */
    public ArrayList<CourierStore> getAllGeolocationListByCourier(int courierId){
        return Optional.ofNullable(courierStoreMap.get(courierId))
                .orElseThrow(() -> {
                    logger.warn("No store entries found for courier ID: {}", courierId);
                    return new CourierNotFoundException(courierId);
                });
    }

    /**
     * Retrieves the most recent store entry for a courier.
     * Returns null if courier has no previous entries.
     */
    public CourierStore getLastGeoLocationByCourierId(int courierId){
        try {
            ArrayList<CourierStore> locations = getAllGeolocationListByCourier(courierId);
            return locations.stream()
                    .max(Comparator.comparing(CourierStore::getEntranceDate))
                    .orElse(null);
        } catch (CourierNotFoundException e) {
            logger.debug("No previous locations found for courier ID: {}", courierId);
            return null;
        } catch (Exception e) {
            String message = String.format("Failed to get last location for courier ID: %d", courierId);
            logger.error(message, e);
            throw new DataAccessException("retrieve", "last courier store", e);
        }
    }
}
