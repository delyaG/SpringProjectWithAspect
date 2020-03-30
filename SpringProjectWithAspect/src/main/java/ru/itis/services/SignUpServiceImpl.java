package ru.itis.services;

import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.itis.config.Sender;
import ru.itis.models.AuthData;
import ru.itis.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.repositories.UserRepository;

import java.io.*;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class SignUpServiceImpl implements SignUpService {

    private static final SecureRandom secureRandom = new SecureRandom();

    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Sender sender;

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
        String receiverMail = user.getAuthData().getMail();
        String message  = getMessageAfterReadingMessageFile(user);
        sender.send(title, message, receiverMail);
    }

    private String getMessageAfterReadingMessageFile(User user) {
        String message = "";
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_29);
        FileTemplateLoader fileTemplateLoader = null;
        try {
            fileTemplateLoader = new FileTemplateLoader(new File("C:\\Users\\garie\\Desktop\\JavaLab\\SpringProjectWithAspect\\src\\main\\web\\WEB-INF\\template"));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        configuration.setTemplateLoader(fileTemplateLoader);
        Map<String, Object> map = new HashMap<>();
        map.put("token", user.getToken());
        try {
            Template template = configuration.getTemplate("message.ftl");
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Writer writer = new OutputStreamWriter(outputStream);
            template.process(map, writer);
            byte[] array = outputStream.toByteArray();

            for (byte ch: array) {
                message += (char)ch;
            }
            System.out.println(message);
            return message;
        } catch (TemplateException | IOException e) {
            throw new IllegalStateException(e);
        }
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
