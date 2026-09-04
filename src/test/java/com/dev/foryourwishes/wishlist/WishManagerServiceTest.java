package com.dev.foryourwishes.wishlist;

import com.dev.foryourwishes.user.User;
import com.dev.foryourwishes.user.UserManagerService;
import com.dev.foryourwishes.wishlist.entity.Reservation;
import com.dev.foryourwishes.wishlist.entity.Wish;
import com.dev.foryourwishes.wishlist.entity.Wishlist;
import com.dev.foryourwishes.wishlist.exception.*;
import com.dev.foryourwishes.wishlist.repository.ReservationRepository;
import com.dev.foryourwishes.wishlist.repository.WishRepository;
import com.dev.foryourwishes.wishlist.service.WishManagerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WishManagerServiceTest {

    @Mock
    private WishRepository wishRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private UserManagerService userManagerService;

    @InjectMocks
    private WishManagerService wishManagerService;

    @Test
    void shouldNotReserveFulfilledWish() {
        Long wishId = 1L;
        Long userId = 1L;
        User owner = new User("owner@test.ru", "owner", "hash");
        Wishlist wishlist = new Wishlist("wishlist", "-", owner);
        Wish wish = new Wish("wish", "-", "https://example.com", wishlist);
        wish.markAsFulfilled();

        when(wishRepository.findById(wishId))
                .thenReturn(Optional.of(wish));

        assertThrows(WishFulfilledException.class,
                () -> wishManagerService.reserveWish(wishId, userId));
        verify(reservationRepository, never())
                .save(any());
    }

    @Test
    void shouldNotReserveOwnWish() {
        Long wishId = 1L;
        Long userId = 1L;
        User owner = mock(User.class);
        Wishlist wishlist = new Wishlist("wishlist", "-", owner);
        Wish wish = new Wish("wish", "-", "https://example.com", wishlist);

        when(owner.getId()).thenReturn(userId);
        when(wishRepository.findById(wishId))
                .thenReturn(Optional.of(wish));
        when(userManagerService.findById(userId))
                .thenReturn(owner);

        assertThrows(OwnWishReservationException.class,
                () -> wishManagerService.reserveWish(wishId, userId));
        verify(reservationRepository, never())
                .save(any());
    }

    @Test
    void shouldNotReserveAlreadyReservedWish() {
        Long wishId = 1L;
        Long userId = 1L;
        User owner = new User("owner@test.ru", "owner", "hash");
        Wishlist wishlist = new Wishlist("wishlist", "-", owner);
        Wish wish = new Wish("wish", "-", "https://example.com", wishlist);

        when(wishRepository.findById(wishId))
                .thenReturn(Optional.of(wish));
        when(reservationRepository.existsByWishId(wishId))
                .thenReturn(true);

        assertThrows(WishReservedException.class,
                () -> wishManagerService.reserveWish(wishId, userId));
        verify(reservationRepository, never())
                .save(any());
    }

    @Test
    void shouldNotReserveWishFromArchivedWishlist(){
        Long wishId = 1L;
        Long userId = 1L;
        User owner = new User("owner@test.ru", "owner", "hash");
        Wishlist wishlist = new Wishlist("wishlist", "-", owner);
        wishlist.archive();
        Wish wish = new Wish("wish", "-", "https://example.com", wishlist);

        when(wishRepository.findById(wishId))
                .thenReturn(Optional.of(wish));
        when(reservationRepository.existsByWishId(wishId))
                .thenReturn(false);

        assertThrows(WishlistArchivedException.class,
                () -> wishManagerService.reserveWish(wishId, userId));
        verify(reservationRepository, never())
                .save(any());
    }

    @Test
    void shouldNotCancelAnotherUsersReservation(){
        Long wishId = 1L;
        Long userId = 1L;
        Long reserverId = 2L;
        User reserver = mock(User.class);
        Wishlist wishlist = new Wishlist("wishlist", "-", null);
        Wish wish = new Wish("wish", "-", "https://example.com", wishlist);
        Reservation reservation = new Reservation(wish, reserver);

        when(reserver.getId()).thenReturn(reserverId);
        when(reservationRepository.findByWishId(wishId))
                .thenReturn(Optional.of(reservation));

        assertThrows(ReservationAccessDeniedException.class,
                () -> wishManagerService.cancelReservation(wishId, userId));
        verify(reservationRepository, never())
                .delete(any());
    }
}
