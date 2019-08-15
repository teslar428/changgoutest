package com.changgou.oauth.config;

import com.changgou.entity.Result;
import com.changgou.oauth.util.UserJwt;
import com.changgou.user.feign.UserFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

//自定义授权认证类
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    ClientDetailsService clientDetailsService;

    @Autowired
    private UserFeign userFeign;

    //自定义授权认证
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //授权码模式

        //取出身份，如果身份为空说明没有认证
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //没有认证统一采用httpbasic认证，httpbasic中存储了client_id和client_secret，开始认证client_id和client_secret
        if (authentication == null) {
            ClientDetails clientDetails = clientDetailsService.loadClientByClientId(username);
            if (clientDetails != null) {
                //秘钥
                String clientSecret = clientDetails.getClientSecret();
                //数据库查找方式
                return new User(username, clientSecret, AuthorityUtils.commaSeparatedStringToAuthorityList(""));
            }
        }

        if (StringUtils.isEmpty(username)) {
            return null;
        }

        //密码模式

        Result<com.changgou.user.pojo.User> userResult = userFeign.findById(username);

        //根据用户名查询用户信息
        //创建User对象
        List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
        list.add(new SimpleGrantedAuthority("admin"));
        list.add(new SimpleGrantedAuthority("salesman"));
        list.add(new SimpleGrantedAuthority("accounttant"));
        list.add(new SimpleGrantedAuthority("user"));
//        String permissions = "salesman,accountant,user";
//        UserJwt userDetails = new UserJwt(username, userResult.getData().getPassword(), AuthorityUtils.commaSeparatedStringToAuthorityList(permissions));
        UserJwt userDetails = new UserJwt(username, userResult.getData().getPassword(), list);
        return userDetails;
    }
}
