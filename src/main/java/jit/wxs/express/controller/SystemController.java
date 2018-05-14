package jit.wxs.express.controller;

import jit.wxs.express.enums.RoleEnum;
import jit.wxs.express.pojo.Express;
import jit.wxs.express.pojo.Feedback;
import jit.wxs.express.pojo.SysUser;
import jit.wxs.express.pojo.interactive.Msg;
import jit.wxs.express.service.FeedbackService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * @author jitwxs
 * @date 2018/5/1 23:34
 */
@RestController
public class SystemController {
    @Autowired
    private FeedbackService feedbackService;

    @Value("${session.latest_express}")
    private String SESSION_LATEST_EXPRESS;

    /**
     * 验证验证码
     * @author jitwxs
     * @since 2018/5/2 0:02
     */
    @PostMapping("/checkVerifyCode")
    public Msg checkVerifyCode(String data, HttpServletRequest request) {
        String validateCode = ((String) request.getSession().getAttribute("validateCode")).toLowerCase();
        String value = data.toLowerCase();

        if(!validateCode.equals(value)) {
            return Msg.error("验证码错误");
        } else {
            return Msg.ok();
        }
    }

    /**
     * 登陆
     * @author jitwxs
     * @since 2018/5/2 0:02
     */
    @PostMapping("/login")
    public Msg login(SysUser user) {
        //Shiro实现登录
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());
        Subject subject = SecurityUtils.getSubject();

        try {
            //如果获取不到用户名就是登录失败，但登录失败的话，会直接抛出异常
            subject.login(token);
        } catch (Exception e) {
            return Msg.error("用户名或密码错误");
        }

        //所有用户均重定向对应的展示配送页面
        if (subject.hasRole(RoleEnum.ADMIN.getName())) {
            return Msg.ok(null,"/admin/express");
        } else if (subject.hasRole(RoleEnum.STAFF.getName())) {
            return Msg.ok(null,"/staff/express");
        }

        return Msg.error("授权失败");
    }

    /**
     * 获取用户名
     * @author jitwxs
     * @since 2018/5/13 19:24
     */
    @GetMapping("/username")
    public Msg selfName() {
        String username = (String)SecurityUtils.getSubject().getPrincipal();

        return Msg.ok(null, username);
    }

    /**
     * 登出
     * @author jitwxs
     * @since 2018/5/13 22:45
     */
    @RequestMapping(value = "/logout")
    public String logout() {
        return "redirect:/logout";
    }

    /**
     * 提交反馈
     * @author jitwxs
     * @since 2018/5/1 23:34
     */
    @PostMapping("/feedback")
    public Msg feedback(Feedback feedback) {
        feedback.setCreateDate(new Date());
        feedbackService.insert(feedback);

        return Msg.ok(null,feedback.getId());
    }

    /**
     * 保存用户输入的订单信息到Cookie
     * @author jitwxs
     * @since 2018/5/1 23:49
     */
    @PostMapping("/express")
    public Msg express(Express express, HttpSession session) {
        express.setCreateDate(new Date());

        // 将订单信息保存到用户Session中
        session.setAttribute(SESSION_LATEST_EXPRESS,express);

        // 保存成功后跳转到支付页面
        return Msg.ok(null, "/payment");
    }
}
