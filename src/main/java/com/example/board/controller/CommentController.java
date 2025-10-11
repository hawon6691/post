package com.example.board.controller;

import com.example.board.dto.Comment;
import com.example.board.dto.LoginInfo;
import com.example.board.service.CommentService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/detail")
    public String getComments(@RequestParam("postId") int postId, Model model) {
        List<Comment> commentlist = commentService.getComments(postId);
        model.addAttribute("list", commentlist);
        return "detail";
    }

    @PostMapping("/addComment")
    public String addComment(@RequestParam("postId") int postId, @RequestParam("content") String content, @RequestParam(value = "parentId", required = false) Integer parentId, HttpSession session) {
        LoginInfo loginInfo = (LoginInfo)session.getAttribute("loginInfo");
        if(loginInfo == null) return "redirect:/loginform";
        commentService.addComment(postId, loginInfo.getUserId(), content, parentId);
        return "redirect:/detail?postId=" + postId;
    }

    @PostMapping("/deleteComment")
    public String deleteComment(@RequestParam("commentId") int commentId, @RequestParam("postId") int postId, HttpSession session) {
        LoginInfo loginInfo = (LoginInfo)session.getAttribute("loginInfo");
        if(loginInfo == null) return "redirect:/loginform";
        commentService.deleteComment(commentId, loginInfo.getUserId());
        return "redirect:/detail?postId=" + postId;
    }
}
