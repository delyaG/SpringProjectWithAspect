package ru.itis.services;

import ru.itis.models.User;

public interface MailService {
    void sendMail(User user, String title, String fileName);
    void sendMail(User user, String title, String fileName, String storageFileName);
}