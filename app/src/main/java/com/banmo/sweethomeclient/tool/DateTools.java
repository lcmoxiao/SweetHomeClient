package com.banmo.sweethomeclient.tool;

import java.text.DateFormat;
import java.util.Date;

public class DateTools {

    public static String formatToDay(Date date) {
        return DateFormat.getDateInstance().format(date);
    }


    //日期格式化到年月日时分秒
    public static String formatToSecond(Date date) {
        return DateFormat.getDateTimeInstance().format(date);
    }

    public static String getNowTimeToSecond() {
        return formatToSecond(new Date());
    }
}
