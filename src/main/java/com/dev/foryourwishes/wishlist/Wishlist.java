package com.dev.foryourwishes.wishlist;

import com.dev.foryourwishes.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "wishlists")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Wishlist {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, length = 50)
    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WishlistStatus status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    public void archive() {
        this.status = WishlistStatus.ARCHIVED;
    }

    public void unarchive() {
        this.status = WishlistStatus.ACTIVE;
    }

    public Wishlist(String title, String description, User owner) {
        this.title = title;
        this.description = description;
        this.owner = owner;
        this.status = WishlistStatus.ACTIVE;
    }

    public void edit(String newTitle, String newDescription) {
        this.title = newTitle;
        this.description = newDescription;
    }

}
