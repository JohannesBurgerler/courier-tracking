package com.migrosonlinecase.couriertracking.dao;

import com.migrosonlinecase.couriertracking.model.Courier;

import java.util.ArrayList;
import java.util.Map;

public interface IJsonCourierDao {
    void saveCouriers(Map<Integer, ArrayList<Courier>>  courierMap);
    Map<Integer, ArrayList<Courier>> loadCouriers();

}
