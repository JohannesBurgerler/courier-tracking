package com.migrosonlinecase.couriertracking.factory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.migrosonlinecase.couriertracking.dao.IJsonCourierDao;
import com.migrosonlinecase.couriertracking.dao.IJsonCourierStoreDao;
import com.migrosonlinecase.couriertracking.dao.impl.JsonCourierDao;
import com.migrosonlinecase.couriertracking.dao.impl.JsonCourierStoreDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Creates JSON data access objects for storing courier and store data.
 */
@Component
public class JsonDaoFactory implements DaoFactory {
    private final ObjectMapper objectMapper;

    @Autowired
    public JsonDaoFactory(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public IJsonCourierDao createCourierDao() {
        return new JsonCourierDao(objectMapper);
    }

    @Override
    public IJsonCourierStoreDao createCourierStoreDao() {
        return new JsonCourierStoreDao(objectMapper);
    }
}
