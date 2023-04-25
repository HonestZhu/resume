package com.wpen.auth.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.realm.AuthorizingRealm;
import org.springframework.beans.factory.annotation.Autowired;

public class AccountRealm extends AuthorizingRealm {

    @Autowired
    private AccountService accountService;

    // 覆盖 doGetAuthenticationInfo 方法，用于认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;

        String username = upToken.getUsername();
        String password = new String(upToken.getPassword());

        // 查询账号是否存在
        Account account = accountService.getAccountByUsername(username);
        if (account == null) {
            throw new UnknownAccountException("账号不存在");
        }

        // 验证密码是否正确
        String hashedPassword = HashUtil.sha256(password, account.getSalt());
        if (!hashedPassword.equals(account.getPassword())) {
            throw new IncorrectCredentialsException("密码错误");
        }

        // 构建 SimpleAuthenticationInfo 对象，并返回
        SimpleAuthenticationInfo authInfo = new SimpleAuthenticationInfo();
        authInfo.setPrincipals(new SimplePrincipalCollection(account.getUsername(), getName()));
        authInfo.setCredentials(password);

        return authInfo;
    }

    // 覆盖 doGetAuthorizationInfo 方法，用于授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo authInfo = new SimpleAuthorizationInfo();
        Object principal = principals.getPrimaryPrincipal();

        if (principal instanceof String) { // 如果是用户名
            String username = (String) principal;
            Set<String> roles = accountService.getRolesByUsername(username);
            authInfo.setRoles(roles);

            Set<String> permissions = accountService.getPermissionsByUsername(username);
            authInfo.setStringPermissions(permissions);
        }

        return authInfo;
    }

}