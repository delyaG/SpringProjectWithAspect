package ru.itis.services;

public interface SignUpService {
    void signUp(String name, String mail, String password);
    void setConfirmation(String token);
}
