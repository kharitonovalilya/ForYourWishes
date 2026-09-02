package com.dev.foryourwishes.wishlist;

import com.dev.foryourwishes.user.User;
import com.dev.foryourwishes.user.UserManagerService;
import com.dev.foryourwishes.wishlist.exceptions.*;
import jakarta.transaction.Transactional;
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
        Wishlist wishlist = wish.getWishlist();
        if (wishlist.getStatus() == WishlistStatus.ARCHIVED) {
            throw new WishlistArchivedException(wishlist.getId());
        }
        wish.editWish(newTitle, newDescription, newUrl);
        return wishRepository.save(wish);
    }

    public Wish markAsFulfilled(Long wishId) {
        Wish wish = findById(wishId);
        Wishlist wishlist = wish.getWishlist();
        if (wishlist.getStatus() == WishlistStatus.ARCHIVED) {
            throw new WishlistArchivedException(wishlist.getId());
        }
        wish.markAsFulfilled();
        return wishRepository.save(wish);
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public Reservation reserveWish(Long wishId, Long userId) {
        Wish wish = findById(wishId);
        if (wish.getStatus() == WishStatus.FULFILLED) {
            throw new WishIsFulfilledException(wishId);
        }
        if (reservationRepository.existsByWishId(wishId)) {
            throw new WishIsReservedException(wishId);
        }
        User user = userManagerService.findById(userId);
        Wishlist wishlist = wish.getWishlist();
        if (wishlist.getStatus() == WishlistStatus.ARCHIVED) {
            throw new WishlistArchivedException(wishlist.getId());
        }
        if (wishlist.getOwner().getId().equals(userId)) {
            throw new OwnWishReservationException(userId);
        }
        Reservation reservation = new Reservation(wish, user);
        return reservationRepository.save(reservation);
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void cancelReservation(Long wishId, Long userId) {
        Wish wish = findById(wishId);
        if (wish.getStatus() == WishStatus.FULFILLED) {
            throw new WishIsFulfilledException(wishId);
        }
        Wishlist wishlist = wish.getWishlist();
        if (wishlist.getStatus() == WishlistStatus.ARCHIVED) {
            throw new WishlistArchivedException(wishlist.getId());
        }
        Reservation reservation = reservationRepository.findByWishId(wishId)
                .orElseThrow(() -> new ReservationNotFoundException(wishId));
        if (!reservation.getReservedBy().getId().equals(wish.getId())) {
            throw new ReservationAccessDeniedException(reservation.getId());
        }
        reservationRepository.save(reservation);
    }

}
