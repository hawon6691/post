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
public class post {
    private long postId;
    private long userId;
    private String title;
    private String content;
    private int view_count;
    private boolean is_public;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}
