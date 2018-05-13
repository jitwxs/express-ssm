package jit.wxs.express.realm;

import jit.wxs.express.enums.RoleEnum;
import jit.wxs.express.pojo.SysUser;
import jit.wxs.express.service.SysUserService;
import jit.wxs.express.utils.PasswordUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * 身份验证
 * @author jitwxs
 * @date 2018/1/2 19:24
 */
@Component
public class LoginRealm extends AuthorizingRealm {

    @Autowired
    private SysUserService userService;

    /**
     * 身份验证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        // 获取用户名和密码
        String userName = (String) token.getPrincipal();
        String password = new String((char[])token.getCredentials());

        SysUser sysUser = userService.getByUserName(userName);

        // 登录失败
        if (sysUser == null || !PasswordUtils.validatePassword(password, sysUser.getPassword())) {
            throw new IncorrectCredentialsException("登录失败，用户名或密码不正确！");
        }

        // 身份验证通过,返回一个身份信息
        return new SimpleAuthenticationInfo(userName,password,getName());
    }

    /**
     * 身份授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String userName = (String) getAvailablePrincipal(principalCollection);
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        SysUser sysUser = userService.getByUserName(userName);
        // 获取角色对象

        Integer roleId = sysUser.getRoleId();

        //通过用户名从数据库获取权限/角色信息
        Set<String> r = new HashSet<>();

        if (roleId != null) {
            r.add(RoleEnum.getName(roleId));
            info.setRoles(r);
        }

        return info;
    }
}
