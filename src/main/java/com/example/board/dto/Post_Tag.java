package com.example.board.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class Post_Tag {
    private long postId;
    private String name;
    private List<String> tags = new ArrayList<>();

    public Post_Tag(long postId,String name) {
        this.postId = postId;
        this.name = name;
    }

    public void addTags(String tagName) {
        tags.add(tagName);
    }
}
