package ru.itis.repositories;

import ru.itis.models.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<Long, User> {
    Optional<User> findByToken(String token);
    void updateConfirmation(User user);
    Optional<User> find(String mail, String password);
}
