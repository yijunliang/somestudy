package com.yjl.config;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import com.yjl.entity.SysPermission;
import com.yjl.entity.SysRole;
import com.yjl.entity.UserInfo;
import com.yjl.service.UserInfoService;

import javax.annotation.Resource;

public class MyShiroRealm extends AuthorizingRealm {
	//用于用户查询
	@Resource
    private UserInfoService userInfoService;
    //角色权限和对应权限添加
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
    	//获取登录用户名
        String name= (String) principals.getPrimaryPrincipal();
        //查询用户名称
        UserInfo findByUsername = userInfoService.findByUsername(name);
        System.out.println("权限配置-->MyShiroRealm.doGetAuthorizationInfo()");
        //添加角色和权限
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        //获取用户信息
        UserInfo userInfo  = (UserInfo)principals.getPrimaryPrincipal();
        //userInfo.getRoleList()用户的角色(一个用户有多个角色)
        for(SysRole role : userInfo.getRoleList()){
        	//添加角色
            authorizationInfo.addRole(role.getRole());
            //添加权限
            for(SysPermission p : role.getPermissions()){
                authorizationInfo.addStringPermission(p.getPermission());
            }
        }
        return authorizationInfo;
    }

    /*主要是用来进行身份认证的，也就是说验证用户输入的账号和密码是否正确。*/
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {
        System.out.println("进入MyShiroRealm.doGetAuthenticationInfo()身份验证");
        //获取用户信息
        String username = (String)token.getPrincipal();
        System.out.println(token.getCredentials());
        
        //实际项目中，这里可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制，2分钟内不会重复执行该方法
        UserInfo userInfo = userInfoService.findByUsername(username);
        //通过username从数据库中查找 User对象，如果找到，没找到.
        System.out.println("----->>userInfo=" + userInfo.getUsername() + "|" + userInfo.getPassword());
        if(userInfo == null){
        	//这里返回后会报出对应异常
            return null;
        }
        //这里验证authenticationToken和simpleAuthenticationInfo的信息  (属于else)
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                userInfo, //用户名
                userInfo.getPassword(),//密码
                ByteSource.Util.bytes(userInfo.getCredentialsSalt()),//salt=username+salt
                getName()//realm name
        );
        return authenticationInfo;
    }

}