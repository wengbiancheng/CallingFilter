package com.flamingo.filterdemo.impl;

import android.content.Context;
import android.util.Log;

import java.util.Calendar;

import com.flamingo.filterdemo.core.IFilter;
import com.flamingo.filterdemo.core.MessageData;
import com.flamingo.filterdemo.utils.TimeModeKeeper;

/**
 * 按时段过滤
 * @author boyliang
 *
 */
public final class TimeRangFilter implements IFilter {
	private int mStartHour;
	private int mEndHour;
	private int mStartMinute;
	private int mEndMinute;
	
	public TimeRangFilter(Context context){
		String[] timeSplit= TimeModeKeeper.readAccessDate(context).split(",");
		String startTime=timeSplit[0];
		String endTime=timeSplit[1];
		String []startSplit=startTime.split(":");
		String []endSplit=endTime.split(":");
		mStartHour=Integer.parseInt(startSplit[0]);
		mStartMinute=Integer.parseInt(startSplit[1]);
		mEndHour=Integer.parseInt(endSplit[0]);
		mEndMinute=Integer.parseInt(endSplit[1]);
		Log.i("PhoneService", "时间拦截初始化完成");
	}
	
	@Override
	public int onFiltering(MessageData data) {
//		String phone = data.getString(MessageData.KEY_DATA);

		Log.i("PhoneService", "时间拦截过滤触发");
		Calendar now = Calendar.getInstance();
		int current_hour = now.get(Calendar.HOUR_OF_DAY);
		int current_minute=now.get(Calendar.MINUTE);
		
		if(current_hour > mStartHour && current_hour < mEndHour){
			return IFilter.OP_BLOCKED;
		}else if(current_hour==mStartHour){
			if(current_minute>=mStartMinute){
				return IFilter.OP_BLOCKED;
			}
		}else if(current_hour==mEndHour){
			if(current_minute<=mEndMinute){
				return IFilter.OP_BLOCKED;
			}
		}
		return IFilter.OP_SKIP;
	}

}
