package jit.wxs.express.service;

import jit.wxs.express.pojo.SysUser;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 职员表 服务类
 * </p>
 *
 * @author jitwxs
 * @since 2018-04-23
 */
public interface SysUserService extends IService<SysUser> {
    /**
     * 用户名是否存在。存在：true
     */
    boolean hasExistUserName(String userName);

    /**
     * 根据用户名查询
     */
    SysUser getByUserName(String userName);
}
