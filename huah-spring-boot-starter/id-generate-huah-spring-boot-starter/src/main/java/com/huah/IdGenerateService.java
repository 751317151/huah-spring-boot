package com.huah;

import com.huah.annotation.AnMethod;

import java.util.Random;

/**
 * @author huah 2023/12/18 20:02
 */
public class IdGenerateService {
    private Long workId;
    public IdGenerateService(Long workId) {
        this.workId = workId;
    }

    public String generate() {
        System.out.println("======generate======");
        return new Random().nextInt(100) + "_" + workId;
    }

    @AnMethod
    public void testAspect() {
        System.out.println("=====aspect=====");;
    }
}
