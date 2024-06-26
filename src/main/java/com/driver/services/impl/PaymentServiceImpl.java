package com.driver.services.impl;

import com.driver.Exceptions.InSufficientBalanceException;
import com.driver.Exceptions.PaymentModeException;
import com.driver.model.Payment;
import com.driver.model.PaymentMode;
import com.driver.model.Reservation;
import com.driver.model.Spot;
import com.driver.repository.PaymentRepository;
import com.driver.repository.ReservationRepository;
import com.driver.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    ReservationRepository reservationRepository2;
    @Autowired
    PaymentRepository paymentRepository2;

    @Override
    public Payment pay(Integer reservationId, int amountSent, String mode) throws Exception {
        Reservation reservation=reservationRepository2.findById(reservationId).get();

        //calculate bill = NumberOfHours * PricePerHours
        Spot spot=reservation.getSpot();
        int bill=reservation.getNumberOfHours()*spot.getPricePerHour();

        //check if amountSent is less than bill or not
        if(amountSent<bill)
        {
            throw new Exception("Insufficient Amount");
        }

        if(mode.equalsIgnoreCase("cash") || mode.equalsIgnoreCase("card") || mode.equalsIgnoreCase("upi"))
        {
            Payment payment=new Payment();


            if(mode.equalsIgnoreCase("cash"))
            {
                payment.setPaymentMode(PaymentMode.CASH);
            }
            else if(mode.equalsIgnoreCase("card"))
            {
                payment.setPaymentMode(PaymentMode.CARD);
            }
            else
            {
                payment.setPaymentMode(PaymentMode.UPI);
            }

            //mark payment as completed
            payment.setPaymentCompleted(true);

            //add payment in reservation
            // as one to one mapping
            payment.setReservation(reservation);
            reservation.setPayment(payment);
            //save then payment into db
            reservationRepository2.save(reservation);

            return payment;

        }
        else
        {
            throw new Exception("Payment mode not detected");
        }

    }
}
