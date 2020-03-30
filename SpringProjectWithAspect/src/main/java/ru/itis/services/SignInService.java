package ru.itis.services;

import ru.itis.models.User;

import java.util.Optional;

public interface SignInService {
    Optional<User> signIn(String mail, String password);
}
