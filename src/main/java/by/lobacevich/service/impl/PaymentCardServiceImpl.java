package by.lobacevich.service.impl;

import by.lobacevich.dto.request.PayCardDtoRequest;
import by.lobacevich.dto.response.PayCardDtoResponse;
import by.lobacevich.entity.PaymentCard;
import by.lobacevich.entity.User;
import by.lobacevich.exception.CardsLimitException;
import by.lobacevich.exception.EntityNotFoundException;
import by.lobacevich.exception.InvalidDataException;
import by.lobacevich.mapper.PaymentCardMapper;
import by.lobacevich.repository.PaymentCardRepository;
import by.lobacevich.repository.UserRepository;
import by.lobacevich.service.PaymentCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentCardServiceImpl implements PaymentCardService {

    private final PaymentCardRepository repository;
    private final PaymentCardMapper mapper;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public PayCardDtoResponse create(PayCardDtoRequest dto) {
        Long userId = dto.userId();
        User user = findUserById(userId);
        if (repository.countByUserId(userId) > 4) {
            throw new CardsLimitException("User with id" + userId + " can not have more then 5 cards");
        }
        PaymentCard card = mapper.dtoToEntity(dto);
        card.setUser(user);
        try {
            repository.save(card);
            return mapper.entityToDto(findCardById(card.getId()));
        } catch (DataIntegrityViolationException e) {
            throw new InvalidDataException("Duplicate card number");
        }
    }

    @Transactional
    @Override
    public PayCardDtoResponse update(PayCardDtoRequest dto, Long id) {
        PaymentCard oldCard = findCardById(id);
        PaymentCard newCard = mapper.dtoToEntity(dto);
        newCard.setUser(findUserById(id));
        newCard.setActive(oldCard.getActive());
        newCard.setCreatedAt(oldCard.getCreatedAt());
        try {
            return mapper.entityToDto(repository.save(newCard));
        } catch (DataIntegrityViolationException e) {
            throw new InvalidDataException("Duplicate card number");
        }
    }

    @Transactional
    @Override
    public void delete(Long id) {
        repository.delete(findCardById(id));
    }

    @Override
    public PayCardDtoResponse findById(Long id) {
        return mapper.entityToDto(findCardById(id));
    }

    @Override
    public List<PayCardDtoResponse> findCards(int pageNumber,
                                              int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return repository.findAll(pageable).stream()
                .map(mapper::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PayCardDtoResponse> findCardsByUserId(Long id) {
        return repository.findByUserId(id).stream()
                .map(mapper::entityToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void activate(Long id) {
        if (repository.activateCard(id) == 0) {
            throw new EntityNotFoundException("Card not found with id " + id);
        }
    }

    @Transactional
    @Override
    public void deactivate(Long id) {
        if (repository.deactivateCard(id) == 0) {
            throw new EntityNotFoundException("Card not found with id " + id);
        }
    }

    private PaymentCard findCardById(Long id) {
        return repository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Card not found with id " + id));
    }

    private User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("User not found with id " + id));
    }
}
