package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.Constants;
import ru.practicum.shareit.booking.dto.BookingDtoForUser;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.mapper.BookingMapper;
import ru.practicum.shareit.mapper.ItemMapper;
import ru.practicum.shareit.service.BookingService;
import ru.practicum.shareit.service.ItemService;
import ru.practicum.shareit.service.UserService;
import ru.practicum.shareit.user.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class BookingControllerTest {
    private Item item;
    private User booker;
    private User owner;
    private ItemDto itemDto;
    private Booking booking;
    private BookingDtoForUser bookingDtoForUser;
    @Mock
    private ItemService itemService;
    @Mock
    private UserService userService;
    @Mock
    private BookingService bookingService;
    private MockMvc mockMvc;
    @InjectMocks
    private BookingController controller;
    private final ObjectMapper objectMapper = JsonMapper.builder().addModule(new JavaTimeModule()).build();
    private Map<String, Object> params;

    @BeforeEach
    public void beforeEach() {
        params = new LinkedHashMap<>();
        params.put("state", "FUTURE");
        params.put("from", "0");
        params.put("size", "1");

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();

        itemDto = new ItemDto(1L, "Test thing", "Test Description", true, null, null);

        item = new Item(
                1L,
                "Test thing",
                "Test Descriptoon",
                true,
                null
        );

        owner = new User(
                1L,
                "Kolya",
                "kolya@test.test"
        );

        booker = new User(
                3L,
                "Petya",
                "petya@test.test"
        );

        bookingDtoForUser = new BookingDtoForUser(
                1L,
                LocalDateTime.of(2023, 6,1, 0, 0),
                LocalDateTime.of(2023, 12, 1, 0, 0)
        );

        booking = new Booking(
                1L,
                LocalDateTime.of(2023, 11, 1, 0, 0),
                LocalDateTime.of(2023, 12, 1, 0, 0),
                item,
                booker,
                StatusType.APPROVED
        );
    }

    @Test
    public void addNewBookingAndReturnHttp200WithId3AndStatusWaiting() throws Exception {
        when(userService.getById(anyLong())).thenReturn(booker);
        when(itemService.getById(1L)).thenReturn(ItemMapper.toItem(itemDto, null));
        when(bookingService.create(any(Booking.class))).thenReturn(BookingMapper.toBooking(bookingDtoForUser, item, booker));

        mockMvc.perform(
                post("/bookings")
                        .header(Constants.HEADER_USER_ID, 3L)
                        .content(objectMapper.writeValueAsString(bookingDtoForUser))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.booker.id", is(3)))
                .andExpect(jsonPath("$.status", is(StatusType.WAITING.name())));
    }

    @Test
    public void updateBookingAndReturnHttp200AndStatusApproved() throws Exception {
        when(userService.getById(anyLong())).thenReturn(owner);
        when(bookingService.updateStatus(owner, booking.getId(), true)).thenReturn(booking);

        mockMvc.perform(patch("/bookings/1/?approved=true")
                .header(Constants.HEADER_USER_ID, 1L)
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.status", is(StatusType.APPROVED.name())));
    }

    @Test
    public void findBookingAndReturnHttp200WithStatusApproved() throws Exception {
        when(userService.getById(anyLong())).thenReturn(owner);
        when(bookingService.findById(owner, booking.getId())).thenReturn(booking);

        mockMvc.perform(get("/bookings/1")
                .header(Constants.HEADER_USER_ID, 1L)
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.status", is(StatusType.APPROVED.name())));
    }

    @Test
    public void findBookingByCreatorAndReturnHttp200WithStatusApproved() throws Exception {
        when(userService.getById(3L)).thenReturn(booker);
        when(bookingService.getAll(booker, params.get("state").toString(), new HashMap<>())).thenReturn(List.of(booking));

        mockMvc.perform(get("/bookings/?state=FUTURE&from=0&size=1")
                .header(Constants.HEADER_USER_ID, 3L)
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.[0].status", is(StatusType.APPROVED.name())));
    }

    @Test
    public void findBookingByOwnerAndReturnHttp200WithStatusApproved() throws Exception {
        when(userService.getById(1L)).thenReturn(owner);
        when(itemService.getUserItems(owner)).thenReturn(List.of(ItemMapper.toItemDtoToUser(item, Collections.emptyList())));
        when(bookingService.getAllForOwner(owner, params.get("state").toString(), params)).thenReturn(List.of(booking));

        mockMvc.perform(get("/bookings/owner/?state=FUTURE&from=0&size=1")
                .header(Constants.HEADER_USER_ID, 1L)
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.[0].status", is(StatusType.APPROVED.name())));
    }
}
