package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.Constants;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.Map;

@Service
public class ItemRequestClient extends BaseClient {
    @Autowired
    public ItemRequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + Constants.ITEM_REQUEST_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> getAllRequests(Long userId, Long from, Long size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("/all/?from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> getUserRequests(Long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> getUserRequest(Long userId, Long requestId) {
        return get("/" + requestId, userId);
    }

    public ResponseEntity<Object> addRequest(Long userId, ItemRequestDto requestDto) {
        return post("", userId, requestDto);
    }
}
