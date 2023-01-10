package ru.practicum.shareit.request;

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
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.service.ItemRequestService;
import ru.practicum.shareit.service.ItemService;
import ru.practicum.shareit.service.UserService;
import ru.practicum.shareit.user.User;

import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ItemRequestControllerTest {
    @Mock
    private ItemService itemService;
    @Mock
    private ItemRequestService requestService;
    @Mock
    private UserService userService;
    @InjectMocks
    private ItemRequestController controller;
    private MockMvc mockMvc;
    private Item item;
    private User requestor;
    private ItemRequestDto itemRequestDto;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private Map<String, Object> params = new LinkedHashMap<>();

    @BeforeEach
    public void beforeEach() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();

        item = new Item(
                1L,
                "Test",
                "Description",
                true,
                null
        );

        requestor = new User(
                2L,
                "Kolya",
                "kolya@test.test"
        );
        itemRequestDto = new ItemRequestDto();
    }

    @Test
    public void addNewItemRequestAndReturnHttp200() throws Exception {
        itemRequestDto.setId(1L);
        itemRequestDto.setDescription("Test Description 2");
        when(userService.getById(2L)).thenReturn(requestor);
        when(requestService.add(any(ItemRequest.class))).thenReturn(ItemRequestMapper.toItemRequest(itemRequestDto, requestor));


        mockMvc.perform(post("/requests")
                .header(Constants.HEADER_USER_ID, 2L)
                .content(objectMapper.writeValueAsString(itemRequestDto))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().is(HttpStatus.OK.value()))
            .andExpect(jsonPath("$.description", is("Test Description 2")))
            .andExpect(jsonPath("$.requestorId", is(2)));
    }

    @Test
    public void findUserRequestAndReturnHttp200() throws Exception {
        itemRequestDto.setId(1L);
        itemRequestDto.setDescription("Test Description 2");
        when(userService.getById(2L)).thenReturn(requestor);
        when(requestService.getRequests(requestor)).thenReturn(List.of(ItemRequestMapper.toItemRequest(itemRequestDto, requestor)));


        mockMvc.perform(get("/requests")
                .header(Constants.HEADER_USER_ID, 2L)
                .content(objectMapper.writeValueAsString(itemRequestDto))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().is(HttpStatus.OK.value()))
            .andExpect(jsonPath("$[0].description", is("Test Description 2")))
            .andExpect(jsonPath("$[0].requestorId", is(2)));
    }
    @SuppressWarnings("checkstyle:EmptyLineSeparator")
    @Test
    public void findAllRequestsAndReturnHttp200() throws Exception {
        params.put("from", "0");
        params.put("size", "1");
        itemRequestDto.setId(1L);
        itemRequestDto.setDescription("Test Description 2");
        when(userService.getById(2L)).thenReturn(requestor);
        when(requestService.getAll(requestor, params)).thenReturn(List.of(ItemRequestMapper.toItemRequest(itemRequestDto, requestor)));
        when(itemService.getItemByRequest(any(ItemRequest.class))).thenReturn(List.of(item));

        mockMvc.perform(get("/requests/all/?from=0&size=1")
                .header(Constants.HEADER_USER_ID, 2L)
                .content(objectMapper.writeValueAsString(itemRequestDto))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().is(HttpStatus.OK.value()))
            .andExpect(jsonPath("$[0].description", is("Test Description 2")))
            .andExpect(jsonPath("$[0].requestorId", is(2)));
    }

    @Test
    public void testFindUserRequest() throws Exception {
        itemRequestDto.setId(1L);
        itemRequestDto.setDescription("Test Description 2");
        when(userService.getById(2L)).thenReturn(requestor);
        when(itemService.getItemByRequest(any(ItemRequest.class))).thenReturn(List.of(item));
        when(requestService.getById(1L)).thenReturn(ItemRequestMapper.toItemRequest(itemRequestDto, requestor));


        mockMvc.perform(get("/requests/1")
                .header(Constants.HEADER_USER_ID, 2L)
                .content(objectMapper.writeValueAsString(itemRequestDto))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().is(HttpStatus.OK.value()))
            .andExpect(jsonPath("$.description", is("Test Description 2")))
            .andExpect(jsonPath("$.requestorId", is(2)));
    }
}
