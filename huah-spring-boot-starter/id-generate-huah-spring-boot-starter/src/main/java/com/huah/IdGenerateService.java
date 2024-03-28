package com.huah;

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
        return new Random().nextInt(100) + "_" + workId;
    }
}
