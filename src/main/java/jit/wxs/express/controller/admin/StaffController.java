package jit.wxs.express.controller.admin;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import jit.wxs.express.enums.RoleEnum;
import jit.wxs.express.pojo.SysUser;
import jit.wxs.express.pojo.interactive.Msg;
import jit.wxs.express.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 职员管理
 * @author jitwxs
 * @date 2018/5/2 16:49
 */
@RestController
@RequestMapping("/admin/staff")
public class StaffController {
    @Autowired
    private SysUserService userService;
    /**
     * 获取所有职员
     * @author jitwxs
     * @since 2018/5/2 16:50
     */
    @GetMapping("/")
    public Msg listStaff() {
        List<SysUser> users = userService.selectList(new EntityWrapper<SysUser>().eq("role_id", RoleEnum.STAFF.getIndex()));

        return Msg.ok(null,users);
    }

}
