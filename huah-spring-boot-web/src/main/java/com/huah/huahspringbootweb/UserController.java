package com.huah.huahspringbootweb;

import com.huah.IdGenerateService;
import com.huah.IdProperties;
import com.huah.test.common.CommonBean;
import com.huah.test.config.ConfigMarker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author huah 2023/12/18 15:44
 */
@RestController
public class UserController {

    @Autowired
    private CommonBean commonBean;

    @Autowired
    private ConfigMarker configMarker;

    @Autowired
    private IdProperties idProperties;

    @Autowired
    private IdGenerateService idGenerateService;

    @GetMapping("/")
    public String home() {
        return "登录成功后的主页";
    }

    @GetMapping("/test")
    public String test() {
        System.out.println(commonBean);
        System.out.println(configMarker);
        idGenerateService.testAspect();
        System.out.println(idGenerateService.generate());
        return "success";
    }

    @GetMapping("/userinfo")
    public String userinfo(@AuthenticationPrincipal OAuth2User principal) {
        StringBuilder sb = new StringBuilder();
        sb.append("GitHub 登录成功！用户信息：<br>");
        sb.append("用户名: ").append((String) principal.getAttribute("login")).append("<br>");
        sb.append("姓名: ").append((String) principal.getAttribute("name")).append("<br>");
        sb.append("邮箱: ").append((String) principal.getAttribute("email"));
        return sb.toString();
    }

}
