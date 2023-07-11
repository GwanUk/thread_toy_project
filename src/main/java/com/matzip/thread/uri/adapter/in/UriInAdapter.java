package com.matzip.thread.uri.adapter.in;

import com.matzip.thread.uri.application.port.in.UriInPort;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/uri")
@RequiredArgsConstructor
public class UriInAdapter {

    private final UriInPort uriInPort;

    @PostMapping
    void save(@RequestBody UriSaveRequest uriSaveRequest) {
        uriInPort.save(uriSaveRequest.toEntity());
    }
}
