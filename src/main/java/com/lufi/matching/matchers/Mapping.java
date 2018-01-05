package com.lufi.matching.matchers;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by symsimmy on 2018/1/4
 */
public class Mapping {
    public static Scanner resource(final String name){
        return new Scanner(Mapping.class.getClassLoader()
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

    public static Table<String,Integer,String> multiMapOf(final String filename){
        Table<String,Integer,String> multiMapping = HashBasedTable.create();
        Scanner cin = resource(filename);
        while(cin.hasNext()){
            multiMapping.put(cin.next(),cin.nextInt(),cin.next());
        }

        cin.close();
        return multiMapping;
    }
}
