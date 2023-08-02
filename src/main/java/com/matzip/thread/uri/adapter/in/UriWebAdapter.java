package com.matzip.thread.uri.adapter.in;

import com.matzip.thread.common.annotation.WebAdapter;
import com.matzip.thread.uri.application.port.in.UriWebPort;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@WebAdapter(path = "/api/uri")
public class UriWebAdapter {

    private final UriWebPort uriWebPort;

    @GetMapping("/all")
    List<UriResponse> findAll() {
        return uriWebPort.findAll().stream().map(UriResponse::from).toList();
    }

    @GetMapping
    Optional<UriResponse> findByUri(@Validated @RequestBody UriRequest uriRequest) {
        String uri = uriRequest.getUri();
        return uriWebPort.findByUri(uri).map(UriResponse::from);
    }

    @PostMapping
    void save(@Validated @RequestBody UriSaveRequest uriSaveRequest) {
        uriWebPort.save(uriSaveRequest.toEntity());
    }

    @PutMapping
    void update(@Validated @RequestBody UriUpdateRequest uriUpdateRequest) {
        uriWebPort.update(uriUpdateRequest.toEntity());
    }

    @DeleteMapping
    void delete(@Validated @RequestBody UriRequest uriRequest) {
        String uri = uriRequest.getUri();
        uriWebPort.delete(uri);
    }
}

