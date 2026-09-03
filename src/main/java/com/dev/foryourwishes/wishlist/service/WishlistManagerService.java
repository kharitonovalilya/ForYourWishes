package com.dev.foryourwishes.wishlist.service;

import com.dev.foryourwishes.user.User;
import com.dev.foryourwishes.user.UserManagerService;
import com.dev.foryourwishes.wishlist.WishlistStatus;
import com.dev.foryourwishes.wishlist.entity.Wish;
import com.dev.foryourwishes.wishlist.entity.Wishlist;
import com.dev.foryourwishes.wishlist.exception.WishlistArchivedException;
import com.dev.foryourwishes.wishlist.exception.WishlistNotFoundException;

import com.dev.foryourwishes.wishlist.repository.ReservationRepository;
import com.dev.foryourwishes.wishlist.repository.WishRepository;
import com.dev.foryourwishes.wishlist.repository.WishlistRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class WishlistManagerService {

    private final WishlistRepository wishlistRepository;
    private final WishRepository wishRepository;
    private final UserManagerService userManagerService;
    private final ReservationRepository reservationRepository;

    public Wishlist findById(Long wishlistId) {
        return wishlistRepository.findById(wishlistId)
                .orElseThrow(() -> new WishlistNotFoundException(wishlistId));
    }

    @Transactional
    public Wishlist createWishlist(Long ownerId, String title, String description) {
        User owner = userManagerService.findById(ownerId);
        Wishlist newWishlist = new Wishlist(title, description, owner);
        return wishlistRepository.save(newWishlist);
    }

    @Transactional
    public Wish addWishToWishlist(Long wishlistId, String title, String description, String url) {
        Wishlist wishlist = findById(wishlistId);
        if (wishlist.getStatus() == WishlistStatus.ARCHIVED) {
            throw new WishlistArchivedException(wishlistId);
        }
        Wish wish = new Wish(title, description, url, wishlist);
        return wishRepository.save(wish);
    }

    @Transactional
    public void editWishlist(Long wishlistId, String newTitle, String newDescription) {
        Wishlist wishlist = findById(wishlistId);
        if (wishlist.getStatus() == WishlistStatus.ARCHIVED) {
            throw new WishlistArchivedException(wishlistId);
        }
        wishlist.edit(newTitle, newDescription);
    }

    @Transactional
    public void deleteWishlist(Long wishlistId) {
        reservationRepository.deleteAllByWishWishlistId(wishlistId);
        wishlistRepository.deleteById(wishlistId);
    }

    @Transactional
    public void archiveWishlist(Long wishlistId) {
        Wishlist wishlist = findById(wishlistId);
        if (wishlist.getStatus() == WishlistStatus.ARCHIVED) {
            throw new WishlistArchivedException(wishlistId);
        }
        wishlist.archive();
        reservationRepository.deleteAllByWishWishlistId(wishlistId);
    }

    @Transactional
    public void unarchiveWishlist(Long wishlistId) {
        Wishlist wishlist = findById(wishlistId);
        wishlist.unarchive();
    }

}
