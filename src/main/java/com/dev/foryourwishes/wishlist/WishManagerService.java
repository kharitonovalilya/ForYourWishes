package com.dev.foryourwishes.wishlist;

import com.dev.foryourwishes.user.User;
import com.dev.foryourwishes.user.UserManagerService;
import com.dev.foryourwishes.wishlist.exceptions.WishIsFulfilledException;
import com.dev.foryourwishes.wishlist.exceptions.WishNotFoundException;
import com.dev.foryourwishes.wishlist.exceptions.WishlistArchivedException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class WishManagerService {

    private final WishRepository wishRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationManagerService reservationManagerService;
    private final UserManagerService userManagerService;

    public Wish findById(Long wishId) {
        return wishRepository.findById(wishId)
                .orElseThrow(() -> new WishNotFoundException(wishId));
    }

    public void deleteWish(Long wishId) {
        wishRepository.deleteById(wishId);
    }

    public Wish editWish(Long wishId, String newTitle, String newDescription, String newUrl) {
        Wish wish = findById(wishId);
        wish.editWish(newTitle, newDescription, newUrl);
        return wishRepository.save(wish);
    }

    public Wish markAsFulfilled(Long wishId) {
        Wish wish = findById(wishId);
        wish.markAsFulfilled();
        return wishRepository.save(wish);
    }

    public Reservation reserveWish(Long wishId, Long userId) {
        Wish wish = findById(wishId);
        if (wish.getStatus() == WishStatus.RESERVED) {
            throw new WishIsFulfilledException(wishId);
        }
        if (wish.getStatus() == WishStatus.FULFILLED) {
            throw new WishIsFulfilledException(wishId);
        }
        User user = userManagerService.findById(userId);
        Wishlist wishlist = wish.getWishlist();
        if (wishlist.getStatus() == WishlistStatus.ARCHIVED) {
            throw new WishlistArchivedException(wishlist.getId());
        }
        User owner = wishlist.getOwner();
        if (owner.getId().equals(userId)) {
            throw new IllegalArgumentException("User id = %d cannot reserve own wish".formatted(userId));
        }
        Reservation reservation = new Reservation(wish, user);
        wish.markAsReserved();
        wishRepository.save(wish);
        return reservationRepository.save(reservation);
    }

    public void cancelReservation(Long wishId) {
        Wish wish = findById(wishId);
        if (wish.getStatus() == WishStatus.FULFILLED) {
            throw new WishIsFulfilledException(wishId);
        }
        Wishlist wishlist = wish.getWishlist();
        if (wishlist.getStatus() == WishlistStatus.ARCHIVED) {
            throw new WishlistArchivedException(wishlist.getId());
        }
    }

}
