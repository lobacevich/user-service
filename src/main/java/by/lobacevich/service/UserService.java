package by.lobacevich.service;

import by.lobacevich.dto.request.UserDtoRequest;
import by.lobacevich.dto.response.UserDtoResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserService {
    UserDtoResponse create(UserDtoRequest dto);

    @Transactional
    UserDtoResponse update(UserDtoRequest dto, Long id);

    @Transactional
    void delete(Long id);

    UserDtoResponse findById(Long id);

    List<UserDtoResponse> findUsers(String firstname,
                                    String surname,
                                    int pageNumber,
                                    int pageSize);
    @Transactional
    void activate(Long id);

    @Transactional
    void deactivate(Long id);
}
