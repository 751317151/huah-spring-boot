package com.huah.huahspringbootweb;

import com.huah.annotation.AnMethod;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * @Author huah
 * @Date 2024-07-26 09:10
 */
@Aspect
@Slf4j
@Component
public class TestAspect {

    @Around("execution(public * com.huah.IdGenerateService.generate())")
    public Object doAspect(ProceedingJoinPoint pjp) {
        Object result = null;
        try {
            log.info("=====before=====");
            result = pjp.proceed();
            log.info("=====after======");
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Around("@annotation(anMethod)")
    public Object doAspect2(ProceedingJoinPoint pjp, AnMethod anMethod) {
        Object result = null;
        try {
            log.info("=====anMethod before=====");
//            result = pjp.proceed();
            log.info("=====anMethod after======");
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
