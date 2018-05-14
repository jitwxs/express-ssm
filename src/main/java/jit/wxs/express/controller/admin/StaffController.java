package jit.wxs.express.controller.admin;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import jit.wxs.express.dto.SysUserDto;
import jit.wxs.express.enums.RoleEnum;
import jit.wxs.express.enums.SysUserStatusEnum;
import jit.wxs.express.pojo.SysUser;
import jit.wxs.express.pojo.interactive.Msg;
import jit.wxs.express.pojo.interactive.SysUserSelectWrapper;
import jit.wxs.express.service.SysUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
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

    private List<SysUserDto> sysUser2dto(List<SysUser> userList) {
        List<SysUserDto> result = new ArrayList<>();
        for(SysUser user : userList) {
            result.add(sysUser2dto(user));
        }
        return result;
    }

    private SysUserDto sysUser2dto(SysUser user) {
        SysUserDto dto = new SysUserDto();
        BeanUtils.copyProperties(user,dto);

        // 设置角色名称
        String roleName = RoleEnum.getName(dto.getRoleId());
        dto.setRoleName(roleName);

        // 设置用户状态名称
        String statusName = SysUserStatusEnum.getName(dto.getStatus());
        dto.setStatusName(statusName);

        return dto;
    }

    /**
     * 构造筛选条件
     * @author jitwxs
     * @since 2018/5/13 15:50
     */
    private EntityWrapper<SysUser> getSysUserWrapper(SysUserSelectWrapper usw) {
        EntityWrapper<SysUser> entityWrapper = new EntityWrapper<>();

        // 前台传递，状态-1表示所有状态
        if(usw.getStatus() != null && usw.getStatus() != -1) {
            entityWrapper.eq("status", usw.getStatus());
        }
        if(StringUtils.isNotBlank(usw.getTel())){
            entityWrapper.eq("tel", usw.getTel());
        }
        if(StringUtils.isNotBlank(usw.getSex())) {
            entityWrapper.eq("sex", usw.getSex());
        }
        if(StringUtils.isNotBlank(usw.getName())){
            entityWrapper.like("username", usw.getName());
        }
        if(StringUtils.isNotBlank(usw.getAddress())){
            entityWrapper.like("address", usw.getAddress());
        }
        return entityWrapper;
    }

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
            String name = usw.getName();
            if(StringUtils.isNotBlank(name)) {
                usw.setName(new String(name.getBytes("iso8859-1"),"utf-8"));
            }
            String address = usw.getAddress();
            if(StringUtils.isNotBlank(address)) {
                usw.setAddress(new String(address.getBytes("iso8859-1"),"utf-8"));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 得到筛选条件
        EntityWrapper<SysUser> userWrapper = getSysUserWrapper(usw);
        // 不显示admin角色
        userWrapper.ne("role_id", RoleEnum.ADMIN.getIndex());
        Page<SysUser> selectPage = userService.selectPage(new Page<>(page, rows), userWrapper);

        List<SysUserDto> list = sysUser2dto(selectPage.getRecords());

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
