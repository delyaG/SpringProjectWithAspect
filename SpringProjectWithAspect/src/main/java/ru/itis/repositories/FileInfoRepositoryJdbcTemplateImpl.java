package ru.itis.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.itis.models.FileInfo;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Component
public class FileInfoRepositoryJdbcTemplateImpl implements FileInfoRepository {

    private static final String SQL_INSERT = "insert into file_storage_info(original_file_name, storage_file_name, size, type, url, user_id) values (?, ?, ?, ?, ?, ?)";

    private static final String FIND_BY_ID = "select * from file_storage_info where id = ?";

    private static final String FIND_BY_FILE_NAME = "select * from file_storage_info where storage_file_name = ?";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<FileInfo> fileInfoRowMapper = (row, rowNumber) ->
        FileInfo.builder()
                .id(row.getLong("id"))
                .originalFileName(row.getString("original_file_name"))
                .storageFileName(row.getString("storage_file_name"))
                .size(row.getLong("size"))
                .type(row.getString("type"))
                .url(row.getString("url"))
                .userId(row.getLong("user_id"))
                .build();

    @Override
    public Optional<FileInfo> find(Long id) {
        FileInfo fileInfo = jdbcTemplate.queryForObject(FIND_BY_ID, new Object[]{id}, fileInfoRowMapper);
        return Optional.ofNullable(fileInfo);
    }

    @Override
    public Optional<FileInfo> findByStorageName(String name) {
        FileInfo fileInfo = jdbcTemplate.queryForObject(FIND_BY_FILE_NAME, new Object[]{name}, fileInfoRowMapper);
        return Optional.ofNullable(fileInfo);
    }

    @Override
    public List<FileInfo> findAll() {
        return null;
    }

    @Override
    public FileInfo save(FileInfo entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection
                    .prepareStatement(SQL_INSERT);
            statement.setString(1, entity.getOriginalFileName());
            statement.setString(2, entity.getStorageFileName());
            statement.setLong(3, entity.getSize());
            statement.setString(4, entity.getType());
            statement.setString(5, entity.getUrl());
            statement.setLong(6, entity.getUserId());
            return statement;
        }, keyHolder);
        entity.setId((Long) keyHolder.getKey());
        System.out.println();
        return entity;
    }

    @Override
    public void delete(Long aLong) {

    }
}
