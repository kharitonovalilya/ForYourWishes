package com.dev.foryourwishes.wishlist;

import com.dev.foryourwishes.user.User;
import com.dev.foryourwishes.user.UserManagerService;
import com.dev.foryourwishes.wishlist.exceptions.WishlistArchivedException;
import com.dev.foryourwishes.wishlist.exceptions.WishlistNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class WishlistManagerService {

    private final WishlistRepository wishlistRepository;
    private final WishRepository wishRepository;
    private final UserManagerService userManagerService;

    public Wishlist findById(Long wishlistId) {
        return wishlistRepository.findById(wishlistId)
                .orElseThrow(() -> new WishlistNotFoundException(wishlistId));
    }

    public Wishlist createWishlist(Long ownerId, String title, String description) {
        User owner = userManagerService.findById(ownerId);
        Wishlist newWishlist = new Wishlist(title, description, owner);
        return wishlistRepository.save(newWishlist);
    }

    public Wish addWishToWishlist(Long wishlistId, String title, String description, String url) {
        Wishlist wishlist = findById(wishlistId);
        if (wishlist.getStatus() == WishlistStatus.ARCHIVED) {
            throw new WishlistArchivedException(wishlistId);
        }
        Wish wish = new Wish(title, description, url, wishlist);
        return wishRepository.save(wish);
    }

    public Wishlist editWishlist(Long wishlistId, String newTitle, String newDescription) {
        Wishlist wishlist = findById(wishlistId);
        if (wishlist.getStatus() == WishlistStatus.ARCHIVED) {
            throw new WishlistArchivedException(wishlistId);
        }
        wishlist.edit(newTitle, newDescription);
        return wishlistRepository.save(wishlist);
    }

    public void deleteWishlist(Long wishlistId) {
        wishlistRepository.deleteById(wishlistId);
    }

    public Wishlist archiveWishlist(Long wishlistId) {
        Wishlist wishlist = findById(wishlistId);
        if (wishlist.getStatus() == WishlistStatus.ARCHIVED) {
            throw new WishlistArchivedException(wishlistId);
        }
        wishlist.archive();
        return wishlistRepository.save(wishlist);
    }

    public Wishlist unarchiveWishlist(Long wishlistId) {
        Wishlist wishlist = findById(wishlistId);
        wishlist.unarchive();
        return wishlistRepository.save(wishlist);
    }

}
