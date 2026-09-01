package com.dev.foryourwishes.wishlist;

import com.dev.foryourwishes.user.User;
import com.dev.foryourwishes.wishlist.exceptions.ReservationIsNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ReservationManagerService {

    private final ReservationRepository reservationRepository;

    public Reservation createReservation(Wish wish, User reservedBy) {
        Reservation reservation = new Reservation(wish, reservedBy);
        return reservationRepository.save(reservation);
    }

    public Reservation findById(Long reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationIsNotFoundException(reservationId));
    }

}
