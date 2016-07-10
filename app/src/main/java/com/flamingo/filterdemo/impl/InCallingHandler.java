package com.flamingo.filterdemo.impl;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.flamingo.filterdemo.core.AbsHandler;
import com.flamingo.filterdemo.core.IFilter;
import com.flamingo.filterdemo.core.MessageData;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 来电 处理器， 主要对经由各种Filter判断之后的结果进行处理，比如挂断来电，通知栏提醒等等
 * @author boyliang
 *
 */
public final class InCallingHandler extends AbsHandler {
	
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
	}
}
