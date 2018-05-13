package jit.wxs.express.controller;

import jit.wxs.express.enums.RoleEnum;
import jit.wxs.express.pojo.SysUser;
import jit.wxs.express.pojo.interactive.Msg;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 登陆/注册Controller
 * @author jitwxs
 * @date 2018/4/23 10:04
 */
@RestController
public class SSOController {
    @Value("${server.host}")
    private String SERVER_HOST;

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

        //如果获取不到用户名就是登录失败，但登录失败的话，会直接抛出异常
        subject.login(token);

        //所有用户均重定向对应的展示配送页面
        if (subject.hasRole(RoleEnum.ADMIN.getName())) {
            return Msg.ok(null,"/admin/express");
        } else if (subject.hasRole(RoleEnum.STAFF.getName())) {
            return Msg.ok(null,"/staff/express");
        }

        return Msg.error("授权失败");
    }
}
