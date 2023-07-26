package com.matzip.thread.uri.adapter.in;

import com.matzip.thread.common.annotation.WebAdapter;
import com.matzip.thread.uri.application.port.in.UriInPort;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@WebAdapter(path = "/api/uri")
public class UriWebAdapter {

    private final UriInPort uriInPort;

    @GetMapping
    List<UriResponse> findAll() {
        return uriInPort.findAll().stream().map(UriResponse::from).toList();
    }

    @GetMapping("/{uri}")
    Optional<UriResponse> findByUri(@PathVariable String uri) {
        uri = uri.replace('$', '/');
        return uriInPort.findByUri(uri).map(UriResponse::from);
    }

    @PostMapping
    void save(@RequestBody UriSaveRequest uriSaveRequest) {
        uriInPort.save(uriSaveRequest.toEntity());
    }
}

