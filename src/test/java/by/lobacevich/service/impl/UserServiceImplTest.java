package by.lobacevich.service.impl;

import by.lobacevich.dto.request.UserDtoRequest;
import by.lobacevich.dto.response.UserDtoResponse;
import by.lobacevich.entity.User;
import by.lobacevich.mapper.UserMapper;
import by.lobacevich.repository.PaymentCardRepository;
import by.lobacevich.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    public static final Long ID = 1L;

    @Mock
    UserRepository repository;

    @Mock
    UserMapper mapper;

    @Mock
    PaymentCardRepository cardRepository;

    @Mock
    UserDtoRequest dtoRequest;

    @Mock
    UserDtoResponse dtoResponse;

    @Mock
    User user;

    @InjectMocks
    private UserServiceImpl service;

    @Test
    void create_ShouldCallSaveMethodOfRepositoryAndReturnUserDtoResponse() {
        when(mapper.dtoToUser(dtoRequest)).thenReturn(user);
        when(repository.save(user)).thenReturn(user);
        when(mapper.userToDto(user)).thenReturn(dtoResponse);

        UserDtoResponse actual = service.create(dtoRequest);

        verify(repository, times(1)).save(user);
        assertEquals(dtoResponse, actual);
    }

    @Test
    void update_ShouldCallSaveMethodOfRepositoryAndReturnUserDtoResponse() {

    }

    @Test
    void delete() {
    }

    @Test
    void findById() {
    }

    @Test
    void findUsers() {
    }

    @Test
    void activate() {
    }

    @Test
    void deactivate() {
    }
}