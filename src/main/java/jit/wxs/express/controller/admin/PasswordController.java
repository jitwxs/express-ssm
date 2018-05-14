package jit.wxs.express.controller.admin;

import jit.wxs.express.pojo.SysUser;
import jit.wxs.express.pojo.interactive.Msg;
import jit.wxs.express.service.SysUserService;
import jit.wxs.express.utils.PasswordUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 密码Controller
 * @author jitwxs
 * @since 2018/5/14 15:33
 */
@RestController
@RequestMapping("/password")
public class PasswordController {
    @Autowired
    private SysUserService userService;

    /**
     * 重置密码
     * @author jitwxs
     * @since 2018/5/14 15:49
     */
    @PostMapping("/reset")
    public Msg resetPassword(String oldPassword, String newPassword) {
        String username = (String)SecurityUtils.getSubject().getPrincipal();
        SysUser user = userService.getByUserName(username);

        if(!PasswordUtils.validatePassword(oldPassword, user.getPassword())) {
            return Msg.error("原始密码错误");
        } else {
            user.setPassword(PasswordUtils.entryptPassword(newPassword));
            userService.updateById(user);
            return Msg.ok();
        }

    }

}
