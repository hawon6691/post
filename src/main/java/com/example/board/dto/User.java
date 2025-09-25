package com.example.board.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class User {
    private int userId;
    private String name;
    private String email;
    private String password;
    private String profileImage;
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;
}
