package com.dev.foryourwishes.wishlist.service;

import com.dev.foryourwishes.wishlist.entity.Reservation;
import com.dev.foryourwishes.wishlist.exception.ReservationNotFoundException;
import com.dev.foryourwishes.wishlist.repository.ReservationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ReservationManagerService {

    private final ReservationRepository reservationRepository;

    public Reservation findById(Long reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException(reservationId));
    }

}
