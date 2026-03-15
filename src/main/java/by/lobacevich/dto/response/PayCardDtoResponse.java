package by.lobacevich.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record PayCardDtoResponse(Long id,
                                 String number,
                                 String holder,
                                 LocalDate expirationDate,
                                 Boolean active,
                                 LocalDateTime createdAt,
                                 LocalDateTime updatedAt) {
}
