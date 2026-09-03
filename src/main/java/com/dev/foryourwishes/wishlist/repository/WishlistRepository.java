package com.dev.foryourwishes.wishlist.repository;

import com.dev.foryourwishes.wishlist.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
}
