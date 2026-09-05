package com.dev.foryourwishes.wishlist.controller;

import com.dev.foryourwishes.security.AuthUser;
import com.dev.foryourwishes.wishlist.dto.CreateWishlistRequest;
import com.dev.foryourwishes.wishlist.dto.EditWishlistRequest;
import com.dev.foryourwishes.wishlist.dto.WishlistResponse;
import com.dev.foryourwishes.wishlist.entity.Wishlist;
import com.dev.foryourwishes.wishlist.repository.WishlistRepository;
import com.dev.foryourwishes.wishlist.service.WishlistManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/wishlists")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistManagerService wishlistManagerService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public WishlistResponse createWishlist
            (@RequestBody CreateWishlistRequest request,
             @AuthenticationPrincipal AuthUser currentUser) {
        Wishlist wishlist = wishlistManagerService.createWishlist(
                currentUser.getId(),
                request.title(),
                request.description()
        );
        return new WishlistResponse(wishlist.getId(), wishlist.getTitle(), wishlist.getDescription());
    }

    @PatchMapping("/edit/{wishlistId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void editWishlist(
            @RequestBody EditWishlistRequest request,
            @AuthenticationPrincipal AuthUser currentUser,
            @PathVariable Long wishlistId
            ){
        wishlistManagerService.editWishlist(
                wishlistId,
                request.newTitle(),
                request.newDescription(),
                currentUser.getId()
        );
    }

}
