package com.lufi.regex;

import java.util.Map;

public class PhoneInfoUtil {

    private final static Map<String, String> districts;
    private final static Map<String, String> phoneNumbers;
    private final static Map<String, String> diallingCodes;

    static{
        districts = MappingUtil.mapOf("province-city");
        phoneNumbers = MappingUtil.mapOf("phone-numbers");
        diallingCodes = MappingUtil.mapOf("dialling-code");
    }

    /**
     * 返回手机/电话号码对应的归属地。
     *
     * @param number 手机/电话号码。
     * @return 归属地。
     */
    public static String ofTelNumber(final String number) {
        if (number == null || districts.isEmpty()) {
            return null;
        }
        if (number.startsWith("0")) {
            if (number.length() >= 3) {
                String prefix = number.substring(0, 3);
                if (diallingCodes.containsKey(prefix)&&(number.substring(3).length()==7||number.substring(3).length()==8)) {
                    return diallingCodes.get(prefix);
                }
            }
            if (number.length() >= 4) {
                String prefix = number.substring(0, 4);
                if (diallingCodes.containsKey(prefix)&&(number.substring(4).length()==7||number.substring(4).length()==8)) {
                    return diallingCodes.get(prefix);
                }
            }
        } else if (number.startsWith("1") && number.length() == 11) {
            String prefix = number.substring(0, 7);
            if (phoneNumbers.containsKey(prefix)) {
                return phoneNumbers.get(prefix);
            }
        }
        return null;
    }

    /**
     * 返回手机/电话号码对应的归属地。
     *
     * @param number 手机/电话号码。
     * @return 是否有效。
     */
    public static boolean isTelNumber(final String number) {
        if (number == null || districts.isEmpty()) {
            return false;
        }
        if (number.startsWith("0")) {
            if (number.length() >= 3) {
                String prefix = number.substring(0, 3);
                if (diallingCodes.containsKey(prefix)&&(number.substring(3).length()==7||number.substring(3).length()==8)) {
                    return true;
                }
            }
            if (number.length() >= 4) {
                String prefix = number.substring(0, 4);
                if (diallingCodes.containsKey(prefix)&&(number.substring(4).length()==7||number.substring(4).length()==8)) {
                    return true;
                }
            }
        } else if (number.startsWith("1") && number.length() == 11) {
            String prefix = number.substring(0, 7);
            if (phoneNumbers.containsKey(prefix)) {
                return true;
            }
        }
        return false;
    }


}
