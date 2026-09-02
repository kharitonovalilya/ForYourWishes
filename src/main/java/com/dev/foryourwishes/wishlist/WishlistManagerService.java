package com.dev.foryourwishes.wishlist;

import com.dev.foryourwishes.user.User;
import com.dev.foryourwishes.user.UserManagerService;
import com.dev.foryourwishes.wishlist.exceptions.WishlistArchivedException;
import com.dev.foryourwishes.wishlist.exceptions.WishlistNotFoundException;

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
    public Wishlist editWishlist(Long wishlistId, String newTitle, String newDescription) {
        Wishlist wishlist = findById(wishlistId);
        if (wishlist.getStatus() == WishlistStatus.ARCHIVED) {
            throw new WishlistArchivedException(wishlistId);
        }
        wishlist.edit(newTitle, newDescription);
        return wishlistRepository.save(wishlist);
    }

    @Transactional
    public void deleteWishlist(Long wishlistId) {
        wishlistRepository.deleteById(wishlistId);
    }

    // TODO test how hibernate works on this
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
    public Wishlist unarchiveWishlist(Long wishlistId) {
        Wishlist wishlist = findById(wishlistId);
        wishlist.unarchive();
        return wishlistRepository.save(wishlist);
    }

}
