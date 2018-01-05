package com.lufi.matching.matchers;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.lufi.matching.Matcher;

import java.util.Iterator;
import java.util.Set;

/**
 * Created by Sunny on 2018/1/4.
 */
public class BankMatcher implements Matcher {

    private static volatile BankMatcher INSTANCE = null;

    private final Table<String,Integer,String> banks;

    private BankMatcher(){
        banks = Mapping.multiMapOf("bank-bin");
    }

    public static BankMatcher getInstance(){
        if(INSTANCE == null){
            synchronized(BankMatcher.class){
                if(INSTANCE == null){
                    INSTANCE = new BankMatcher();
                }
            }
        }
        return  INSTANCE;
    }


    /**
     * 基于Luhn算法,校验银行卡号是否有效
     * @param bankId 银行卡号。
     * @return 是否有效，null和""都是false
     */
    @Override
    public boolean isValid(final String bankId) {
        if (bankId.length() < 13 || bankId.length() > 19) {
            return false;
        }
        int[] cardNoArr = new int[bankId.length()];
        for (int i = 0; i < bankId.length(); i++) {
            if (bankId.charAt(i) - '0' < 0 || bankId.charAt(i) - '0' > 9) {
                return false;
            }
            cardNoArr[i] = Integer.valueOf(String.valueOf(bankId.charAt(i)));
        }
        for (int i = cardNoArr.length - 2; i >= 0; i -= 2) {
            cardNoArr[i] <<= 1;
            cardNoArr[i] = cardNoArr[i] / 10 + cardNoArr[i] % 10;
        }
        int sum = 0;
        for (int i = 0; i < cardNoArr.length; i++) {
            sum += cardNoArr[i];
        }

        if ((sum % 10 != 0) || !check(bankId)) {
            return false;
        }

        return true;
    }

    /**
     * 获取银行编码
     * @param bankId 银行卡号。
     * @return 银行编码
     */

    private boolean check(final String bankId){
        if(banks.isEmpty()){
            return false;
        }

        Set<Table.Cell<String,Integer,String>> bankSet = banks.cellSet();
        Iterator<Table.Cell<String,Integer,String>> it = bankSet.iterator();
        while(it.hasNext()){
            Table.Cell<String,Integer,String> bank = it.next();
            if(bankId.contains(bank.getRowKey()) && bankId.length() == bank.getColumnKey()){
                //System.out.println(bank.getValue());
                return true;
            }
        }

        return false;

    }
}
