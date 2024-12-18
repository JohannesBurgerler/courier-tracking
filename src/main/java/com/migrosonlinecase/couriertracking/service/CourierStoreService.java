package com.migrosonlinecase.couriertracking.service;

import com.migrosonlinecase.couriertracking.dao.CourierStoreDao;
import com.migrosonlinecase.couriertracking.dao.IJsonStoreDao;
import com.migrosonlinecase.couriertracking.model.Courier;
import com.migrosonlinecase.couriertracking.model.CourierStore;
import com.migrosonlinecase.couriertracking.model.Store;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Manages store entry detection for couriers.
 * Tracks when couriers enter stores and prevents duplicate entries within 1 minute.
 */
@Service
public class CourierStoreService {

    @Autowired
    private CourierStoreDao courierStoreDao;
    @Autowired
    private IJsonStoreDao IJsonStoreDao;

    private static final Logger logger = LoggerFactory.getLogger(CourierStoreService.class);

    public static final double EARTH_RADIUS = 6371;
    /**
     * Checks if a courier has entered a store (within 100m radius).
     * Returns store name if entry recorded, null otherwise.
     */
    public String storeEntrance(Courier courier) {
        ArrayList<Store> stores = IJsonStoreDao.getAllStores();

        for(Store store : stores) {
            double distCourierStore = calculateShortDistance(
                    courier.getLat(),
                    courier.getLng(),
                    store.getLat(),
                    store.getLng()
            );

            if(distCourierStore < 100 && !isRecentEntry(courier, store.getName())) {
                CourierStore courierStore = new CourierStore(
                        courier.getId(),
                        store.getName(),
                        courier.getTime()
                );
                courierStoreDao.addCourierStore(courierStore);
                return store.getName();
            }
        }
        return null;
    }

    /**
     * Checks if courier entered same store in last minute.
     * Used to prevent duplicate entries.
     */
    public boolean isRecentEntry(Courier courier, String storeName) {
        try {
            CourierStore lastEntry = courierStoreDao.getLastGeoLocationByCourierId(courier.getId());

            // If no previous entry, or previous entry was to a different store
            if(lastEntry == null || !lastEntry.getStoreName().equals(storeName)) {
                return false;
            }

            LocalDateTime lastEntranceDate = lastEntry.getEntranceDate();
            LocalDateTime courierEntranceDate = courier.getTime();

            long differenceInSeconds = Duration.between(courierEntranceDate, lastEntranceDate).toSeconds();
            // true if it's a recent entry (less than 1 minute)
            return Math.abs(differenceInSeconds) < 60;

        } catch (RuntimeException e) {
            throw new RuntimeException(
                    "Failed to check recent entry for courier: " + courier.getId(),
                    e
            );
        }

    }

    /**
     * Gets all store entries for a courier.
     */
    public ArrayList<CourierStore> getAllStoreEntriesListByCourier(int courierId){
        return courierStoreDao.getAllGeolocationListByCourier(courierId);
    }


    /**
     * Gets names of all stores visited by a courier.
     */
    public ArrayList<String> getAllStoreEntryNamesByCourierId(int courierId){
        return courierStoreDao.getAllGeolocationListByCourier(courierId)
                .stream()
                .map(CourierStore::getStoreName)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Calculates distance between two points in meters.
     * Used for checking if courier is within store radius.
     */
    public static double calculateShortDistance(double startLat, double startLong,
                                                double endLat, double endLong) {
        double dLat = Math.toRadians(endLat - startLat);
        double dLong = Math.toRadians(endLong - startLong);

        // Convert to radians once
        double lat1 = Math.toRadians(startLat);

        // Calculate x and y distances
        double x = dLong * Math.cos(lat1);
        double y = dLat;

        // Calculate distance in kilometers
        double distance = Math.sqrt(x*x + y*y) * EARTH_RADIUS;

        // Convert to meters
        return distance * 1000;
    }
}
