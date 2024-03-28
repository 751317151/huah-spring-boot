package com.huah.annotation;

import java.lang.reflect.Field;

/**
 * @author huah 2023/03/30 11:34
 */
public class AnnotationTest {
    public static void main(String[] args) {
        User user = new User();
        System.out.println(user);

        try {
            Field nameField = User.class.getDeclaredField("name");
            nameField.setAccessible(true);
            if (nameField.isAnnotationPresent(AnValue.class)) {
                AnValue anValue = nameField.getAnnotation(AnValue.class);
//                user.setName(anValue.value());
                nameField.set(user, anValue.value());
            }

            Field ageField = User.class.getDeclaredField("age");
            ageField.setAccessible(true);
            if (ageField.isAnnotationPresent(AnValue.class)) {
                AnValue anValue = ageField.getAnnotation(AnValue.class);
//                user.setAge(anValue.value());
                ageField.set(user, anValue.value());
            }
        }catch (Exception e){

        }
        System.out.println(user);

    }
}
