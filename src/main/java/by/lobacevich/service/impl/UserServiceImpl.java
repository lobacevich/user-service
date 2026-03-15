package by.lobacevich.service.impl;

import by.lobacevich.dto.request.UserDtoRequest;
import by.lobacevich.dto.response.UserDtoResponse;
import by.lobacevich.entity.User;
import by.lobacevich.exception.EntityNotFoundException;
import by.lobacevich.exception.InvalidDataException;
import by.lobacevich.mapper.UserMapper;
import by.lobacevich.repository.PaymentCardRepository;
import by.lobacevich.repository.UserRepository;
import by.lobacevich.service.UserService;
import by.lobacevich.specification.UserSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final PaymentCardRepository cardRepository;

    @Transactional
    @Override
    public UserDtoResponse create(UserDtoRequest dto) {
        try {
            User user = repository.save(mapper.dtoToUser(dto));
            return mapper.userToDto(findUserById(user.getId()));
        } catch (DataIntegrityViolationException e) {
            throw new InvalidDataException("Duplicate email");
        }
    }

    @Transactional
    @Override
    public UserDtoResponse update(UserDtoRequest dto, Long id) {
        User oldUser = findUserById(id);
        User newUser = mapper.dtoToUser(dto);
        newUser.setId(id);
        newUser.setActive(oldUser.getActive());
        newUser.setCreatedAt(oldUser.getCreatedAt());
        try {
            return mapper.userToDto(repository.save(newUser));
        } catch (DataIntegrityViolationException e) {
            throw new InvalidDataException("Duplicate email");
        }
    }

    @Transactional
    @Override
    public void delete(Long id) {
        cardRepository.findByUserId(id)
                        .forEach(cardRepository::delete);
        repository.delete(findUserById(id));
    }

    @Override
    public UserDtoResponse findById(Long id) {
        return mapper.userToDto(findUserById(id));
    }

    @Override
    public List<UserDtoResponse> findUsers(String firstname,
                                           String surname,
                                           int pageNumber,
                                           int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Specification<User> spec = UserSpecification.filterBy(firstname, surname);
        return repository.findAll(spec, pageable).stream()
                .map(mapper::userToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void activate(Long id) {
        if (repository.activateUser(id) == 0) {
            throw new EntityNotFoundException("User not found with id " + id);
        }
    }

    @Transactional
    @Override
    public void deactivate(Long id) {
        if (repository.deactivateUser(id) == 0) {
            throw new EntityNotFoundException("User not found with id " + id);
        }
    }

    private User findUserById(Long id) {
        return repository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("User not found with id " + id));
    }
}
