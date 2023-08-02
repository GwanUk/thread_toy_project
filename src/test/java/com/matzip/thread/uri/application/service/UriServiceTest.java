package com.matzip.thread.uri.application.service;

import com.matzip.thread.uri.application.port.out_.UriPersistencePort;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UriServiceTest {

    @InjectMocks
    private UriService uriService;
    @Mock
    private UriPersistencePort uriPersistencePort;

}