package com.example.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class WriteInfo {
    private int postId;
    private String title;
    private List<Tag> tags = new ArrayList<>();

    public WriteInfo(int postId, String title) {
        this.postId = postId;
        this.title = title;
    }

    public void addTag(Tag tagName) {
        tags.add(tagName);
    }
}
