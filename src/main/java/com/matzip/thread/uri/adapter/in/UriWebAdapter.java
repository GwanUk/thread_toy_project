package com.matzip.thread.uri.adapter.in;

import com.matzip.thread.common.annotation.WebAdapter;
import com.matzip.thread.uri.application.port.in.UriInPort;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@WebAdapter(path = "/api/uri")
public class UriWebAdapter {

    private final UriInPort uriInPort;

    @PostMapping
    void save(@RequestBody UriSaveRequest uriSaveRequest) {
        uriInPort.save(uriSaveRequest.toEntity());
    }
}
