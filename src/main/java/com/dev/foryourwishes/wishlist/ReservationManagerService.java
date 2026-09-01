package com.dev.foryourwishes.wishlist;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;

}
