package com.changdao.master.common.tool.utils;

import android.text.TextUtils;

import com.bigkoo.pickerview.utils.LunarCalendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by zyt on 2018/7/18.
 * 时间工具类
 */

public class DateUtils {
    private static DateUtils dateUtils;

    private final String[] Gan = new String[]{"癸", "甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬"};
    private final String[] Zhi = new String[]{"亥", "子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌"};


    public static DateUtils init() {
        if (dateUtils == null) {
            synchronized (DateUtils.class) {
                if (dateUtils == null) {
                    dateUtils = new DateUtils();
                }
            }
        }
        return dateUtils;
    }

    /**
     * 获取星期几
     *
     * @param i 传 cal.get(Calendar.DAY_OF_WEEK)
     * @return
     */
    public String getWeek(int i) {
//        Calendar cal = Calendar.getInstance();
//        int i = cal.get(Calendar.DAY_OF_WEEK);
        switch (i) {
            case 1:
                return "星期日";
            case 2:
                return "星期一";
            case 3:
                return "星期二";
            case 4:
                return "星期三";
            case 5:
                return "星期四";
            case 6:
                return "星期五";
            case 7:
                return "星期六";
            default:
                return "";
        }
    }


    public String getChinaWeek(int i) {
        switch (i) {
            case 1:
                return "周日";
            case 2:
                return "周一";
            case 3:
                return "周二";
            case 4:
                return "周三";
            case 5:
                return "周四";
            case 6:
                return "周五";
            case 7:
                return "周六";
            default:
                return "";
        }
    }

    /**
     * string类型转换为date类型
     * formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒，
     * strTime的时间格式必须要与formatType的时间格式相同
     *
     * @param strTime
     * @param formatType
     * @return
     */
    public Date stringToDate(String strTime, String formatType) {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        try {
            date = formatter.parse(strTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 获取上午下午中午
     *
     * @param d
     * @return
     */
    public String getDateTimeSlot(Date d) {
//        if (d.getHours() < 11) {
//            tv.setText("一 早上好 一");
//        } else
        if (d.getHours() < 13) {
            return "上午";
        } else if (d.getHours() < 18) {
            return "下午";
        } else if (d.getHours() < 24) {
            return "晚上";
        }
        return "上午";
    }

    /**
     * 返回数据格式 yyyy年MM月dd日
     *
     * @param date
     * @return
     */
    public String getSimpleDataString(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");// HH:mm:ss
        return simpleDateFormat.format(date);
    }

    /**
     * 传回农历 y年的生肖
     *
     * @param year 2018
     * @return
     */
    public String animalsYear(int year) {
        final String[] Animals = new String[]{"鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊", "猴", "鸡", "狗", "猪"};
        return Animals[(year - 4) % 12];
    }

    /**
     * 获取天干地支年
     *
     * @param year 2018
     * @return
     */
    public String cyclicalmYear(int year) {
        int num = year - 3;
        return (Gan[num % 10] + Zhi[num % 12]);
    }

    /**
     * 获取天干地支月=== 使用阳历的日期获取，不要使用农历日期
     *
     * @param cld
     * @return
     */
    public String cyclicalmMonth(Calendar cld) {
        int year = cld.get(Calendar.YEAR);
        int month = cld.get(Calendar.MONTH);
        String yearStr = String.valueOf(year);
        String monthStr = String.valueOf(month);
        int endYear = Integer.valueOf(yearStr.substring(yearStr.length() - 1, yearStr.length()));
        int endMonth = monthStr.length() > 1 ? Integer.valueOf(monthStr.substring(monthStr.length() - 1, monthStr.length())) : month;
        int G = (endYear + 2) * 2 + endMonth;
        int Z = month + 2 > 12 ? month - 10 : month + 2;//（大于12的时候同样减去12）
        return (Gan[G % 10] + Zhi[Z % 12]);
    }

    /**
     * 获取天干地支日
     *
     * @param cld 使用阳历的日期获取，不要使用农历日期
     * @return
     */
    public String cyclicalmDay(Calendar cld) {
        //月份需要加1
        int year = cld.get(Calendar.YEAR);
        int C = Integer.valueOf(String.valueOf(year).substring(0, 2));
        int Y = Integer.valueOf(String.valueOf(year).substring(2, String.valueOf(year).length()));
        int M = cld.get(Calendar.MONTH) + 1;
        int D = cld.get(Calendar.DAY_OF_MONTH);
        int I = M % 2 == 0 ? 6 : 0;//I 是需要判断当前月份是奇数月还是偶数月 奇数月I=0 偶数月 I=6
        // 日天干：=(4*C +[C /4]+[5*Y ]+[Y /4]+[3*(M +1)/5]+D – 3)Mod 10
        // 日地支：=(8*C +[C /4]+[5*Y ]+[Y /4]+[3*(M +1)/5]+D +7+I )Mod 12
        int G = (4 * C + (C / 4) + (5 * Y) + (Y / 4) + (3 * (M + 1) / 5) + D - 3);
        int Z = (8 * C + (C / 4) + (5 * Y) + (Y / 4) + (3 * (M + 1) / 5) + D + 7 + I);
//        int Z = G + 4 * C + 10 + I;
        return Gan[G % 10] + Zhi[Z % 12];
    }

    /**
     * 获取农历的月份日期
     *
     * @param calendar
     * @return 正月初三
     */
    public String getChinaMonthAndDayString(Calendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        //获取农历的方法
        int[] lunarDate = LunarCalendar.solarToLunar(year, month, day);
//        LogUtil.e("农历：：" + lunarDate[0] + "===" + lunarDate[1] + "===" + lunarDate[2]);
        String[] nlMonth = {"正", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二"};

        return nlMonth[lunarDate[1] - 1] + "月" + getChinaDayString(lunarDate[2]);
    }

    /**
     * 获取农历的天
     *
     * @param day cal.get(Calendar.DAY_OF_Moth)
     * @return
     */
    private String getChinaDayString(int day) {
        String chineseTen[] = {"初", "十", "廿", "卅"};
        String chineseDay[] = {"一", "二", "三", "四", "五", "六", "七", "八", "九", "十"};
        String var;
        if (day != 20 && day != 30) {
            var = chineseTen[((day - 1) / 10)] + chineseDay[((day - 1) % 10)];
        } else if (day != 20) {
            var = chineseTen[(day / 10)] + "十";
        } else {
            var = "二十";
        }
        return var;
    }

    /**
     * 时间换算
     *
     * @param ss 秒
     * @return
     */
    public String getReadTime(int ss) {
        if (ss < 60) {
            return ss + "秒";
        } else if (ss < 60 * 60) {
            return (ss / 60) + "分钟" + (ss % 60) + "秒";
        } else {
            int mm = (ss / 60) % 60;//分钟
            int hh = mm / (60 * 60);//小时
            return hh + "小时" + (mm % 60) + "分钟";
        }
    }

    /**
     * 获得持续时长  默认传入格式为"mm:ss"
     *
     * @param strtime
     * @return
     */
    public int getDuration(String strtime) {
        if (TextUtils.isEmpty(strtime)) {
            return 0;
        }
        String[] mytime;
        if (strtime.contains(":")) {
            mytime = strtime.split(":");
        } else if (strtime.contains("：")) {
            mytime = strtime.split("：");
        } else {
            mytime = new String[]{"0", "0"};
        }
        int min = Integer.parseInt(mytime[0]);
        int sec = Integer.parseInt(mytime[1]);

        int totalmillisecond = (min * 60 + sec) * 1000;

        return totalmillisecond;
    }
}
