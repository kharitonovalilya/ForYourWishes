package com.dev.foryourwishes.wishlist;

import com.dev.foryourwishes.user.User;
import com.dev.foryourwishes.wishlist.exceptions.ReservationNotFoundException;
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
