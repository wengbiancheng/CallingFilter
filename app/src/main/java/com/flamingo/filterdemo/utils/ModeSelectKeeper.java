package com.flamingo.filterdemo.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.flamingo.filterdemo.entity.ModeSelect;

/**
 * Created by qq on 2016/7/10.
 */
public class ModeSelectKeeper {

    private static final String USER_DATA="select_mode";

    private static final String MODE_ONE="mode_time";
    private static final String MODE_TWO="mode_black";
    private static final String MODE_THREE="mode_no";
    private static final String MODE_FOUR="mode_place";


    /**
     * 保存各个模式的选择到本地中去
     * @param context
     * @param modeOne
     * @param modeTwo
     * @param modeThree
     * @param modeFour
     */
    public static void writeAccessData(Context context,String modeOne,String modeTwo,String modeThree,String modeFour){
        if(context==null){
            return;
        }

        SharedPreferences pref=context.getSharedPreferences(USER_DATA,Context.MODE_APPEND);
        SharedPreferences.Editor editor=pref.edit();
        editor.putString(MODE_ONE,modeOne);
        editor.putString(MODE_TWO,modeTwo);
        editor.putString(MODE_THREE,modeThree);
        editor.putString(MODE_FOUR,modeFour);
        editor.commit();
    }

    /**
     * 从SharedPreference中读取出数据
     * @param context
     * @return
     */
    public static ModeSelect readAccessDate(Context context){
        if(context==null){
            return null;
        }
        ModeSelect modeSelect=new ModeSelect();
        SharedPreferences pref=context.getSharedPreferences(USER_DATA, Context.MODE_APPEND);
        modeSelect.setModeOne(pref.getString(MODE_ONE,"0"));
        modeSelect.setModeTwo(pref.getString(MODE_TWO, "0"));
        modeSelect.setModeThree(pref.getString(MODE_THREE, "0"));
        modeSelect.setModeFour(pref.getString(MODE_FOUR,"0"));
        return modeSelect;
    }

    /**
     * 清楚所有的信息
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
