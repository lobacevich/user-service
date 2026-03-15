package by.lobacevich.repository;

import by.lobacevich.entity.PaymentCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PaymentCardRepository extends JpaRepository<PaymentCard, Long> {

    int countByUserId(Long userId);

    List<PaymentCard> findByUserId(Long id);

    @Modifying
    @Query(value = "UPDATE payment_cards SET active = true WHERE id = :id",
            nativeQuery = true)
    int activateCard(@Param("id") Long id);

    @Modifying
    @Query("UPDATE PaymentCard pc SET pc.active = false WHERE pc.id = :id")
    int deactivateCard(@Param("id") Long id);
}
