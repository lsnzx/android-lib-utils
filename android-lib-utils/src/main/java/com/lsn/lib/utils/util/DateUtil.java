package com.lsn.lib.utils.util;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Author: Chris
 * Blog: https://www.jianshu.com/u/a3534a2292e8
 * Date: 2021/4/7
 * Description
 */
public class DateUtil {
    private static final SimpleDateFormat birthdayFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat hmsFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String YYYY01MM01DD_HH_MM_SS = "yyyy/MM/dd HH:mm:ss";
    public static String YYYY02MM02DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static String YYYY01MM01DD_HH_MM = "yyyy/MM/dd HH:mm";
    public static String YYYY02MM02DD_HH_MM = "yyyy-MM-dd HH:mm";
    public static String YYYY01MM01DD = "yyyy/MM/dd";
    public static String YYYY02MM02DD = "yyyy-MM-dd";

    public static String X02MM02DD = "MM-dd HH:mm";
    public static String X01MM02DD = "MM/dd HH:mm";

    /**
     * second to HH:MM:ss
     * @param seconds
     * @return
     */
    public static String convertSecondsToTime(long seconds) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (seconds <= 0)
            return "00:00";
        else {
            minute = (int)seconds / 60;
            if (minute < 60) {
                second = (int)seconds % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = (int)(seconds - hour * 3600 - minute * 60);
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    public static String convertSecondsToFormat(long seconds,String format){

        if(TextUtils.isEmpty(format))
            return "";

        Date date = new Date(seconds);
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        return sdf.format(date);
    }


    public static Calendar getThisDayCalendar() {
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date());
        instance.set(Calendar.HOUR_OF_DAY,0);
        instance.set(Calendar.MINUTE,0);
        instance.set(Calendar.SECOND,0);
        return instance;
    }


    private static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }

    public static long formatMill(float day){
        return (long) (day * 24 * 60 * 60 * 1000);
    }


    public static String formatDuring(long mss) {
        long days = mss / (1000 * 60 * 60 * 24);
        long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (mss % (1000 * 60)) / 1000;
        return days + " days " + hours + " hours " + minutes + " minutes "
                + seconds + " seconds ";
    }


    /**
     * 格式化日期时间 日期时间格式yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    @SuppressWarnings("AlibabaAvoidCallStaticSimpleDateFormat")
    public static String birthdaytime(Date date) {
        return birthdayFormat.format(date);
    }

    public static String hmsdaytime(Date date) {
        return hmsFormat.format(date);
    }
}
