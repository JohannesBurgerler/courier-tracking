package com.migrosonlinecase.couriertracking.service;

import com.migrosonlinecase.couriertracking.dao.CourierDao;
import com.migrosonlinecase.couriertracking.exception.business.GeographicValidationException;
import com.migrosonlinecase.couriertracking.model.Courier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
/**
 * Handles courier location tracking and distance calculations.
 */
@Service
public class CourierService {
    @Autowired
    private CourierDao courierDao;
    public static final double EARTH_RADIUS = 6371.0;

    /**
     * Adds a new courier location to tracking.
     */
    public void addCourier(Courier courier){
        validateCourierData(courier);
        try {

            // Add courier to the in-memory map
            courierDao.addCourierGeolocation(courier);


        } catch (Exception e) {
            throw new RuntimeException("Failed to add courier", e);
        }
    }

    /**
     * Calculates total distance traveled by a courier in kilometers.
     */
    public double getTotalTravelDistance(int courierId){
        double totalTravelDistance = 0.0;
        ArrayList<Courier> geoLocationList = courierDao.getAllGeolocationListByCourier(courierId);
        for(int i = 1; i < geoLocationList.size(); i++){
            totalTravelDistance += calculateDistance(geoLocationList.get(i-1).getLat(), geoLocationList.get(i-1).getLng(),
                                                     geoLocationList.get(i).getLat(), geoLocationList.get(i).getLng());
        }

        return totalTravelDistance;
    }

    /**
     * Calculates distance between two points using a simplified version
     * of the haversine formula.
     */
    double calculateDistance(double startLat, double startLong, double endLat, double endLong) {


            double lat1Rad = Math.toRadians(startLat);
            double lat2Rad = Math.toRadians(endLat);
            double lon1Rad = Math.toRadians(startLong);
            double lon2Rad = Math.toRadians(endLong);

            double x = (lon2Rad - lon1Rad) * Math.cos((lat1Rad + lat2Rad) / 2);
            double y = (lat2Rad - lat1Rad);

            return Math.sqrt(x * x + y * y) * EARTH_RADIUS;

    }

    private void validateCourierData(Courier courier) {
        if (courier == null) {
            throw new IllegalArgumentException("Courier cannot be null");
        }

        // Validate coordinates
        if (courier.getLat() < -90 || courier.getLat() > 90 ||
                courier.getLng() < -180 || courier.getLng() > 180) {
            throw new GeographicValidationException(
                    String.format("Invalid coordinates: lat=%f, lng=%f",  courier.getLat(), courier.getLng())
            );
        }
    }
}
