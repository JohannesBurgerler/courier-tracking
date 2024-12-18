package com.migrosonlinecase.couriertracking.dao;

import com.migrosonlinecase.couriertracking.model.Store;

import java.util.ArrayList;

public interface IJsonStoreDao {
    void loadStores();
    ArrayList<Store> getAllStores();
    Store getStoreByName(String storeName);
    boolean hasStore(String name);
}
