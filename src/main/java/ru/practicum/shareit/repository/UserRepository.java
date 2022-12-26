package ru.practicum.shareit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.User;
import java.util.Optional;

@SuppressWarnings("checkstyle:Regexp")
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserById(Long id);

    Optional<User> findUserByEmail(String email);
}
