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

## Future Improvements

While this implementation demonstrates the core courier tracking functionality. Due to time constraints and no tech requirements system is using JSON files for storage. In a production environment, several enhancements would make the system more robust and scalable:
- A better database solution like PostgreSQL or MongoDB would provide better data management capabalities.
- Adding authentication and authorization would enhance security and ensure that only authorized personnel can access courier tracking info. This could be implemented using Spring Security with JWT tokens.
- Implementing a caching layer for frequently accessed data such as store locations would reduce database loads and improve response times. This could be achieved using Redis.
- MongoDB could be used as push operator is atomic. Which would indeed make sure if two location updates came at the same time, it guarantees they both will be recorded without conflicts.
- Also for store entries, MongoDB's findAndModify operation could be used to handleone-minute rule.
- We could also add pagination for endpoints returning lists.
- We could add batch processing operations to handle multiple couriers. Using courier-specific locks and concurrenthashmaps would be good ways to handle multiple couriers.
- Using MongoDB would automatically eliminate such needs as concurrency is handled at database level.
- A better global exception handler class could be written along with a better structured custom error responses.
- Input validation could be moved to DTO and controller level from service level.
