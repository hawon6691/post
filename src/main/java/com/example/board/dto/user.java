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
public class user {
    private long userId;
    private String username;
    private String email;
    private String password;
    private String profile_image;
    private LocalDateTime created_at;
    private LocalDateTime last_login;
}
