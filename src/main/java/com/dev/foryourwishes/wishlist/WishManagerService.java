package com.dev.foryourwishes.wishlist;

import com.dev.foryourwishes.wishlist.exceptions.WishNotFound;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class WishManagerService {

    private final WishRepository wishRepository;

    public Wish findById(Long wishId) {
        return wishRepository.findById(wishId)
                .orElseThrow(() -> new WishNotFound(wishId));
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

}
