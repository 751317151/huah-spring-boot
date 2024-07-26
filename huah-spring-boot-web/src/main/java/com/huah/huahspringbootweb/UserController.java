package com.huah.huahspringbootweb;

import com.huah.IdGenerateService;
import com.huah.IdProperties;
import com.huah.test.common.CommonBean;
import org.springframework.beans.factory.annotation.Autowired;
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
    private IdProperties idProperties;

    @Autowired
    private IdGenerateService idGenerateService;

    @GetMapping("/test")
    public String test() {
        System.out.println(commonBean);
        idGenerateService.testAspect();
        System.out.println(idGenerateService.generate());
        return "success";
    }
}
