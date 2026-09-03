package com.dev.foryourwishes.wishlist;

import com.dev.foryourwishes.user.User;
import com.dev.foryourwishes.user.UserRepository;
import com.dev.foryourwishes.wishlist.entity.Reservation;
import com.dev.foryourwishes.wishlist.entity.Wish;
import com.dev.foryourwishes.wishlist.entity.Wishlist;
import com.dev.foryourwishes.wishlist.exception.*;
import com.dev.foryourwishes.wishlist.repository.ReservationRepository;
import com.dev.foryourwishes.wishlist.repository.WishRepository;
import com.dev.foryourwishes.wishlist.repository.WishlistRepository;
import com.dev.foryourwishes.wishlist.service.WishManagerService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
public class WishManagerServiceIntegrationTest {
    @Container
    @ServiceConnection
    static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:latest");

    @Autowired
    private WishManagerService wishManagerService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WishRepository wishRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private WishlistRepository wishlistRepository;

    @Test
    void shouldReserveAvailableWish() {
        User owner = userRepository.save(new User("owner@test.ru", "owner", "hash"));
        User reserver = userRepository.save(new User("reserver@test.ru", "reserver", "hash"));
        Wishlist wishlist = wishlistRepository.save(new Wishlist("wishlist", "-", owner));
        Wish wish = wishRepository.save(new Wish("wish", "-", "https://example", wishlist));

        wishManagerService.reserveWish(wish.getId(), reserver.getId());

        Optional<Reservation> reservation = reservationRepository.findByWishId(wish.getId());
        assertTrue(reservation.isPresent());
        Reservation savedReservation = reservation.get();
        assertEquals(reserver.getId(), savedReservation.getReservedBy().getId());
        assertEquals(wish.getId(), savedReservation.getWish().getId());
    }

    @Test
    void shouldNotReserveOwnWish(){
        User owner = userRepository.save(new User("owner@test.ru", "owner", "hash"));
        Wishlist wishlist = wishlistRepository.save(new Wishlist("wishlist", "-", owner));
        Wish wish = wishRepository.save(new Wish("wish", "-", "https://example", wishlist));

        assertThrows(OwnWishReservationException.class,
                () -> wishManagerService.reserveWish(wish.getId(), owner.getId()));

        Optional<Reservation> reservation = reservationRepository.findByWishId(wish.getId());
        assertFalse(reservation.isPresent());
    }

    @Test
    void shouldNotReserveAlreadyReservedWish(){
        User owner = userRepository.save(new User("owner@test.ru", "owner", "hash"));
        User reserver = userRepository.save(new User("reserver@test.ru", "reserver", "hash"));
        User secondReserver = userRepository.save(new User("second_reserver@test.ru", "second_reserver", "hash"));
        Wishlist wishlist = wishlistRepository.save(new Wishlist("wishlist", "-", owner));
        Wish wish = wishRepository.save(new Wish("wish", "-", "https://example", wishlist));
        wishManagerService.reserveWish(wish.getId(), reserver.getId());
        Reservation reservation = reservationRepository
                .findByWishId(wish.getId())
                .orElseThrow();

        assertThrows(WishReservedException.class,
                () -> wishManagerService.reserveWish(wish.getId(), secondReserver.getId()));
        assertEquals(
                reserver.getId(),
                reservation.getReservedBy().getId()
        );
    }

    @Test
    void shouldNotReserveFulfilledWish(){
        User owner = userRepository.save(new User("owner@test.ru", "owner", "hash"));
        User reserver = userRepository.save(new User("reserver@test.ru", "reserver", "hash"));
        Wishlist wishlist = wishlistRepository.save(new Wishlist("wishlist", "-", owner));
        Wish wish = new Wish("wish", "-", "https://example", wishlist);
        wish.markAsFulfilled();
        wishRepository.save(wish);

        assertThrows(WishFulfilledException.class,
                ()  -> wishManagerService.reserveWish(wish.getId(), reserver.getId()));

        Optional<Reservation> reservation = reservationRepository.findByWishId(wish.getId());
        assertFalse(reservation.isPresent());
    }

    @Test
    void shouldNotReserveWishFromArchivedWishlist(){
        User owner = userRepository.save(new User("owner@test.ru", "owner", "hash"));
        User reserver = userRepository.save(new User("reserver@test.ru", "reserver", "hash"));
        Wishlist wishlist = new Wishlist("wishlist", "-", owner);
        wishlist.archive();
        wishlistRepository.save(wishlist);
        Wish wish = wishRepository.save(new Wish("wish", "-", "https://example", wishlist));

        assertThrows(WishlistArchivedException.class,
                ()  -> wishManagerService.reserveWish(wish.getId(), reserver.getId()));

        Optional<Reservation> reservation = reservationRepository.findByWishId(wish.getId());
        assertFalse(reservation.isPresent());
    }

    @Test
    void shouldCancelOwnReservation(){
        User owner = userRepository.save(new User("owner@test.ru", "owner", "hash"));
        User reserver = userRepository.save(new User("reserver@test.ru", "reserver", "hash"));
        Wishlist wishlist = wishlistRepository.save(new Wishlist("wishlist", "-", owner));
        Wish wish = wishRepository.save(new Wish("wish", "-", "https://example", wishlist));
        wishManagerService.reserveWish(wish.getId(), reserver.getId());

        wishManagerService.cancelReservation(wish.getId(), reserver.getId());

        Optional<Reservation> reservation = reservationRepository.findByWishId(wish.getId());
        assertFalse(reservation.isPresent());
    }

    @Test
    void shouldNotCancelAnotherUsersReservation(){
        User owner = userRepository.save(new User("owner@test.ru", "owner", "hash"));
        User reserver = userRepository.save(new User("reserver@test.ru", "reserver", "hash"));
        User secondReserver = userRepository.save(new User("second_reserver@test.ru", "second_reserver", "hash"));
        Wishlist wishlist = wishlistRepository.save(new Wishlist("wishlist", "-", owner));
        Wish wish = wishRepository.save(new Wish("wish", "-", "https://example", wishlist));
        wishManagerService.reserveWish(wish.getId(), reserver.getId());

        assertThrows(ReservationAccessDeniedException.class,
                () -> wishManagerService.cancelReservation(wish.getId(), secondReserver.getId()));
    }

    @Test
    void shouldThrowWhenCancellingNonExistingReservation(){
        User owner = userRepository.save(new User("owner@test.ru", "owner", "hash"));
        User reserver = userRepository.save(new User("reserver@test.ru", "reserver", "hash"));
        Wishlist wishlist = wishlistRepository.save(new Wishlist("wishlist", "-", owner));
        Wish wish = wishRepository.save(new Wish("wish", "-", "https://example", wishlist));

        assertThrows(ReservationNotFoundException.class,
                () -> wishManagerService.cancelReservation(wish.getId(), reserver.getId()));
    }

    @AfterEach
    void cleanDB() {
        reservationRepository.deleteAll();
        wishRepository.deleteAll();
        wishlistRepository.deleteAll();
        userRepository.deleteAll();
    }
}
