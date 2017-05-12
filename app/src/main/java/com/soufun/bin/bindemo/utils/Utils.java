package com.soufun.bin.bindemo.utils;

/**
 * @描述
 * @入口
 * @进入要传的值
 * @备注说明
 */

public class Utils {

    public static String getWeek(int week){
        String str;
        switch (week){
            case 0:
                str = "星期一";
                break;
            case 1:
                str = "星期二";
                break;
            case 2:
                str = "星期三";
                break;
            case 3:
                str = "星期四";
                break;
            case 4:
                str = "星期五";
                break;
            case 5:
                str = "星期六";
                break;
            case 6:
                str = "星期日";
                break;
            default:
                str = "星期一";
        }
        return str;
    }

    public static boolean isNullOrEmpty(String str) {
        if (str == null || "".equals(str.trim()) || str.trim().length() == 0
                || "null".equals(str.trim())) {
            return true;
        } else {
            return false;
        }
    }
}
