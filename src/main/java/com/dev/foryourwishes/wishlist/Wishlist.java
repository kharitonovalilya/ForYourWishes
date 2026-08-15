package com.dev.foryourwishes.wishlist;

import com.dev.foryourwishes.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Wishlist {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WishlistStatus status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @OneToMany(mappedBy = "wishlist_id", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Wish> wishes = new ArrayList<>();

    public void archive() {
        this.status = WishlistStatus.ARCHIVED;
    }

    public void unarchive() {
        this.status = WishlistStatus.ACTIVE;
    }
}
