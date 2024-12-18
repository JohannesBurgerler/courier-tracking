package com.migrosonlinecase.couriertracking.controller;

import com.migrosonlinecase.couriertracking.dto.CourierDistanceResponse;
import com.migrosonlinecase.couriertracking.dto.CourierLocationRequest;
import com.migrosonlinecase.couriertracking.dto.CourierLocationResponse;
import com.migrosonlinecase.couriertracking.dto.StoreEntriesResponse;
import com.migrosonlinecase.couriertracking.model.Courier;
import com.migrosonlinecase.couriertracking.model.CourierStore;
import com.migrosonlinecase.couriertracking.service.CourierService;
import com.migrosonlinecase.couriertracking.service.CourierStoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * REST controller handling courier location tracking and related operations.
 * Provides endpoints for recording courier locations, calculating travel distances,
 * and retrieving store entry history.
 */

@RestController
@RequestMapping("/v1/couriers/courier")
public class CourierController {

    @Autowired
    private CourierService courierService;

    @Autowired
    private CourierStoreService courierStoreService;

    private static final Logger logger = LoggerFactory.getLogger(CourierController.class);

    /**
     * Records a new courier location and checks for store entries.
     * If the courier is within 100m of a store, records a store entry
     * unless they've already entered the same store within the last minute.
     *
     * @param request Contains courier ID, coordinates, and timestamp
     * @return Response indicating whether a store entry was recorded
     */
    @PostMapping("/location")
    public ResponseEntity<CourierLocationResponse> processCourierLocation(@RequestBody CourierLocationRequest request) {
        try {
            Courier courier = new Courier(
                    request.getCourierId(),
                    request.getLatitude(),
                    request.getLongitude(),
                    request.getTime()
            );

            courierService.addCourier(courier);
            String storeName = courierStoreService.storeEntrance(courier);

            return ResponseEntity.ok(new CourierLocationResponse(
                    storeName != null && !storeName.isEmpty(),
                    storeName
            ));
        } catch (Exception e) {
            logger.error("Error processing location: ", e);
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Calculates the total distance traveled by a courier.
     * Distance is calculated using the haversine formula between consecutive points.
     *
     * @param courierId ID of the courier
     * @return Total distance in kilometers
     */
    @GetMapping("/{courierId}/totaldistance")
    public ResponseEntity<CourierDistanceResponse> getTotalTravelDistance(
            @PathVariable int courierId) {
        try {
            double totalDistance = courierService.getTotalTravelDistance(courierId);
            return ResponseEntity.ok(new CourierDistanceResponse(courierId, totalDistance));
        } catch(Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Retrieves the history of store entries for a specific courier.
     * Each entry includes the store name and timestamp of when the courier entered.
     *
     * @param courierId ID of the courier
     * @return List of store entries with timestamps
     */
    @GetMapping("/{courierId}/storeentries")
    public ResponseEntity<List<StoreEntriesResponse>> getStoreEntries(
            @PathVariable int courierId) {
        try {
            List<StoreEntriesResponse> storeEntries = new ArrayList<>();
            for(CourierStore courierStore : courierStoreService.getAllStoreEntriesListByCourier(courierId)){
                storeEntries.add(new StoreEntriesResponse(courierId, courierStore.getStoreName(), courierStore.getEntranceDate()));
            }
            return ResponseEntity.ok(storeEntries);
        } catch(Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

}
