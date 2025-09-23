package com.example.board.controller;

import com.example.board.dto.User_Role;
import com.example.board.dto.Post;
import com.example.board.service.PostService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping("/")
    public String PostList(@RequestParam(name = "page", defaultValue = "1") int page, HttpSession session, Model model) {
        User_Role user_role = (User_Role)session.getAttribute("user_role");
        model.addAttribute("user_role", user_role);

        int totalCount = postService.getTotalCount();
        List<Post> list = postService.getPosts(page);
        int pageCount = totalCount / 10;
        if (totalCount % 10 > 0) {
            pageCount++;
        }
        int currentPage = page;

        model.addAttribute("list", list);
        model.addAttribute("pageCount", pageCount);
        model.addAttribute("currentPage", currentPage);

        return "list";
    }
}
