package com.example.board.controller;

import com.example.board.dto.Comment;
import com.example.board.dto.LoginInfo;
import com.example.board.dto.Post;
import com.example.board.service.CommentService;
import com.example.board.service.PostService;
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
    private final PostService postService;

    @GetMapping("/detail")
    public String detail(@RequestParam("postId") int postId, HttpSession session, Model model) {
        LoginInfo loginInfo = (LoginInfo)session.getAttribute("loginInfo");
        model.addAttribute("loginInfo", loginInfo);
        
        // 게시글 정보 가져오기 (조회수 증가 안 함)
        Post post = postService.getPost(postId, false);
        model.addAttribute("post", post);
        
        // 댓글 목록 가져오기
        List<Comment> comments = commentService.getComments(postId);
        model.addAttribute("comments", comments);
        
        return "detail";
    }

    @PostMapping("/comment/add")
    public String addComment(
            @RequestParam("postId") int postId, 
            @RequestParam("content") String content, 
            @RequestParam(value = "parentId", required = false, defaultValue = "") String parentIdStr, 
            HttpSession session) {
        
        LoginInfo loginInfo = (LoginInfo)session.getAttribute("loginInfo");
        if(loginInfo == null) {
            return "redirect:/loginform";
        }
        
        Integer parentId = null;
        if(parentIdStr != null && !parentIdStr.isEmpty()) {
            try {
                parentId = Integer.parseInt(parentIdStr);
            } catch (NumberFormatException e) {
                parentId = null;
            }
        }
        
        commentService.addComment(postId, loginInfo.getUserId(), content, parentId);
        return "redirect:/detail?postId=" + postId;
    }

    @GetMapping("/comment/delete")
    public String deleteComment(
            @RequestParam("commentId") int commentId, 
            @RequestParam("postId") int postId, 
            HttpSession session) {
        
        LoginInfo loginInfo = (LoginInfo)session.getAttribute("loginInfo");
        if(loginInfo == null) {
            return "redirect:/loginform";
        }
        
        commentService.deleteComment(commentId, postId, loginInfo.getUserId());
        return "redirect:/detail?postId=" + postId;
    }

    @PostMapping("/comment/update")
    public String updateComment(
            @RequestParam("commentId") int commentId,
            @RequestParam("postId") int postId,
            @RequestParam("content") String content,
            HttpSession session) {
        
        LoginInfo loginInfo = (LoginInfo)session.getAttribute("loginInfo");
        if(loginInfo == null) {
            return "redirect:/loginform";
        }
        
        commentService.updateComment(commentId, content);
        return "redirect:/detail?postId=" + postId;
    }
}