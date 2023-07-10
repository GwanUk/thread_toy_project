package com.matzip.thread.uri.application.service;

import com.matzip.thread.uri.application.port.in.UriInPort;
import com.matzip.thread.uri.application.port.out_.UriOutPort;
import com.matzip.thread.uri.domain.UriEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UriService implements UriInPort {

    private final UriOutPort uriOutPort;

    @Override
    public List<UriEntity> findAll() {
        return uriOutPort.findAllWithRoles();
    }
}
