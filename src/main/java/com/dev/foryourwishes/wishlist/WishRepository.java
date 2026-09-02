package com.dev.foryourwishes.wishlist;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishRepository extends JpaRepository<Wish, Long> {
    List<Wish> findAllByWishlistId(Long wishlistId);
}
