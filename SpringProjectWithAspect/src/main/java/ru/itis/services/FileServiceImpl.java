package ru.itis.services;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.models.FileInfo;
import ru.itis.repositories.FileInfoRepository;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private FileInfoRepository fileInfoRepository;

    @Autowired
    private String storagePath;

    @Override
    public void writeFileToResponse(String fileName, HttpServletResponse response) {
        Optional<FileInfo> file = fileInfoRepository.findByStorageName(fileName);
        if (file.isPresent()) {
            FileInfo fileInfo = file.get();
            response.setContentType(fileInfo.getType());
            try {
                InputStream inputStream = new FileInputStream(new java.io.File(fileInfo.getUrl()));
                IOUtils.copy(inputStream, response.getOutputStream());
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }

    @Override
    public FileInfo save(MultipartFile multipartFile, Long ownerId) {
        String storageFileName = createStorageFileName(multipartFile.getOriginalFilename());
        FileInfo file = FileInfo.builder()
                .originalFileName(multipartFile.getOriginalFilename())
                .storageFileName(storageFileName)
                .size(multipartFile.getSize())
                .type(multipartFile.getContentType())
                .url(storagePath + "/" + storageFileName)
                .userId(ownerId)
                .build();

        fileInfoRepository.save(file);
        System.out.println(3);

        try {
            long copy = Files.copy(multipartFile.getInputStream(), Paths.get(storagePath, storageFileName));
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }

        return file;
    }

    private String createStorageFileName(String originalFileName) {
        String extension = FilenameUtils.getExtension(originalFileName);
        return UUID.randomUUID().toString() + "." + extension;
    }
}
