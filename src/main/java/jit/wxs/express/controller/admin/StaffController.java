package jit.wxs.express.controller.admin;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import jit.wxs.express.controller.GlobalFunction;
import jit.wxs.express.dto.SysUserDto;
import jit.wxs.express.enums.RoleEnum;
import jit.wxs.express.enums.SysUserStatusEnum;
import jit.wxs.express.interactive.Msg;
import jit.wxs.express.interactive.SysUserSelectWrapper;
import jit.wxs.express.pojo.SysUser;
import jit.wxs.express.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private GlobalFunction globalFunction;

    private void changeUserStatus(String[] ids, Integer status) {
        for(String id : ids) {
            SysUser user = userService.selectById(id);
            user.setStatus(status);
            userService.updateById(user);
        }
    }

    /**
     * 获取用户的状态列表
     * @author jitwxs
     * @since 2018/5/2 9:58
     */
    @GetMapping("/status")
    public Msg listStaffStatus() {
        List<Map<String,Object>> result = new ArrayList<>();
        for(SysUserStatusEnum enums :SysUserStatusEnum.values()) {
            Map<String,Object> map = new HashMap<>();
            map.put("id",enums.getIndex());
            map.put("name",enums.getName());
            result.add(map);
        }
        return Msg.ok(null,result);
    }

    /**
     * 获取所有的职员名,用于分配订单
     * @author jitwxs
     * @since 2018/5/14 13:38
     */
    @GetMapping("/listName")
    public Msg listStaff() {
        // 获取所有在职的职员
        List<SysUser> staffs = userService.selectList(new EntityWrapper<SysUser>()
            .eq("status", SysUserStatusEnum.ACTIVE.getIndex())
            .eq("role_id", RoleEnum.STAFF.getIndex()));

        return Msg.ok(null,staffs);
    }

    /**
     * 获取所有职员
     * @author jitwxs
     * @since 2018/5/2 16:50
     */
    @GetMapping("/list")
    public Map listStaff(Integer rows, Integer page, SysUserSelectWrapper usw) {
        // Get请求中文编码
        try {
            usw.setName(globalFunction.iso8859ToUtf8(usw.getName()));
            usw.setAddress(globalFunction.iso8859ToUtf8(usw.getAddress()));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 得到筛选条件
        EntityWrapper<SysUser> userWrapper = globalFunction.getSysUserWrapper(usw);
        // 不显示admin角色
        userWrapper.ne("role_id", RoleEnum.ADMIN.getIndex());
        Page<SysUser> selectPage = userService.selectPage(new Page<>(page, rows), userWrapper);

        List<SysUserDto> list = globalFunction.sysUser2dto(selectPage.getRecords());

        Map<String,Object> map = new HashMap<>();
        map.put("total", selectPage.getTotal());
        map.put("rows", list);

        return map;
    }

    /**
     * 更新用户信息
     */
    @PostMapping("")
    public Msg update(SysUser user) {
        userService.updateById(user);
        return Msg.ok();
    }

    /**
     * 获取用户信息
     * @author jitwxs
     * @since 2018/5/14 16:15
     */
    @GetMapping("/{id}")
    public Msg getById(@PathVariable String id) {
        SysUser user = userService.selectById(id);
        return Msg.ok(null,user);
    }

    /**
     * 修改员工为在职
     * @author jitwxs
     * @since 2018/5/13 20:42
     */
    @PostMapping("/active")
    public Msg changeActive(String[] ids) {
        changeUserStatus(ids, SysUserStatusEnum.ACTIVE.getIndex());
        return Msg.ok();
    }

    /**
     * 修改员工为冻结
     * @author jitwxs
     * @since 2018/5/13 20:42
     */
    @PostMapping("/freeze")
    public Msg changeFreeze(String[] ids) {
        changeUserStatus(ids, SysUserStatusEnum.FREEZE.getIndex());
        return Msg.ok();
    }

    /**
     * 修改员工为离职
     * @author jitwxs
     * @since 2018/5/13 20:42
     */
    @PostMapping("/leave")
    public Msg changeLeave(String[] ids) {
        changeUserStatus(ids, SysUserStatusEnum.LEAVE.getIndex());
        return Msg.ok();
    }

}
