package ru.itis.models;

import lombok.*;

@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    private Long id;
    private String name;
    private AuthData authData;
    private String token;
    private boolean isConfirmed;
}
