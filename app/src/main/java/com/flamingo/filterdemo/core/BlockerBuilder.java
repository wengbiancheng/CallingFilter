package com.flamingo.filterdemo.core;

import android.util.Log;

import java.util.ArrayList;

/**
 * 拦截器构造者，用于Blocker的配置及构造
 * @author boyliang
 */
public final class BlockerBuilder {
	
	/**
	 * IBlocker的内部实现，隐藏Blocker的实现细节
	 * @author boyliang
	 *
	 */
	static final class BlockerImpl implements IBlocker, AbsTrigger.ITriggerListener{
		private AbsTrigger mTrigger;
		private ArrayList<IFilter> mFilters = new ArrayList<IFilter>();
		private AbsHandler mHandler;
		
		@Override
		public void enable() {
			if(mTrigger != null){
				mTrigger.enable();
			}else{
				throw new RuntimeException("mTrigger must be set.");
			}
		}

		@Override
		public void disable() {
			if(mTrigger != null){
				mTrigger.disable();
			}else{
				throw new RuntimeException("mTrigger must be set.");
			}
		}

		@Override
		public void onMessageComing(MessageData data) {
			int opcode = IFilter.OP_PASS;

			Log.i("PhoneService","开始进行过滤号码操作");
			for(IFilter filter : mFilters){
				opcode = filter.onFiltering(data);
				//通过或者拦截
				if(opcode == IFilter.OP_PASS || opcode == IFilter.OP_BLOCKED){
					break;
				}
			}

			data.setInt(MessageData.KEY_OP, opcode);
			Log.i("PhoneService","筛选之后的号码序号是："+opcode+";电话号码是:"+data.getString(MessageData.KEY_DATA));
			
			if(mHandler != null){
				Log.i("PhoneService","过滤完成，开始调用handler处理器");
				mHandler.handle(data);
			}
		}
		
		public void addFilters(IFilter filter){
			mFilters.add(filter);
		}
		
		public void setTrigger(AbsTrigger trigger){
			if(mTrigger != null){
				mTrigger.setListener(null);
			}
			
			mTrigger = trigger;
			
			if(mTrigger != null){
				mTrigger.setListener(this);
			}
		}
		
		public void setHandler(AbsHandler handler){
			mHandler = handler;
		}
	}
	
	private BlockerImpl mBlocker;
	
	public BlockerBuilder(){
		mBlocker = new BlockerImpl();
		mBlocker.setTrigger(new AbsTrigger() {
			
			@Override
			protected void enable() {
				//DO NOTHING
			}
			
			@Override
			protected void disable() {
				//DO NOTHING
			}
		});
		mBlocker.setHandler(new AbsHandler(){
			
		});
		
	}
	
	public IBlocker create(){
		return mBlocker;
	}
	
	/**
	 * 添加 过滤器
	 * @param filter
	 */
	public BlockerBuilder addFilters(IFilter filter){
		mBlocker.addFilters(filter);
		return this;
	}
	
	/**
	 * 配置 触发器
	 * @param trigger
	 */
	public BlockerBuilder setTrigger(AbsTrigger trigger){
		mBlocker.setTrigger(trigger);
		return this;
	}
	
	/**
	 * 配置 处理器
	 * @param handler
	 */
	public BlockerBuilder setHandler(AbsHandler handler){
		mBlocker.setHandler(handler);
		return this;
	}
}
