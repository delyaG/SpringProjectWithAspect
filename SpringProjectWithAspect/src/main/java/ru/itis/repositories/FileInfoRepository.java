package ru.itis.repositories;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.itis.models.FileInfo;

import java.util.List;
import java.util.Optional;

public interface FileInfoRepository extends CrudRepository<Long, FileInfo> {
    Optional<FileInfo> findByStorageName(String storageName);
}
