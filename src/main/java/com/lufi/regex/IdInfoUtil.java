package com.lufi.regex;

import com.lufi.utils.DateValidator;

import java.util.Date;
import java.util.Map;

/**
 * 网络请求api
 * http://api.46644.com/idcard?idcard=362330199605262070&appkey=1307ee261de8bbcf83830de89caae73f
 */


public class IdInfoUtil {
    private final static Map<String,String>  region;

    private final static int[] PARITYBIT = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};
    private final static int[] POWER_LIST = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};

    static{
        region = MappingUtil.mapOf("gb2260");
    }

    /**
     * 身份证号是否基本有效
     * @param code 身份证号。
     * @return 是否有效，null和""都是false
     */

    public static boolean isValid(final String s) {
        if (s == null || (s.length() != 15 && s.length() != 18))
            return false;
        final char[] cs = s.toUpperCase().toCharArray();
        // （1）校验位数
        int power = 0;
        for (int i = 0; i < cs.length; i++) {// 循环比正则表达式更快
            if (i == cs.length - 1 && cs[i] == 'X')
                break;// 最后一位可以是X或者x
            if (cs[i] < '0' || cs[i] > '9')
                return false;
            if (i < cs.length - 1)
                power += (cs[i] - '0') * POWER_LIST[i];
        }
        // （2）校验区位码
        if (!region.containsKey(s.substring(0, 6))) {
            return false;
        }

        //校验日期
        String dateString = s.length()==15 ? "19"+s.substring(6,12):s.substring(6,14);
        Date date = DateValidator.getInstance().validate(dateString,"yyyymmdd");

        if(date==null)
            return false;

        // （6）校验“校验码”
        if (s.length() == 15)
            return true;
        return cs[cs.length - 1] == PARITYBIT[power % 11];
    }




}
