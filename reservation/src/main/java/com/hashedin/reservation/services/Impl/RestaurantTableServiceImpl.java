package com.hashedin.reservation.services.Impl;

import java.util.List;
import java.util.Optional;

import javax.swing.text.html.Option;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hashedin.reservation.Dtos.RequestDtos.RestaurantTableDto;
import com.hashedin.reservation.entity.Restaurant;
import com.hashedin.reservation.entity.RestaurantTable;
import com.hashedin.reservation.repository.ReservationRespository;
import com.hashedin.reservation.repository.RestaurantRepository;
import com.hashedin.reservation.repository.RestaurantTableRepository;
import com.hashedin.reservation.services.RestaurantTableService;
@Service
public class RestaurantTableServiceImpl implements RestaurantTableService {

    @Autowired
    private RestaurantTableRepository restaurantTableRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private ReservationRespository reservationRespository;

    @Override
    public RestaurantTable createRestaurantTable(RestaurantTableDto restaurantTable) throws Exception {
        RestaurantTable newTable = new RestaurantTable();
        newTable.setCapacity(restaurantTable.getCapacity());
        newTable.setTableNumber(restaurantTable.getTableNumber());
        newTable.setTableType(restaurantTable.getTableType());
        if (restaurantTable.getRestaurantId() == null) {
            throw new Exception("Restaurant Id is required");
        }
        Optional<Restaurant> restaurant = restaurantRepository.findById(restaurantTable.getRestaurantId());
        newTable.setRestaurant(restaurant.get());
        return restaurantTableRepository.save(newTable);
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

        if (reservationRespository.findByTableId(id).size() > 0) {
            throw new Exception("Table has bookings, cannot update");
        }
        
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
    public void deleteRestaurantTable(Long id) throws Exception {
        if (reservationRespository.findByTableId(id).size() > 0) {
            throw new Exception("Table has bookings, cannot update");
        }
        restaurantTableRepository.deleteById(id);
    }

    public List<RestaurantTable> getTablesByRestaurantId(Long id) {
        //  Getting tables by restaurant
        List<RestaurantTable> restaurantTables = restaurantTableRepository.findByRestaurantId(id);

        return restaurantTables;
    }

}
