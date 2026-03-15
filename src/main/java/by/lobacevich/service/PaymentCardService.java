package by.lobacevich.service;

import by.lobacevich.dto.request.PayCardDtoRequest;
import by.lobacevich.dto.response.PayCardDtoResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PaymentCardService {

    @Transactional
    PayCardDtoResponse create(PayCardDtoRequest dto);

    @Transactional
    PayCardDtoResponse update(PayCardDtoRequest dto, Long id);

    @Transactional
    void delete(Long id);

    PayCardDtoResponse findById(Long id);

    List<PayCardDtoResponse> findCards(int pageNumber,
                                       int pageSize);

    List<PayCardDtoResponse> findCardsByUserId(Long id);

    @Transactional
    void activate(Long id);

    @Transactional
    void deactivate(Long id);
}
