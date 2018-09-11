package com.hhly.commoncore.base.util;

import com.hhly.skeleton.base.util.RegularValidateUtil;

/**
 * Created by lgs on 2016/12/29.
 * String类处理
 */
public class StringUtil {

    /**
     * 判断字符串是否是null或者""
     * @param str 参数
     * @return true or false
     */
    public static boolean isEmpty(String str){
        if(str==null||str.equals("")){
            return true;
        }
        return false;
    }

    public static String encrptionNickname(String nickName) {
        char[] chars = nickName.toCharArray();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < nickName.length(); i++) {
            String str = String.valueOf(chars[i]);
            if (str.matches(RegularValidateUtil.REGULAR_NICKNAME)) {
                stringBuffer.append(str);
            }
        }
        String nickname = stringBuffer.toString().trim();
        if (nickname.length()<= 8) {
            return nickname;
        } else {
            String str = nickname.substring(0,8)+"...";
            return str;
        }
    }
}
