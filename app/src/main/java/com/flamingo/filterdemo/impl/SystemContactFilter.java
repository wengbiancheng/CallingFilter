package com.flamingo.filterdemo.impl;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.TextUtils;
import android.util.Log;

import com.flamingo.filterdemo.core.IFilter;
import com.flamingo.filterdemo.core.MessageData;

import java.util.ArrayList;
import java.util.List;

/**
 * 系统联系人过滤器， 进阶课程内容
 * 
 * @author boyliang
 *
 */
public final class SystemContactFilter implements IFilter {

	private Context context;
	private static final String[] PHONES_PROJECTION = new String[] {
			Phone.NUMBER };
	private List<String> list=new ArrayList<String>();

	public SystemContactFilter(Context context){
		this.context=context;
		getData();
		Log.i("PhoneService", "陌生人拦截初始化完成");
	}

	/**
	 * 获取系统联系人的手机号码
	 */
	private void getData(){
		ContentResolver resolver=context.getContentResolver();

		Cursor phoneCursor = resolver.query(Phone.CONTENT_URI,PHONES_PROJECTION, null, null, null);
		if(phoneCursor!=null){
			while(phoneCursor.moveToNext()){
				String phoneNumber=phoneCursor.getString(0);
				if(!TextUtils.isEmpty(phoneNumber)){
					list.add(phoneNumber);
				}
			}
		}
		Log.i("PhoneService", "陌生人拦截初始化的list的个数是:"+list.size());
	}

	@Override
	public int onFiltering(MessageData data) {
		String phone = data.getString(MessageData.KEY_DATA);

		Log.i("PhoneService","陌生人拦截过滤触发");
		for(int i=0;i<list.size();i++){
			if(!TextUtils.isEmpty(list.get(i)) && phone.equals(list.get(i))){
				return IFilter.OP_PASS;
			}
		}
		return IFilter.OP_BLOCKED;
	}


}
