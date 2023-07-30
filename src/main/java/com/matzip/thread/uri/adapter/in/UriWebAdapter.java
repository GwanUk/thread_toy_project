package com.matzip.thread.uri.adapter.in;

import com.matzip.thread.common.annotation.WebAdapter;
import com.matzip.thread.uri.application.port.in.UriInPort;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@WebAdapter(path = "/api/uri")
public class UriWebAdapter {

    private final UriInPort uriInPort;

    @GetMapping("/all")
    List<UriResponse> findAll() {
        return uriInPort.findAll().stream().map(UriResponse::from).toList();
    }

    @GetMapping
    Optional<UriResponse> findByUri(@Validated @RequestBody UriRequest uriRequest) {
        String uri = uriRequest.getUri();
        return uriInPort.findByUri(uri).map(UriResponse::from);
    }

    @PostMapping
    void save(@Validated @RequestBody UriSaveRequest uriSaveRequest) {
        uriInPort.save(uriSaveRequest.toEntity());
    }

    @PutMapping
    void update(@Validated @RequestBody UriUpdateRequest uriUpdateRequest) {
        uriInPort.update(uriUpdateRequest.toEntity());
    }

    @DeleteMapping
    void delete(@Validated @RequestBody UriRequest uriRequest) {
        String uri = uriRequest.getUri();
        uriInPort.delete(uri);
    }
}

