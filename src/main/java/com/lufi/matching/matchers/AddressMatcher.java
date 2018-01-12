package com.lufi.matching.matchers;

import com.lufi.matching.Matcher;
import com.lufi.utils.Constants;

import java.io.Serializable;

/**
 * Created by Sunny on 2018/1/4.
 */
public class AddressMatcher implements Matcher,Serializable {

    private static volatile AddressMatcher INSTANCE = null;

    private static String[] cityMatcher = {"省","市","区","县","乡","镇","村"};
    private static String[] streetMatcher = {"大道","路","街","弄","宅"};
    private static String[] numberMatcher = {"队","号","丘","组","楼","层","室"};

    private AddressMatcher(){}

    public static AddressMatcher getInstance(){
        if(INSTANCE == null){
            synchronized(AddressMatcher.class){
                if(INSTANCE == null){
                    INSTANCE = new AddressMatcher();
                }
            }
        }
        return  INSTANCE;
    }

    @Override
    public  boolean isValid(String address){
        if(address == null || address.length() == 0){
            return false;
        }
        int count0 = count(address, cityMatcher);
        int count1 = count(address, streetMatcher);
        int count2 = count(address, numberMatcher);

        if((count0 > 0 || count1 > 0) && count2 > 0)
            return true;
        else
            return false;
    }

    @Override
    public String type() {
        return Constants.ADDRESS;
    }

    private static int count(String address, String[] matcher){
        int count = 0;
        for(int i = 0; i < matcher.length; i++){
            int index = address.indexOf(matcher[i]);
            if(index >= 0)
                count ++;
        }
        return count;
    }
}
