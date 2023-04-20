package com.foxmo.rpc.register;

import java.util.HashMap;
import java.util.Map;

public class LocalRegister {

    private static Map<String, Class> map = new HashMap<>();

    public static void regist(Class implClass) {
        Class<?>[] interfaces = implClass.getInterfaces();

        for(Class clazz : interfaces){
            map.put(clazz.getName(),implClass);
        }
    }

    public static Class get(String interfaceName) {
        return map.get(interfaceName);
    }
}

