
package com.bobo.iweeker.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.text.format.DateFormat;

public class WeiboDateFormat {

    /**
     * 格式化微博时间
     */
    @SuppressLint("SimpleDateFormat")
    public static String getFormatDateFromCreate(String create_at) {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        String str = null;
        int now_date[] = new int[5];

        now_date = getIntTime(date);
        String now_month = getMonthFromDate(now_date[1]);
        String now_year = String.valueOf(now_date[0]);

        String create[] = create_at.split(" ");

        if (now_year.equals(create[5])) {
            if (now_month.equals(create[1])) {
                if (Integer.parseInt(create[2]) == now_date[2]) {
                    String now_time = simpleDateFormat.format(date);
                    String errand = getTimeErrandOfOneDay(now_time, create[3].substring(0, 5));
                    if (errand.equals("")) {
                        str = "今天" + create[3].substring(0, 5);
                    } else {
                        if (errand.equals(" ")) {
                            str = "刚刚";
                        } else {
                            str = errand + "分钟前";
                        }
                    }
                } else {
                    if (Integer.parseInt(create[2]) == now_date[2] - 1) {
                        str = "昨天" + create[3].substring(0, 5);
                    } else {
                        str = getMonthFromDate(create[1]) + "-" + create[2] + " "
                                + create[3].substring(0, 5);
                    }
                }
            } else {
                str = getMonthFromDate(create[1]) + "-" + create[2] + " "
                        + create[3].substring(0, 5);
            }

        } else {
            str = getMonthFromDate(create[1]) + "-" + create[2] + " " + create[3].substring(0, 5);
        }
        return str;
    }

    /**
     * 获得时间差
     */
    public static String getTimeErrandOfOneDay(String now_time, String temp_time) {
        String str = null;
        int now_h = Integer.parseInt(now_time.substring(0, 2));
        int now_m = Integer.parseInt(now_time.substring(3, 5));

        int tmp_h = Integer.parseInt(temp_time.substring(0, 2));
        int tmp_m = Integer.parseInt(temp_time.substring(3, 5));

        if (now_h == tmp_h) {
            if (now_m - tmp_m == 0)
                str = " ";
            else
                str = String.valueOf(now_m - tmp_m);
        } else {
            if (now_h == tmp_h + 1) {
                int errand = now_m + 60 - tmp_m;
                if (errand < 60)
                    str = String.valueOf(errand);
                else 
                    str = "";
            } else
                str = "";
        }
        return str;
    }

    /**
     * 获得月份
     */
    public static String getMonthFromDate(int month) {
        switch (month) {
            case 1:
                return "Jan";
            case 2:
                return "Feb";
            case 3:
                return "Mar";
            case 4:
                return "Apr";
            case 5:
                return "May";
            case 6:
                return "Jun";
            case 7:
                return "Jul";
            case 8:
                return "Aug";
            case 9:
                return "Sept";
            case 10:
                return "Oct";
            case 11:
                return "Nov";
            case 12:
                return "Dec";
            default:
                return "";
        }
    }

    /**
     * 获得月份
     */
    public static String getMonthFromDate(String month) {
        if (month.equals("Jan"))
            return "01";
        if (month.equals("Feb"))
            return "02";
        if (month.equals("Mar"))
            return "03";
        if (month.equals("Apr"))
            return "04";
        if (month.equals("May"))
            return "05";
        if (month.equals("Jun"))
            return "06";
        if (month.equals("Jul"))
            return "07";
        if (month.equals("Aug"))
            return "08";
        if (month.equals("Sept"))
            return "09";
        if (month.equals("Oct"))
            return "10";
        if (month.equals("Nov"))
            return "11";
        if (month.equals("Dec"))
            return "12";
        return "";
    }

    /**
     * 获得整数时间
     */
    @SuppressLint("SimpleDateFormat")
    public static int[] getIntTime(Date date) {
        String dateStr;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        int tempDate[] = new int[5];

        dateStr = DateFormat.format("yyyy-MM-dd", date).toString();

        tempDate[0] = Integer.parseInt(dateStr.substring(0, 4));
        tempDate[1] = Integer.parseInt(dateStr.substring(5, 7));
        tempDate[2] = Integer.parseInt(dateStr.substring(8, 10));

        dateStr = simpleDateFormat.format(date);

        tempDate[3] = Integer.parseInt(dateStr.substring(0, 2));
        tempDate[4] = Integer.parseInt(dateStr.substring(3, 5));

        return tempDate;
    }
}
