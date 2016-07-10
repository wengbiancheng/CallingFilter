package com.flamingo.filterdemo.impl;

import com.flamingo.filterdemo.core.IFilter;
import com.flamingo.filterdemo.core.MessageData;

/**
 * 
 * 基于号码严格匹配的过滤器
 * @author boyliang
 *
 */
public final class NumeralFilter implements IFilter {
	private String[] mNumbers;
	private int mOpcode;
	
	public NumeralFilter(int opcode, String ...numbers){
		mOpcode = opcode;
		mNumbers = numbers;
	}
	
	@Override
	public int onFiltering(MessageData data) {
		String phone = data.getString(MessageData.KEY_DATA);
		
		for(String number : mNumbers){
			if(number != null && number.equals(phone)){
				return mOpcode;
			}
		}
		
		return IFilter.OP_SKIP;
	}
}
