package com.hashedin.reservation.services.Impl;

import java.util.List;
import java.util.Optional;

import javax.swing.text.html.Option;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hashedin.reservation.entity.RestaurantTable;
import com.hashedin.reservation.repository.RestaurantTableRepository;
import com.hashedin.reservation.services.RestaurantTableService;
@Service
public class RestaurantTableServiceImpl implements RestaurantTableService {

    @Autowired
    private RestaurantTableRepository restaurantTableRepository;

    @Override
    public RestaurantTable createRestaurantTable(RestaurantTable restaurantTable) {
        return restaurantTableRepository.save(restaurantTable);
    }

    @Override
    public RestaurantTable getRestaurantTableById(Long id) {
        // return restaurantTableRepository.find(id);
        return null;
    }

    @Override
    public List<RestaurantTable> getAllRestaurantTables() {
        return restaurantTableRepository.findAll();
    }

    @Override
    public RestaurantTable updateRestaurantTable(Long id, RestaurantTable updatedTable) throws Exception {
        Optional<RestaurantTable> restaurantTableData = restaurantTableRepository.findById(id);
        if (restaurantTableData.isPresent()) {
            RestaurantTable _restaurantTable = restaurantTableData.get();
            _restaurantTable.setCapacity(updatedTable.getCapacity());
            _restaurantTable.setTableNumber(updatedTable.getTableNumber());
            return restaurantTableRepository.save(_restaurantTable);
        }
        throw new Exception("Table Not found");
    }

    @Override
    public void deleteRestaurantTable(Long id) {
        // Need to check if there is any bookings for that table
        // If there is then raise an error
        restaurantTableRepository.deleteById(id);
    }

    public List<RestaurantTable> getTablesByRestaurantId(Long id) {
        //  Getting tables by restaurant
        List<RestaurantTable> restaurantTables = restaurantTableRepository.findByRestaurantId(id);

        return restaurantTables;
    }

}
