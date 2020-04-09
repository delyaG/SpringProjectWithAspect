package ru.itis.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;
import ru.itis.models.FileInfo;
import ru.itis.models.User;
import ru.itis.repositories.UserRepository;
import ru.itis.services.MailService;

import java.util.Optional;

@Aspect
@Component
@EnableAspectJAutoProxy
public class UploadFileAspect {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MailService mailService;

    @Autowired
    private UserRepository userRepository;

    @Pointcut("execution(public * ru.itis.services.FileServiceImpl.save(..))")
    public void callAtFileService() {}

    @AfterReturning(pointcut = "callAtFileService()", returning = "file")
    public void afterCallMethodSave(JoinPoint joinPoint, FileInfo file) {
        Optional<User> user = userRepository.find(file.getUserId());
        logger.info("after save " + file);

        if (user.isPresent()) {
            String title = "Notify";
            String fileName = "notifyMessage.ftl";
            mailService.sendMail(user.get(), title, fileName, file.getStorageFileName());
        }

        System.out.println(joinPoint);
    }
}

