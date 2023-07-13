package com.matzip.thread.uri.adapter.in;

import com.matzip.thread.common.annotation.WebAdapter;
import com.matzip.thread.uri.application.port.in.UriInPort;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RequiredArgsConstructor
@WebAdapter(path = "/api/uri")
public class UriWebAdapter {

    private final UriInPort uriInPort;

    @GetMapping
    List<UriResponse> findAll() {
        return uriInPort.findAll().stream().map(UriResponse::fromEntity).toList();
    }

    @PostMapping
    void save(@RequestBody UriSaveRequest uriSaveRequest) {
        uriInPort.save(uriSaveRequest.toEntity());
    }
}

