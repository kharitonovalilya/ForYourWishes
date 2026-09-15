package com.dev.foryourwishes.wishlist.dto;

import com.dev.foryourwishes.wishlist.WishlistStatus;

public record WishlistSummaryResponse(Long id, String title, String description, WishlistStatus status) { }