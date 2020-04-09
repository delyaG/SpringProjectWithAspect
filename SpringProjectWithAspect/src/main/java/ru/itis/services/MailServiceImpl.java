package ru.itis.services;

import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.config.Sender;
import ru.itis.models.User;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Service
public class MailServiceImpl implements MailService {

    @Autowired
    private Sender sender;

    @Override
    public void sendMail(User user, String title, String fileName) {
        sendMail(user, title, fileName, null);
    }

    @Override
    public void sendMail(User user, String title, String fileName, String storageFileName) {
        String receiverMail = user.getAuthData().getMail();
        String message  = getMessageAfterReadingMessageFile(user, fileName, storageFileName);
        sender.send(title, message, receiverMail);
    }

    private String getMessageAfterReadingMessageFile(User user, String fileName, String storageFileName) {
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

        switch (fileName) {
            case "message.ftl":
                map.put("token", user.getToken());
                break;
            case "notifyMessage.ftl":
                map.put("fileName", storageFileName);
                break;
        }
        try {
            Template template = configuration.getTemplate(fileName);
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
}
