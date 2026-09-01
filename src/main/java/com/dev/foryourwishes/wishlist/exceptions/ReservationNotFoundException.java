package com.dev.foryourwishes.wishlist.exceptions;

public class ReservationNotFoundException extends RuntimeException {
    public ReservationNotFoundException(Long reservationId) {
        super("Reservation with id " + reservationId + " not found");
    }
}
