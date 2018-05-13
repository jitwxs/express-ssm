package jit.wxs.express.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import jit.wxs.express.pojo.SysUser;
import jit.wxs.express.mapper.SysUserMapper;
import jit.wxs.express.service.SysUserService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 职员表 服务实现类
 * </p>
 *
 * @author jitwxs
 * @since 2018-04-23
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    @Autowired
    private SysUserMapper userMapper;

    @Override
    public boolean hasExistUserName(String userName) {
        List<SysUser> list = userMapper.selectList(new EntityWrapper<SysUser>().eq("username", userName));

        return list == null || list.size() == 0;
    }

    @Override
    public SysUser getByUserName(String userName) {
        List<SysUser> list = userMapper.selectList(new EntityWrapper<SysUser>().eq("username", userName));
        if(list == null || list.size() == 0) {
            return null;
        } else {
            return list.get(0);
        }
    }
}
