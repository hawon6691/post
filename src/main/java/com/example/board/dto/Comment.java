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
public class Comment {
    private int commentId;
    private int postId;
    private int userId;
    private Integer parentId;
    private String name;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
