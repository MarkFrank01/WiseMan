package com.zxcx.zhizhe.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by anm on 2017/7/13.
 */

public class DateTimeUtils {

    public static long getNowTimestamp() {
        Date date = new Date();
        long timestamp = date.getTime();
        return timestamp;
    }

    public static String getDate() {
        String time = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = GregorianCalendar.getInstance();
        try {
            time = sdf.format(calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }

    public static String getDate(long timestamp) {
        Date date = new Date(timestamp);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    public static String getDateString(Date date) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
            return format.format(date);
        } catch (Exception e) {
            return "";
        }
    }

    public static int getYear(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = GregorianCalendar.getInstance();
        try {
            calendar.setTime(sdf.parse(time));
        } catch (Exception e) {
            e.printStackTrace();
        }
        int year = calendar.get(Calendar.YEAR);
        return year;
    }

    public static int getMonth(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = GregorianCalendar.getInstance();
        try {
            calendar.setTime(sdf.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int month = calendar.get(Calendar.MONTH);
        return month;
    }

    public static int getDay(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = GregorianCalendar.getInstance();
        try {
            calendar.setTime(sdf.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int day = calendar.get(Calendar.DATE);
        return day;
    }

    public static String getBirthdayTime(int year, int monthOfYear, int dayOfMonth) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, monthOfYear);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        Date d = c.getTime();
        return format.format(d);
    }

}
