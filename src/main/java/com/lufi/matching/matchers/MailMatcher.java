package com.lufi.matching.matchers;

import com.lufi.matching.Matcher;
import com.lufi.utils.Constants;

import java.io.Serializable;
import java.util.regex.Pattern;

/**
 * Created by Sunny on 2018/1/4.
 */
public class MailMatcher implements Matcher,Serializable {

    private static volatile MailMatcher INSTANCE = null;

    private MailMatcher(){}

    public static MailMatcher getInstance(){
        if(INSTANCE == null){
            synchronized(MailMatcher.class){
                if(INSTANCE == null){
                    INSTANCE = new MailMatcher();
                }
            }
        }
        return  INSTANCE;
    }

    /**
     * 邮箱地址是否基本有效
     * @param mailAddress 邮箱地址。
     * @return 是否有效，null和""都是false
     */
    @Override
    public boolean isValid(final String mailAddress) {
        String pattern="^[a-z0-9]+([._\\\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$";
        return Pattern.matches(pattern,mailAddress);
    }

    @Override
    public String type() {
        return Constants.MAIL;
    }
}
