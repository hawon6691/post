package com.example.board.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class comment {
    private long commentId;
    private long postId;
    private long userId;
    private String content;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}
