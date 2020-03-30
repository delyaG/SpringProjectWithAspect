package ru.itis.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.models.User;
import ru.itis.repositories.UserRepository;

import java.util.Optional;

@Service
public class SignInServiceImpl implements SignInService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<User> signIn(String mail, String password) {
        return userRepository.find(mail, password);
    }
}
