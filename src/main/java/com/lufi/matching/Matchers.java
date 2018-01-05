package com.lufi.matching;

import java.util.ArrayList;

/**
 * Created by Sunny on 2018/1/4.
 */
public class Matchers {
    private ArrayList<Matcher> matchers = new ArrayList<>();

    public Matchers(){}

    // 添加新的匹配方法到Matchers中，使得匹配方法灵活可扩展
    public void addMatcher(Matcher matcher){
        matchers.add(matcher);
    }

    // 对目标文件进行隐私信息匹配，返回结果文件的路径
    public String match(String fileName){
        // TODO
        
        return null;
    }

}
