package com.driver.services.impl;

import com.driver.model.ParkingLot;
import com.driver.model.Spot;
import com.driver.model.SpotType;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.SpotRepository;
import com.driver.services.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ParkingLotServiceImpl implements ParkingLotService {
    @Autowired
    ParkingLotRepository parkingLotRepository1;
    @Autowired
    SpotRepository spotRepository1;
    @Override
    public ParkingLot addParkingLot(String name, String address) {
        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setName(name);
        parkingLot.setAddress(address);
        parkingLot.setSpotList(new ArrayList<>());
        ParkingLot p = parkingLotRepository1.save(parkingLot);
        return p;

    }

    @Override
    public Spot addSpot(int parkingLotId, Integer numberOfWheels, Integer pricePerHour) {
        Spot spot = new Spot();
       ParkingLot parkingLot = parkingLotRepository1.findById(parkingLotId).get();
        spot.setParkingLot(parkingLot);
        if (numberOfWheels==2){
            spot.setSpotType(SpotType.TWO_WHEELER);
        }else if (numberOfWheels==4){
            spot.setSpotType(SpotType.FOUR_WHEELER);
        }else{
            spot.setSpotType(SpotType.OTHERS);
        }
        spot.setPricePerHour(pricePerHour);
        spot.setOccupied(false);
        spotRepository1.save(spot);
        return spot;
    }

    @Override
    public void deleteSpot(int spotId) {
        spotRepository1.deleteById(spotId);
    }

    @Override
    public Spot updateSpot(int parkingLotId, int spotId, int pricePerHour) {
        ParkingLot parkingLot = parkingLotRepository1.findById(parkingLotId).get();
        List<Spot> spotList = parkingLot.getSpotList();
        Spot spot_to_be_updated  = null;
        for (Spot spot:spotList){
            if (spot.getId()==spotId){
                spot_to_be_updated = spot;
            }
        }
        spot_to_be_updated.setPricePerHour(pricePerHour);
        spotRepository1.save(spot_to_be_updated);
        return spot_to_be_updated;
    }

    @Override
    public void deleteParkingLot(int parkingLotId) {
        parkingLotRepository1.deleteById(parkingLotId);
    }
}
