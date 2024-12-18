package com.migrosonlinecase.couriertracking.dao;

import com.migrosonlinecase.couriertracking.exception.business.CourierNotFoundException;
import com.migrosonlinecase.couriertracking.exception.technical.DataAccessException;
import com.migrosonlinecase.couriertracking.factory.DaoFactory;
import com.migrosonlinecase.couriertracking.model.Courier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Primary data access object for courier location tracking.
 * Manages both in-memory and persistent storage of courier locations.
 */
@Component
public class CourierDao {
    private static final Map<Integer, ArrayList<Courier>> courierGeolocationMap = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(CourierDao.class);

    private final IJsonCourierDao jsonCourierDao;

    @Autowired
    public CourierDao(DaoFactory daoFactory) {
        this.jsonCourierDao = daoFactory.createCourierDao();
    }

    /**
     * Adds a new courier location and persists to storage.
     * Maintains location history for each courier.
     */
    public void addCourierGeolocation(Courier courier){
        try {

            courierGeolocationMap.computeIfAbsent(courier.getId(), k -> new ArrayList<>())
                        .add(courier);

            logger.debug("Added geolocation for courier ID: {}", courier.getId());

            jsonCourierDao.saveCouriers(courierGeolocationMap);

        } catch (Exception e) {
            throw new DataAccessException("add", "courier geolocation", e);
        }
    }

    /**
     * Retrieves location history for a specific courier.
     * @throws CourierNotFoundException if courier doesn't exist
     */
    public ArrayList<Courier> getAllGeolocationListByCourier(int courierId) {
        return Optional.ofNullable(courierGeolocationMap.get(courierId))
                .orElseThrow(() -> new CourierNotFoundException(courierId));
    }

    /**
     * Loads all courier data from persistent storage.
     */
    public Map<Integer, ArrayList<Courier>> getAllCouriersFromJson(){
        return Optional.ofNullable(jsonCourierDao.loadCouriers()).orElseThrow(()-> new DataAccessException("load","courierMap from json file", null));
    }

}
