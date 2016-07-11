package com.flamingo.filterdemo.impl;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.flamingo.filterdemo.core.IFilter;
import com.flamingo.filterdemo.core.MessageData;
import com.flamingo.filterdemo.utils.DButil;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 基于号码严格匹配的过滤器
 * @author boyliang
 *
 */
public final class NumeralFilter implements IFilter {
	private int mOpcode;
	private List<String> list=new ArrayList<String>();
	private DButil dButil;
	
	public NumeralFilter(int opcode, List<String> list){
		mOpcode = opcode;
		this.list=list;
	}
	public NumeralFilter(int opcode,Context context){
		dButil=new DButil(context);
		mOpcode=opcode;
		this.list=dButil.getAllPhone();
		if(list==null){
			list=new ArrayList<String>();
			Log.i("PhoneService","黑名单拦截初始化的list的个数是:"+list.size());
		}
		Log.i("PhoneService","黑名单拦截初始化完成");
	}
	
	@Override
	public int onFiltering(MessageData data) {
		Log.i("PhoneService","黑名单过滤触发");
		String phone = data.getString(MessageData.KEY_DATA);
		
		for(int i=0;i<list.size();i++){
			if(!TextUtils.isEmpty(list.get(i)) && phone.equals(list.get(i))){
				return mOpcode;
			}
		}
		return IFilter.OP_SKIP;
	}
}
