package com.lufi.regex;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class MappingUtil {
    public static Scanner resource(final String name){
        return new Scanner(PhoneInfoUtil.class.getClassLoader()
                .getResourceAsStream(String.format("%s.txt",name)));//用src/main/resource/%s.txt不行.
    }

    public static Map<String,String> mapOf(final String filename){
        Map<String,String> mapping = new HashMap<>();
        Scanner cin = resource(filename);
        while(cin.hasNext()){
            mapping.put(cin.next(),cin.next());
        }

        cin.close();
        return mapping;
    }
}
