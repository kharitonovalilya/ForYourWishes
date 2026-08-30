package com.dev.foryourwishes.wishlist;

import com.dev.foryourwishes.user.UserManagerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class WishlistManagerService {

    private final WishlistRepository wishlistRepository;
    private final UserManagerService userManagerService;

    public Wishlist createWishlistForUser(Long ownerId, String title, String description) {
        
    }

}
