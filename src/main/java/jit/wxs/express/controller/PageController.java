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

    @GetMapping("/search")
    public String showSearch() {
        return "search";
    }

    @GetMapping("/payment")
    public String showPayment() {
        return "payment";
    }

    @GetMapping("/payment/result")
    public String showPaymentResult() {
        return "payment_result";
    }

    @GetMapping("/admin/express")
    public String showExpress() {
        return "admin/express";
    }

    @GetMapping("/admin/expressRecycle")
    public String showExpressRecycle() {
        return "admin/expressRecycle";
    }

    @GetMapping("/admin/staff")
    public String showStaff() {
        return "admin/staff";
    }

    @GetMapping("/admin/feedback")
    public String showFeedback() {
        return "admin/feedback";
    }

    @GetMapping("/admin/password")
    public String showPassword() {
        return "admin/password";
    }
}