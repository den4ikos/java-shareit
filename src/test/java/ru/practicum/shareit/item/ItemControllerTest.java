package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoToUser;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.mapper.CommentMapper;
import ru.practicum.shareit.mapper.ItemMapper;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.service.CommentService;
import ru.practicum.shareit.service.ItemRequestService;
import ru.practicum.shareit.service.ItemService;
import ru.practicum.shareit.service.UserService;
import ru.practicum.shareit.user.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ItemControllerTest {
    @InjectMocks
    private ItemController controller;
    @Mock
    private ItemService itemService;
    @Mock
    private ItemRequestService itemRequestService;
    @Mock
    private UserService userService;
    @Mock
    private CommentService commentService;
    private User owner;
    private ItemDto itemDto;
    private ItemDto itemDtoWithRequest;
    private ItemDto newItemDto;
    private ItemRequest itemRequest;
    private User requestor;
    private Item item1;
    private Item item2;
    private Item item3;
    private Item item4;
    private CommentDto commentDto;
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private Map<String, Object> params;

    @BeforeEach
    void beforeEach() {
        params = new LinkedHashMap<>();
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        itemDto = new ItemDto(
                1L,
                "Test thing",
                "Test Description",
                true,
                null,
                null
        );

        newItemDto = new ItemDto(
                1L,
                "Test thing",
                "Test Description for new item",
                true,
                null,
                null
        );

        itemDtoWithRequest = new ItemDto(
                2L,
                "Test thing with request",
                "Test Description with request",
                true,
                null,
                1L);

        owner = new User(
                1L,
                "Kolya",
                "kolya@test.test");

        item1 = new Item(1L, "Test thing", "Test Description", true, null);
        item2 = new Item(2L, "Test thing 2", "Test Description 2", true, null);
        item3 = new Item(3L, "Testthing3", "Test Description 3", true, null);
        item4 = new Item(4L, "Test thing 4", "Test Description 4", true, null);

        requestor = new User(
                2L,
                "Petya",
                "petya@test.test"
        );

        itemRequest = new ItemRequest(
                1L,
                "Test item request",
                requestor,
                LocalDateTime.now()
        );

        commentDto = new CommentDto(
                1L,
                "Test comment",
                1L,
                requestor.getName());
    }

    @Test
    void addItemWithoutRequestAndReturnHttp200() throws Exception {
        when(userService.getById(anyLong())).thenReturn(owner);
        when(itemService.create(any(Item.class))).thenReturn(ItemMapper.toItem(itemDto, null));

        mockMvc.perform(
                post("/items")
                    .header(Constants.HEADER_USER_ID, 1L)
                    .content(objectMapper.writeValueAsString(itemDtoWithRequest))
                    .characterEncoding(StandardCharsets.UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.name", is("Test thing")))
                .andExpect(jsonPath("$.description", is("Test Description")))
                .andExpect(jsonPath("$.requestId", is(nullValue())));
    }

    @Test
    void addNewItemWithRequestAndReturnHttp200() throws Exception {
        when(userService.getById(anyLong()))
                .thenReturn(owner);
        when(itemRequestService.getById(anyLong()))
                .thenReturn(itemRequest);
        when(itemService.create(any(Item.class)))
                .thenReturn(ItemMapper.toItem(itemDtoWithRequest, itemRequest));

        mockMvc.perform(post("/items")
                .header(Constants.HEADER_USER_ID, 1L)
                .content(objectMapper.writeValueAsString(itemDtoWithRequest))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.name", is("Test thing with request")))
                .andExpect(jsonPath("$.description", is("Test Description with request")))
                .andExpect(jsonPath("$.requestId", is(1)));
    }

    @Test
    void updateItemAndReturnHttp200() throws Exception {
        item1.setOwner(owner);
        when(userService.getById(anyLong()))
                .thenReturn(owner);
        when(itemService.getById(anyLong()))
                .thenReturn(item1);
        doNothing().when(itemService).checkUserForItem(any(Item.class), any(User.class));
        when(itemService.update(null, owner))
                .thenReturn(ItemMapper.toItem(newItemDto, null));


        mockMvc.perform(patch("/items/1")
                .header(Constants.HEADER_USER_ID, 1L)
                .content(objectMapper.writeValueAsString(newItemDto))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.name", is("Test thing")))
                .andExpect(jsonPath("$.description", is("Test Description for new item")))
                .andExpect(jsonPath("$.requestId", is(nullValue())));
    }

    @Test
    void findItemAndReturnHttp200() throws Exception {
        List<Item> userItems = List.of(item1, item2, item3, item4);
        userItems.forEach(i1 -> i1.setOwner(owner));
        List<ItemDtoToUser> userItemsDto = userItems.stream()
                .map(item1 -> ItemMapper.toItemDtoToUser(item1, Collections.emptyList()))
                .collect(Collectors.toList());
        when(userService.getById(anyLong()))
                .thenReturn(owner);
        when(itemService.getById(anyLong()))
                .thenReturn(item1);
        when(itemService.getUserItems(any(User.class)))
                .thenReturn(userItemsDto);


        mockMvc.perform(get("/items/2")
                .header(Constants.HEADER_USER_ID, 1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.name", is("Test thing 2")))
                .andExpect(jsonPath("$.description", is("Test Description 2")));
    }

    @Test
    void findUserItemsAndReturnHttp200() throws Exception {
        List<Item> userItems = List.of(item1, item2, item3, item4);
        userItems.forEach(i1 -> i1.setOwner(owner));
        List<ItemDtoToUser> userItemsDto = userItems.stream()
                .map(item1 -> ItemMapper.toItemDtoToUser(item1, Collections.emptyList()))
                .collect(Collectors.toList());
        when(userService.getById(anyLong()))
                .thenReturn(owner);
        when(itemService.getUserItems(owner))
                .thenReturn(List.of(userItemsDto.get(1)));


        mockMvc.perform(get("/items/?from=1&size=1")
                .header(Constants.HEADER_USER_ID, 1L)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(jsonPath("$[0].name", is("Test thing 2")))
                .andExpect(jsonPath("$[0].description", is("Test Description 2")));
    }

    @Test
    void searchItemsByRequestParametersAndReturnHttp200() throws Exception {
        params.put("text", "Testthing3");
        List<Item> userItems = List.of(item1, item2, item3, item4);
        item3.setOwner(owner);
        when(itemService.search(params))
                .thenReturn(List.of(userItems.get(2)));


        mockMvc.perform(get("/items/search?text=Testthing3")
                .header(Constants.HEADER_USER_ID, 1L)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(jsonPath("$[0].name", is("Testthing3")))
                .andExpect(jsonPath("$[0].description", is("Test Description 3")));
    }

    @Test
    void addCommentToItemAndReturnHttp200() throws Exception {
        List<Item> userItems = List.of(item1, item2, item3, item4);
        userItems.forEach(i -> i.setOwner(owner));
        when(userService.getById(anyLong())).thenReturn(requestor);
        when(itemService.getById(1L)).thenReturn(item1);
        when(commentService.save(requestor, item1, commentDto)).thenReturn(CommentMapper.toComment(requestor, item1, commentDto.getText()));


        mockMvc.perform(post("/items/1/comment")
                .header(Constants.HEADER_USER_ID, 2L)
                .content(objectMapper.writeValueAsString(commentDto))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.text", is("Test comment")))
                .andExpect(jsonPath("$.itemId", is(1)));
    }
}
