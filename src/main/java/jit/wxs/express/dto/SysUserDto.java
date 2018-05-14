package jit.wxs.express.dto;

import jit.wxs.express.pojo.SysUser;

/**
 * @author jitwxs
 * @since 2018/5/13 20:01
 */
public class SysUserDto extends SysUser {
    private String roleName;

    private String statusName;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
}
