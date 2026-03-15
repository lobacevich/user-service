package by.lobacevich.repository;

import by.lobacevich.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long>,
        JpaSpecificationExecutor<User> {

    @Modifying
    @Query(value = "UPDATE users SET active = true WHERE id = :id",
            nativeQuery = true)
    int activateUser(@Param("id") Long id);

    @Modifying
    @Query("UPDATE User u SET u.active = false WHERE u.id = :id")
    int deactivateUser(@Param("id") Long id);
}
