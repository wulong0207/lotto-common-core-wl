package com.hhly.commoncore.base.util;

import com.hhly.skeleton.base.constants.UserConstants;
import com.hhly.skeleton.base.util.MathUtil;

import java.text.DecimalFormat;

/**
 * @author zhouyang
 * @version 1.0
 * @desc 金钱格式转换工具类
 * @date 2017/10/27
 * @company 益彩网络科技公司
 */
public class MoneyFormatUtil {

    public static String dispose(Double prebonus) {

        String str = "";
        if (prebonus < UserConstants.TEN_THOUSANT) {
            return formatAmountToStr(prebonus) + UserConstants.YUAN;
        } else if (prebonus >= UserConstants.TEN_THOUSANT && prebonus < UserConstants.HUNDRED_MILLIONS) {
            return MathUtil.formatAmountToStr(prebonus/UserConstants.TEN_THOUSANT) + UserConstants.TEN_THOUSANT_STR + UserConstants.YUAN;
        } else {
            return MathUtil.formatAmountToStr(prebonus/UserConstants.HUNDRED_MILLIONS) + UserConstants.HUNDRED_MILLIONS_STR+ UserConstants.YUAN;
        }
    }

    public static String formatAmountToStr(Double amount) {
        if (MathUtil.compareTo(amount, 1.0) < 0) {
            return String.valueOf(amount);
        }
        DecimalFormat myformat = new DecimalFormat();
        myformat.applyPattern("#,###.##");
        try {
            return myformat.format(amount);
        } catch (Exception e) {
            return String.valueOf(amount);
        }
    }
}
