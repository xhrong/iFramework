package com.iflytek.iFramework.download.util;

import com.iflytek.iFramework.logger.Logger;

import java.util.Locale;

/**
 * MD 加密
 *
 * @author Kevin
 */
public class MD5 {

    public static String getMD5(String instr) {
        String s = null;
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            java.security.MessageDigest md = java.security.MessageDigest
                    .getInstance("MD5");
            md.update(instr.getBytes());
            byte tmp[] = md.digest();
            char str[] = new char[16 * 2];
            int k = 0;
            for (int i = 0; i < 16; i++) {
                byte byte0 = tmp[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            s = new String(str).toUpperCase(Locale.ENGLISH);

        } catch (Exception e) {
            //如果失败，则用原字符串
            Logger.e("Download MD5", e.toString());
            s = instr;
        } finally {
            return s;
        }
    }

}
