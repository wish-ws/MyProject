package com.um.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * @description 随机数工具类
 * @author ws
 */
public class NumberUtil {


    public static final String DEFAULT_PWD = "666666";


    private static final char[] letters
            = new char[]{'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q',
                        'R','S','T','U','V','W','X','Y','Z','a','b','c','d','e','f','g','h','i',
                        'j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','r',
                        '0','1','2','3','4','5','6','7','8','9'};

    //临时订单编码集合
    public static List<String> tempCodeList;

    /**
     * 生成随机数字
     * @param digits 位数
     */
    public static String generateNumber(int digits){
        String code = "";
        Random rand = new Random();//生成随机数
        for(int a = 0; a < digits; a++){
            code += rand.nextInt(10);//0-10
        }
        return code;
    }


    /**
     * 生成随机数字和字母的组合
     * @param digits 位数
     */
    public static String generateNumberAndLetters(int digits){
        String code = "";
        Random rand = new Random();//生成随机数
        int index;
        for(int a = 0; a < digits; a++){
            index = rand.nextInt(letters.length);
            code += letters[index];
        }
        return code;
    }

    public static String generateUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }






    public static String createCode(Long randomNum,String beginStr) {
        StringBuffer orderCode = new StringBuffer(beginStr);
        if(null != randomNum && randomNum > 0) {
            //取当前日期
            String dateStr = DateUtil.dateToString(DateUtil.getCurrentDate());
            //获取年份
            String year = dateStr.substring(2,4);
            //获取月份
            String month = dateStr.substring(5,7);
            orderCode.append(year).append(month).append(dateStr.substring(8));
            //订单数%99999,确保订单数取模后占5位
            Long orderSeq = 10000l+randomNum%9999;
            orderCode.append(orderSeq.toString().substring(1));
            //订单末尾加三位数，从001-999，预防两个订单之间并发下单
            if(null == tempCodeList || tempCodeList.size() <= 0) {
                tempCodeList = new ArrayList<String>();
                for(int i = 102; i < 200; i ++) {
                    tempCodeList.add(String.valueOf(i).substring(1,3));
                }
                orderCode.append("01");
            }else {
                orderCode.append(tempCodeList.get(0));
                tempCodeList.remove(0);
            }
        }
        return orderCode.toString();
    }


}
