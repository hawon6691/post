package com.example.board.controller;

import com.example.board.dto.LoginInfo;
import com.example.board.dto.Post;
import com.example.board.service.PostService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping("/")
    public String PostList(@RequestParam(name = "page", defaultValue = "1") int page, HttpSession session, Model model) {
        LoginInfo loginInfo = (LoginInfo)session.getAttribute("loginInfo");
        model.addAttribute("loginInfo", loginInfo);

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

    @GetMapping("/post")
    public String post(@RequestParam("postId") int postId, HttpSession httpSession, Model model) {
        LoginInfo loginInfo = (LoginInfo)httpSession.getAttribute("loginInfo");
        model.addAttribute("loginInfo", loginInfo);

        System.out.println("postId : " + postId);

        Post post = postService.getPost(postId);
        model.addAttribute("post", post);

        return "post";
    }

    private String loginCheck(HttpSession httpSession) {
        LoginInfo loginInfo = (LoginInfo)httpSession.getAttribute("loginInfo");
        if(loginInfo == null) {
            return "redirect:/loginform";
        }
        return null;
    }

    @GetMapping("/writeForm")
    public String writeForm(HttpSession httpSession, Model model) {
        LoginInfo loginInfo = (LoginInfo)httpSession.getAttribute("loginInfo");
        if(loginInfo == null) {
            return "redirect:/loginform";
        }

//        return loginCheck(httpSession);

        model.addAttribute("loginInfo", loginInfo);

        return "writeForm";
    }

    @PostMapping("/write")
    public String write(@RequestParam("title") String title, @RequestParam("content") String content, @RequestParam("is_public") boolean is_public, HttpSession httpSession) {
        System.out.println("title : " + title);
        System.out.println("content : " + content);

        LoginInfo loginInfo = (LoginInfo)httpSession.getAttribute("loginInfo");
        if(loginInfo == null) {
            return "redirect:/loginform";
        }

        postService.addPost(loginInfo.getUserId(), title, content, is_public);

        return "redirect:/";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("postId") int postId, HttpSession httpSession) {
        LoginInfo loginInfo = (LoginInfo)httpSession.getAttribute("loginInfo");
        if(loginInfo == null) {
            return "redirect:/loginform";
        }

        List<String> roles = loginInfo.getRoles();
        if(roles.contains("ROLE_ADMIN")) {
            postService.deletePost(postId);
        } else {
            postService.deletePost(loginInfo.getUserId(), postId);
        }

        return "redirect:/";
    }

    @GetMapping("/updateform")
    public String updateform(@RequestParam("postId") int postId, Model model, HttpSession session) {
        LoginInfo loginInfo = (LoginInfo)session.getAttribute("loginInfo");
        if(loginInfo == null) return "redirect:/loginform";

        Post post = postService.getPost(postId, false);
        model.addAttribute("post", post);
        model.addAttribute("loginInfo", loginInfo);
        return "updateform";
    }

    @PostMapping("/update")
    public String update(@RequestParam("postId") int postId, @RequestParam("title") String title, @RequestParam("content") String content, @RequestParam("is_public") boolean is_public, HttpSession session) {
        LoginInfo loginInfo = (LoginInfo)session.getAttribute("loginInfo");
        if(loginInfo == null) return "redirect:/loginform";

        Post post = postService.getPost(postId, false);
        if(post.getUserId() != loginInfo.getUserId()) {
            return "redirect:/board?boardId=" + postId;
        }

        postService.updatePost(postId, title, content, is_public);
        return "redirect:/post?postId=" + postId;
    }
}
