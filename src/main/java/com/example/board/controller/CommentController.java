package com.example.board.controller;

import com.example.board.dto.Comment;
import com.example.board.service.CommentService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/detail")
    public String list(@RequestParam("postId") int postId, HttpSession session, Model model) {
        List<Comment> list = commentService.getComments(postId);
        model.addAttribute("list", list);
        return "detail";
    }
}
