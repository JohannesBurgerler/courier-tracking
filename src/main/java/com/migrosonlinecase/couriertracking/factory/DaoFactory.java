package com.migrosonlinecase.couriertracking.factory;

import com.migrosonlinecase.couriertracking.dao.IJsonCourierDao;
import com.migrosonlinecase.couriertracking.dao.IJsonCourierStoreDao;

/**
 * Factory interface for creating data access objects.
 * Handles creation of JSON-based storage implementations.
 */
public interface DaoFactory {
    IJsonCourierDao createCourierDao();
    IJsonCourierStoreDao createCourierStoreDao();
}
