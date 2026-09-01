package com.dev.foryourwishes.wishlist.exceptions;

public class ReservationIsNotFoundException extends RuntimeException {
    public ReservationIsNotFoundException(Long reservationId) {
        super("Reservation with id " + reservationId + " not found");
    }
}
