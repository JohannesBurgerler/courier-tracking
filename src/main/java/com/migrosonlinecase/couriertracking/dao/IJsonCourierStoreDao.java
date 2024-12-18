package com.migrosonlinecase.couriertracking.dao;

import com.migrosonlinecase.couriertracking.model.CourierStore;

import java.util.ArrayList;
import java.util.Map;

public interface IJsonCourierStoreDao {
    void saveCourierStores(Map<Integer, ArrayList<CourierStore>> courierStoreMap);
    Map<Integer, ArrayList<CourierStore>> loadCourierStores();

}
