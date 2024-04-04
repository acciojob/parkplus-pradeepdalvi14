package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;
    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {

        Reservation reservation = new Reservation();

        Optional<ParkingLot> parkingLotOptional = parkingLotRepository3.findById(parkingLotId);
        Spot spotToBeReserved = null;
        int price = Integer.MAX_VALUE;
        if (parkingLotOptional.isPresent()){

            List<Spot> spotList = parkingLotOptional.get().getSpotList();
            for (Spot spot:spotList){
                if(numberOfWheels ==4 && (spot.getSpotType()==SpotType.FOUR_WHEELER || spot.getSpotType()==SpotType.OTHERS)  && price<spot.getPricePerHour() && spot.getOccupied()==false){
                    price = spot.getPricePerHour();
                    spotToBeReserved = spot;
                }else if (numberOfWheels==2 &&(spot.getSpotType()==SpotType.FOUR_WHEELER || spot.getSpotType()==SpotType.TWO_WHEELER || spot.getSpotType()==SpotType.OTHERS) && spot.getPricePerHour()<price && spot.getOccupied()==false){
                    spotToBeReserved = spot;
                    price = spot.getPricePerHour();
                }else{
                    if(spot.getSpotType()==SpotType.OTHERS && price<spot.getPricePerHour()){
                        price = spot.getPricePerHour();
                        spotToBeReserved = spot;
                    }
                }
            }
        }else {
            throw new Exception("Cannot make reservation");
        }
        User u = null;
        Optional<User> userOptional = userRepository3.findById(userId);
        if (userOptional.isPresent()){
           u = userOptional.get();
        }else {
            throw new Exception("Cannot make reservation");
        }
        if(spotToBeReserved==null){
            throw new Exception("Cannot make reservation");
        }
        spotToBeReserved.setOccupied(true);
        spotRepository3.save(spotToBeReserved);
        reservation.setNumberOfHours(timeInHours);
        reservation.setSpot(spotToBeReserved);
        reservation.setUser(u);
        return reservationRepository3.save(reservation);

    }
}
