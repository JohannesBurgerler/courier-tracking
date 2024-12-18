package com.migrosonlinecase.couriertracking.service;

import com.migrosonlinecase.couriertracking.dao.CourierDao;
import com.migrosonlinecase.couriertracking.exception.business.CourierNotFoundException;
import com.migrosonlinecase.couriertracking.model.Courier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourierServiceTest {
    @Mock
    private CourierDao courierDao;

    @InjectMocks
    private CourierService courierService;

    private Courier testCourier;
    private LocalDateTime testTime;

    @BeforeEach
    void setUp() {
        testTime = LocalDateTime.now();
        testCourier = new Courier(1, 40.9923307, 29.1244229, testTime);
    }

    // Tests if distance calculation is accurate for known coordinates
    @Test
    @DisplayName("Should calculate correct distance between known points")
    void calculateDistance_ShouldReturnCorrectDistance() {
        double distance = courierService.calculateDistance(
                40.9923307, 29.1244229,
                40.986106, 29.1161293
        );
        assertEquals(1, distance, 0.1);
    }

    // Verifies that distance between same point is zero
    @Test
    @DisplayName("Should return zero for same point")
    void calculateDistance_SamePoint_ReturnsZero() {
        double distance = courierService.calculateDistance(
                40.9923307, 29.1244229,
                40.9923307, 29.1244229
        );

        assertEquals(0.0, distance, 0.001);
    }

    // Tests null check validation
    @Test
    @DisplayName("Should throw exception when courier is null")
    void addCourier_WhenNullCourier_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> courierService.addCourier(null));
    }

    @Test
    @DisplayName("Should successfully add valid courier")
    void addCourier_ValidCourier_Success() {
        courierService.addCourier(testCourier);

        verify(courierDao, times(1)).addCourierGeolocation(testCourier);
    }

    // Tests the total distance calculation for multiple points
    @Test
    @DisplayName("Should calculate correct total distance")
    void getTotalTravelDistance_ValidPath_ReturnsCorrectDistance() {
        ArrayList<Courier> locations = new ArrayList<>();
        locations.add(new Courier(1, 40.9923307, 29.1244229, testTime));
        locations.add(new Courier(1, 40.986106, 29.1161293, testTime.plusMinutes(5)));

        when(courierDao.getAllGeolocationListByCourier(1)).thenReturn(locations);

        double distance = courierService.getTotalTravelDistance(1);

        assertTrue(distance > 0);
        assertEquals(1, distance, 0.1);
    }

    @Test
    @DisplayName("Should throw exception for non-existent courier")
    void getTotalTravelDistance_NonExistentCourier_ThrowsException() {
        when(courierDao.getAllGeolocationListByCourier(anyInt()))
                .thenThrow(new CourierNotFoundException(999));

        assertThrows(CourierNotFoundException.class, () ->
                courierService.getTotalTravelDistance(999)
        );
    }
}
