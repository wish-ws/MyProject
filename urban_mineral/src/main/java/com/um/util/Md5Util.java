package com.um.util;

import com.um.common.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;

@Slf4j
public class Md5Util {

    /***
     * MD5加密 生成32位md5码
     */
    public static String md5Encode(String inStr) {

        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] byteArray = inStr.getBytes("UTF-8");
            byte[] md5Bytes = md5.digest(byteArray);
            StringBuffer hexValue = new StringBuffer();
            for (int i = 0; i < md5Bytes.length; i++) {
                int val = ((int) md5Bytes[i]) & 0xff;
                if (val < 16) {
                    hexValue.append("0");
                }
                hexValue.append(Integer.toHexString(val));
            }
            return hexValue.toString();
        } catch (Exception e) {
            log.error(e.toString());
            throw new ServiceException("MD5加密异常");
        }
    }
}
