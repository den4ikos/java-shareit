package ru.practicum.shareit.booking;

import ru.practicum.shareit.Constants;
import ru.practicum.shareit.booking.dto.BookingDtoForUser;
import ru.practicum.shareit.booking.dto.BookingState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.BadRequestException;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
	private final BookingClient bookingClient;

	@PostMapping
	public ResponseEntity<Object> create(@RequestHeader(Constants.HEADER_USER_ID) Long userId,
										   @RequestBody @Valid BookingDtoForUser params) {
		log.info("Creating booking {}, userId={}", params, userId);
		return bookingClient.bookItem(userId, params);
	}

	@PatchMapping("/{bookingId}")
	public ResponseEntity<Object> updateStatusBooking(@RequestHeader(Constants.HEADER_USER_ID) Long ownerId, @PathVariable Long bookingId,
													  @RequestParam Boolean approved) {
		return bookingClient.updateStatusBooking(ownerId, bookingId, approved);
	}

	@GetMapping
	public ResponseEntity<Object> getAll(@RequestHeader(Constants.HEADER_USER_ID) Long userId,
											  @RequestParam(name = "state", defaultValue = "all") String stateParam,
											  @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
											  @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
		BookingState state = BookingState.from(stateParam)
				.orElseThrow(() -> new BadRequestException("Unknown state: " + stateParam));
		log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
		return bookingClient.getBookings(userId, state, from, size);
	}



	@GetMapping("/{bookingId}")
	public ResponseEntity<Object> getBooking(@RequestHeader(Constants.HEADER_USER_ID) Long userId,
											 @PathVariable Long bookingId) {
		log.info("Get booking {}, userId={}", bookingId, userId);
		return bookingClient.getBooking(userId, bookingId);
	}

	@GetMapping("/owner")
	public ResponseEntity<Object> findOwnerBookings(@RequestHeader(Constants.HEADER_USER_ID) Long userId,
													@RequestParam(name = "state", defaultValue = "all") String stateParam,
													@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
													@Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
		BookingState state = BookingState
				.from(stateParam)
				.orElseThrow(() -> new BadRequestException("Unknown state: " + stateParam));
		log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
		return bookingClient.getBookingsOwner(userId, state, from, size);
	}
}