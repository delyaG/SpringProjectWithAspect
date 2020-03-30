package ru.itis.config;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class Sender {
    private String username;
    private String password;
    private Properties properties;

    public Sender(String username, String password, Properties properties) {
        this.username = username;
        this.password = password;
        this.properties = properties;
    }

    public void send(String subject, String text, String toEmail) {
        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            //от кого
            message.setFrom(new InternetAddress(username));
            //кому
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            //Заголовок письма
            message.setSubject(subject);
            //Содержимое
            message.setText(text);
            message.setContent(text, "text/html");
            //Отправляем сообщение
            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
