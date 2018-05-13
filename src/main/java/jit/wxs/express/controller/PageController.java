package jit.wxs.express.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 页面跳转Controller
 * @author jitwxs
 * @date 2018/4/23 10:39
 */
@Controller
public class PageController {
    @GetMapping("/")
    public String showIndex() {
        return "index";
    }

    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    @GetMapping("/feedback")
    public String showFeedBack() {
        return "feedback";
    }

    @GetMapping("/admin/express")
    public String showExpress() {
        return "admin/express";
    }
}