# Courier Tracking System

A Spring Boot application for tracking courier locations and their visits to Migros stores in real time. The system monitors courier movements, calculates travel distances, and logs store visits.

## What It Does

When a courier's location is reported, the system:
- Records the courier's position and timestamp
- Checks if they are within 100 meters of any Migros store
- Logs store entries (with a 1-minute cooldown to prevent duplicate entries)
- Calculates total travel distance for each courier

## Getting Started

You'll need Java 17 and Maven 3.6 or higher installed on your system.

The application will start at http://localhost:8080/api

Testing the API
Import Postman collection from src/test/resources/postman/Courier_Tracking_API_Tests.json to test the endpoints. The collection includes example requests for all main operations.
Main endpoints:

- Record courier location: POST /v1/couriers/courier/location
- Get travel distance: GET /v1/couriers/courier/{courierId}/totaldistance
- Get store visits: GET /v1/couriers/courier/{courierId}/storeentries

Note: The system uses JSON files for storage (couriers.json and courierstores.json). You'll need to manually delete these files between test runs if you want to start with fresh data.