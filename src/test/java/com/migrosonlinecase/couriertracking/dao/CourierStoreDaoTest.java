package com.migrosonlinecase.couriertracking.dao;

import com.migrosonlinecase.couriertracking.exception.business.CourierNotFoundException;
import com.migrosonlinecase.couriertracking.factory.DaoFactory;
import com.migrosonlinecase.couriertracking.model.CourierStore;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CourierStoreDaoTest {
    @Autowired
    private DaoFactory daoFactory;

    private CourierStoreDao courierStoreDao;
    private CourierStore testCourierStore;
    private LocalDateTime testTime;


    @BeforeEach
    void setUp() {
        courierStoreDao = new CourierStoreDao(daoFactory);
        testTime = LocalDateTime.now();
        testCourierStore = new CourierStore(1, "Test Store", testTime);
    }


    // Tests basic store entry creation
    @Test
    @DisplayName("Should successfully add new courier store")
    void addCourierStore_NewEntry_Success() {
        courierStoreDao.addCourierStore(testCourierStore);

        ArrayList<CourierStore> stores = courierStoreDao.getAllGeolocationListByCourier(1);
        assertEquals(1, stores.size());
        assertEquals("Test Store", stores.get(0).getStoreName());
    }

    // Tests null validation for store entries
    @Test
    @DisplayName("Should throw exception when adding null")
    void addCourierStore_NullEntry_ThrowsException() {

        assertThrows(IllegalArgumentException.class, () ->
                courierStoreDao.addCourierStore(null)
        );
    }

    @Nested
    @DisplayName("Get Locations Tests")
    class GetLocationsTests {

        @BeforeEach
        void setupLocations() {
            courierStoreDao.addCourierStore(testCourierStore);
            courierStoreDao.addCourierStore(
                    new CourierStore(1, "Another Store", testTime.plusMinutes(5))
            );
        }

        // Tests retrieval of all store entries for a courier
        @Test
        @DisplayName("Should get all locations for courier")
        void getAllGeolocationListByCourier_ExistingCourier_ReturnsAll() {
            ArrayList<CourierStore> locations = courierStoreDao.getAllGeolocationListByCourier(1);

            assertEquals(5, locations.size());
        }

        @Test
        @DisplayName("Should throw exception for non-existent courier")
        void getAllGeolocationListByCourier_NonExistentCourier_ThrowsException() {
            assertThrows(CourierNotFoundException.class, () ->
                    courierStoreDao.getAllGeolocationListByCourier(999)
            );
        }

        @Test
        @DisplayName("Should get last location for courier")
        void getLastGeoLocationByCourierId_ExistingCourier_ReturnsLatest() {
            CourierStore lastLocation = courierStoreDao.getLastGeoLocationByCourierId(1);

            assertNotNull(lastLocation);
            assertEquals("Another Store", lastLocation.getStoreName());
        }
    }
}


