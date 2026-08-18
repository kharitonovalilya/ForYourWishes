package com.dev.foryourwishes.wishlist;

import com.dev.foryourwishes.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reservations")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "wish_id", nullable = false, unique = true)
    private Wish wish;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reserved_by_id", nullable = false)
    private User reservedBy;
}
