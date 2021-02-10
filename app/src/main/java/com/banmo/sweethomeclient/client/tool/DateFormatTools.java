package com.banmo.sweethomeclient.client.tool;

import java.text.DateFormat;
import java.util.Date;

public class DateFormatTools {
    //日期格式化到年月日时分秒
    public static String formatToSecond(Date date) {
        return DateFormat.getDateTimeInstance().format(date);
    }
}
