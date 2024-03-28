package com.huah.annotation;

import lombok.Data;

/**
 * @author huah 2023/03/30 11:32
 */

@Data
public class User {

    @AnValue("huah")
    private String name;

    @AnValue("34")
    private int age;

    @AnUser(age = 22)
    public User(){
        System.out.println(this.name);
    }
}
