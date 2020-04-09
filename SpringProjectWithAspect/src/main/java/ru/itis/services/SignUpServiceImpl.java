package ru.itis.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import ru.itis.models.AuthData;
import ru.itis.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.repositories.UserRepository;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Optional;

@Service
public class SignUpServiceImpl implements SignUpService {

    private static final SecureRandom secureRandom = new SecureRandom();

    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public void signUp(String name, String mail, String password) {

        String hashPassword = encoder.encode(password);

        AuthData authData = AuthData.builder()
                .mail(mail)
                .password(hashPassword)
                .build();

        User user = User.builder()
                .name(name)
                .authData(authData)
                .token(createToken())
                .isConfirmed(false)
                .build();

        User savedUser = userRepository.save(user);
        sendConfirmation(savedUser);
    }

    private void sendConfirmation(User user) {
        String title = "Confirmation";
        String fileName = "message.ftl";
        mailService.sendMail(user, title, fileName);
    }

    private String createToken() {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }

    @Override
    public void setConfirmation(String token) {
        Optional<User> userByToken = userRepository.findByToken(token);
        if (userByToken.isPresent()) {
            User user = userByToken.get();
            user.setConfirmed(true);
            userRepository.updateConfirmation(user);
        }
    }
}
