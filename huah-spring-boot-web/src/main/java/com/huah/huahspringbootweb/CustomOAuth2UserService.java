package com.huah.huahspringbootweb;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        System.out.println("loaduser==========: ");
        OAuth2User oAuth2User = super.loadUser(userRequest);
        // 获取用户信息的逻辑
        // 返回包含用户信息的OAuth2User对象
        return oAuth2User;
    }

}