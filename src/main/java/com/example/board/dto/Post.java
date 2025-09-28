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
public class Post {
    private int postId;
    private int userId;
    private String title;
    private String content;
    private int viewCount;
    private String name;
    private Boolean isPublic;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
