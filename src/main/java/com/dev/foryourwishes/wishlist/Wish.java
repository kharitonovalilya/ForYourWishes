package com.dev.foryourwishes.wishlist;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "wishes")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Wish {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, length = 50)
    private String title;

    private String description;
    private String url;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "wishlist_id", nullable = false)
    private Wishlist wishlist;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WishStatus status;
}
