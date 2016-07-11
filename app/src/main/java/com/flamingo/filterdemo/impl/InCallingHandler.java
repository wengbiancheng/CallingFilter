package com.flamingo.filterdemo.impl;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;
import com.flamingo.filterdemo.core.AbsHandler;
import com.flamingo.filterdemo.core.IFilter;
import com.flamingo.filterdemo.core.MessageData;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 来电 处理器， 主要对经由各种Filter判断之后的结果进行处理，比如挂断来电，通知栏提醒等等
 * @author boyliang
 *
 */
public final class InCallingHandler extends AbsHandler {

	private Context context;
	public InCallingHandler(Context context){
		this.context=context;
	}
	@Override
	public void handle(MessageData data) {
		// TODO Auto-generated method stub 
		super.handle(data);

		int opcode = data.getInt(MessageData.KEY_OP);
		String phone = data.getString(MessageData.KEY_DATA);

		// 刷新数据列表
		String phonestr = String.format("(%s)%s",
				(opcode == IFilter.OP_BLOCKED) ? "拦截" :
						((opcode == IFilter.OP_PASS) ? "放行" : "跳过"), phone);
		String datestr = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss",
				Locale.CHINA).format(new Date());
		Log.i("PhoneService","处理后的结果是"+phonestr+";时间为:"+datestr);

		if(opcode==IFilter.OP_BLOCKED){
			try {
				ITelephony iTelephony = getITelephony((context.getApplicationContext())); //获取电话接口
				iTelephony.endCall(); // 挂断电话
				// 通知栏提示
				NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
				Notification.Builder builder = new Notification.Builder(context);
				Notification notification = builder
						.setContentTitle(phonestr)
						.setContentText(datestr)
						.setSmallIcon(android.R.drawable.ic_dialog_info)
						.build();

				notificationManager.notify(22, notification);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Log.i("PhoneService","handler处理完毕");
	}

	/**
	 * 根据反射获取end()方法2
	 * @param context
	 * @return
	 */
	private static ITelephony getITelephony(Context context) {
		ITelephony iTelephony = null;
		TelephonyManager mTelephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		Class<TelephonyManager> c = TelephonyManager.class;
		Method getITelephonyMethod = null;
		try {
			getITelephonyMethod = c.getDeclaredMethod("getITelephony",
					(Class[]) null); // 获取声明的方法
			getITelephonyMethod.setAccessible(true);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}

		try {
			iTelephony = (ITelephony) getITelephonyMethod.invoke(
					mTelephonyManager, (Object[]) null); // 获取实例
			return iTelephony;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return iTelephony;
	}

}
