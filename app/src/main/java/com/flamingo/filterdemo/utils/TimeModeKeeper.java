package com.flamingo.filterdemo.utils;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by qq on 2016/7/11.
 */
public class TimeModeKeeper {

    private static final String USER_DATA="mode_time";

    private static final String StartTime="startTime";
    private static final String EndTime="endTime";


    /**
     * 保存时间到本地去
     * @param context
     * @param startTime
     * @param endTime
     */
    public static void writeAccessData(Context context,String startTime,String endTime){
        if(context==null){
            return;
        }

        SharedPreferences pref=context.getSharedPreferences(USER_DATA,Context.MODE_APPEND);
        SharedPreferences.Editor editor=pref.edit();
        editor.putString(StartTime,startTime);
        editor.putString(EndTime,endTime);
        editor.commit();
    }

    /**
     * 从SharedPreference中读取出数据
     * @param context
     * @return
     */
    public static String readAccessDate(Context context){
        if(context==null){
            return null;
        }
        SharedPreferences pref=context.getSharedPreferences(USER_DATA, Context.MODE_APPEND);
        return pref.getString(StartTime,"00:00")+","+pref.getString(EndTime,"00:00");
    }

    /**
     * 清除所有的信息
     * @param context
     */
    public static void clear(Context context){
        if(context==null){
            return;
        }

        SharedPreferences pref=context.getSharedPreferences(USER_DATA,Context.MODE_APPEND);
        SharedPreferences.Editor editor=pref.edit();
        editor.clear();
        editor.commit();
    }
}
