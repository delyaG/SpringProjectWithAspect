package ru.itis.repositories;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.itis.models.AuthData;
import ru.itis.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Component
public class UserRepositoryJdbcTemplateImpl implements UserRepository {

    private static final String SQL_SELECT_BY_ID = "select id, name, mail, password from users where id = ?";

    private static final String SQL_SELECT_ALL = "select id, name, mail, password from users";

    private static final String SQL_INSERT = "insert into users(name, mail, password, token, is_confirmed) values (?, ?, ?, ?, ?)";

    private static final String FIND_BY_TOKEN = "select * from users where token = ?";

    private static final String UPDATE_CONFIRMATION = "update users SET is_confirmed = ? where id = ?";

    private static final String FIND_BY_MAIL = "select * from users where mail = ?";

    private static final String FIND_BY_ID = "select * from users where id = ?";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PasswordEncoder encoder;

    private RowMapper<AuthData> authDataRowMapper = (row, rowNumber) ->
            AuthData.builder()
                .mail(row.getString("mail"))
                .password(row.getString("password"))
                .build();

    private RowMapper<User> userRowMapper = (row, rowNumber) ->
            User.builder()
                    .id(row.getLong("id"))
                    .name(row.getString("name"))
                    .authData(new AuthData(row.getString("mail"), row.getString("password")))
                    .isConfirmed(row.getBoolean("is_confirmed"))
                    .token(row.getString("token"))
                    .build();

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public User save(User entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection
                    .prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, entity.getName());
            statement.setString(2, entity.getAuthData().getMail());
            statement.setString(3, entity.getAuthData().getPassword());
            statement.setString(4, entity.getToken());
            statement.setBoolean(5, entity.isConfirmed());
            return statement;
        }, keyHolder);

        Object id = keyHolder.getKeyList().get(0).get("id");
        entity.setId(convertToLong(id));
        return entity;
    }

    private Long convertToLong(Object o){
        String stringToConvert = String.valueOf(o);
        return Long.parseLong(stringToConvert);
    }

    @Override
    public void delete(Long aLong) {

    }

    @Override
    public Optional<User> findByToken(String token) {
        User user = jdbcTemplate.queryForObject(FIND_BY_TOKEN, new Object[]{token}, userRowMapper);
        return Optional.ofNullable(user);
    }

    @Override
    public void updateConfirmation(User user) {
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(UPDATE_CONFIRMATION);
            statement.setBoolean(1, user.isConfirmed());
            statement.setLong(2, user.getId());
            return statement;
        });
    }

    @Override
    public Optional<User> find(Long id) {
        User user = jdbcTemplate.queryForObject(FIND_BY_ID, new Object[]{id}, userRowMapper);
        return Optional.ofNullable(user);
    }

    @Override
    public Optional<User> find(String mail, String password) {
        User user = jdbcTemplate.queryForObject(FIND_BY_MAIL, new Object[]{mail}, userRowMapper);
        if (isPasswordEqualUserPassword(password, user.getAuthData().getPassword())) {
            return Optional.of(user);
        }

        return Optional.empty();
    }

    private boolean isPasswordEqualUserPassword(String password, String userPassword) {
        return encoder.matches(password, userPassword);
    }
}
