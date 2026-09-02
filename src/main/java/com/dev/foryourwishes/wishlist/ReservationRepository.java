package com.dev.foryourwishes.wishlist;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Optional<Reservation> findByWishId(Long wishId);
    boolean existsByWishId(Long wishId);
    void deleteAllByWishWishlistId(Long wishWishListId);
    void deleteByWishId(Long wishId);
}
