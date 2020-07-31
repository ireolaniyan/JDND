package com.example.demo;

import java.lang.reflect.Field;

public class TestUtils {
    public static void injectObject(Object target, String fieldName, Object toInject) {
        boolean isPrivate = false; // Whether a field in a class is a private or public (i.e. accessible)

        try {
            Field f = target.getClass().getDeclaredField(fieldName);

            if(!f.isAccessible()) {
                f.setAccessible(true);
                isPrivate = true;
            }
            f.set(target, toInject);

            if(isPrivate){
                f.setAccessible(false);
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
