package com.hashedin.reservation.services.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hashedin.reservation.Dtos.RequestDtos.RestaurantTableDto;
import com.hashedin.reservation.entity.Restaurant;
import com.hashedin.reservation.entity.RestaurantTable;
import com.hashedin.reservation.repository.ReservationRespository;
import com.hashedin.reservation.repository.RestaurantRepository;
import com.hashedin.reservation.repository.RestaurantTableRepository;
import com.hashedin.reservation.services.RestaurantTableService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class RestaurantTableServiceImpl implements RestaurantTableService {

    @Autowired
    private RestaurantTableRepository restaurantTableRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private ReservationRespository reservationRespository;

    private static final Logger logger = LoggerFactory.getLogger(RestaurantTableServiceImpl.class);

    /**
     * Creates a new restaurant table.
     *
     * @param restaurantTable The restaurant table DTO containing the table details.
     * @return The created restaurant table.
     * @throws Exception If the restaurant ID is missing or the restaurant is not found.
     */
    @Override
    public RestaurantTable createRestaurantTable(RestaurantTableDto restaurantTable) throws Exception {
        logger.info("Attempting to create restaurant table: {}", restaurantTable.getTableNumber());
        RestaurantTable newTable = new RestaurantTable();
        newTable.setCapacity(restaurantTable.getCapacity());
        newTable.setTableNumber(restaurantTable.getTableNumber());
        newTable.setTableType(restaurantTable.getTableType());
        if (restaurantTable.getRestaurantId() == null) {
            logger.error("Restaurant Id is required for creating table: {}", restaurantTable.getTableNumber());
            throw new Exception("Restaurant Id is required");
        }
        Optional<Restaurant> restaurant = restaurantRepository.findById(restaurantTable.getRestaurantId());
        if (restaurant.isEmpty()) {
            logger.error("Restaurant not found with id: {}", restaurantTable.getRestaurantId());
            throw new Exception("Restaurant not found");
        }
        newTable.setRestaurant(restaurant.get());
        RestaurantTable savedTable = restaurantTableRepository.save(newTable);
        logger.info("Restaurant table created successfully: {}", savedTable.getTableNumber());
        return savedTable;
    }

    /**
     * Retrieves a restaurant table by its ID.
     *
     * @param id The ID of the restaurant table.
     * @return The restaurant table with the specified ID, or null if not found.
     */
    @Override
    public RestaurantTable getRestaurantTableById(Long id) {
        logger.info("Fetching restaurant table by id: {}", id);
        Optional<RestaurantTable> table = restaurantTableRepository.findById(id);
        if (table.isPresent()) {
            logger.info("Restaurant table found: {}", id);
            return table.get();
        } else {
            logger.warn("Restaurant table not found: {}", id);
            return null;
        }
    }

    /**
     * Retrieves all restaurant tables.
     *
     * @return A list of all restaurant tables.
     */
    @Override
    public List<RestaurantTable> getAllRestaurantTables() {
        logger.info("Fetching all restaurant tables");
        List<RestaurantTable> tables = restaurantTableRepository.findAll();
        logger.info("Fetched {} restaurant tables", tables.size());
        return tables;
    }

    /**
     * Updates a restaurant table.
     *
     * @param id           The ID of the restaurant table to update.
     * @param updatedTable The updated restaurant table.
     * @return The updated restaurant table.
     * @throws Exception If the table has bookings or if the table is not found.
     */
    @Override
    public RestaurantTable updateRestaurantTable(Long id, RestaurantTable updatedTable) throws Exception {
        logger.info("Updating restaurant table: {}", id);
        if (reservationRespository.findByTableId(id).size() > 0) {
            logger.error("Table has bookings, cannot update: {}", id);
            throw new Exception("Table has bookings, cannot update");
        }

        Optional<RestaurantTable> restaurantTableData = restaurantTableRepository.findById(id);
        if (restaurantTableData.isPresent()) {
            RestaurantTable _restaurantTable = restaurantTableData.get();
            _restaurantTable.setCapacity(updatedTable.getCapacity());
            _restaurantTable.setTableNumber(updatedTable.getTableNumber());
            RestaurantTable savedTable = restaurantTableRepository.save(_restaurantTable);
            logger.info("Restaurant table updated successfully: {}", id);
            return savedTable;
        }
        logger.error("Restaurant table not found: {}", id);
        throw new Exception("Table not found");
    }

    /**
     * Deletes a restaurant table.
     *
     * @param id The ID of the restaurant table to delete.
     * @throws Exception If the table has bookings or if the table is not found.
     */
    @Override
    public void deleteRestaurantTable(Long id) throws Exception {
        logger.info("Deleting restaurant table: {}", id);
        if (reservationRespository.findByTableId(id).size() > 0) {
            logger.error("Table has bookings, cannot delete: {}", id);
            throw new Exception("Table has bookings, cannot delete");
        }
        restaurantTableRepository.deleteById(id);
        logger.info("Restaurant table deleted successfully: {}", id);
    }

    /**
     * Retrieves all tables for a given restaurant ID.
     *
     * @param id The ID of the restaurant.
     * @return A list of all tables for the specified restaurant ID.
     */
    public List<RestaurantTable> getTablesByRestaurantId(Long id) {
        logger.info("Fetching tables for restaurant id: {}", id);
        List<RestaurantTable> restaurantTables = restaurantTableRepository.findByRestaurantId(id);
        logger.info("Fetched {} tables for restaurant id: {}", restaurantTables.size(), id);
        return restaurantTables;
    }
}
