package com.dev.foryourwishes.wishlist.repository;

import com.dev.foryourwishes.wishlist.entity.Wish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishRepository extends JpaRepository<Wish, Long> {
    List<Wish> findAllByWishlistId(Long wishlistId);
    void deleteAllByWishlistId(Long wishlistId);
}
