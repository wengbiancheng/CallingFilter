package com.flamingo.filterdemo.demo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.flamingo.filterdemo.R;
import com.flamingo.filterdemo.core.AbsHandler;
import com.flamingo.filterdemo.core.AbsTrigger;
import com.flamingo.filterdemo.core.BlockerBuilder;
import com.flamingo.filterdemo.core.IBlocker;
import com.flamingo.filterdemo.core.IFilter;
import com.flamingo.filterdemo.core.MessageData;
import com.flamingo.filterdemo.impl.NumeralFilter;
import com.flamingo.filterdemo.impl.PrefixFilter;

public class MainActivity extends Activity implements OnClickListener{
	
	/**
	 * 
	 * DEMO专用的Trigger，主要用于模拟器来电事件
	 * @author boyliang
	 *
	 */
	class DemoTrigger extends AbsTrigger{
		private boolean mState = false;
		
		@Override
		protected void enable() {
			mState = true;
		}

		@Override
		protected void disable() {
			mState = false;
		}
		
		public void emulateInComingCall(String phone){
			if(mState){
				MessageData data = new MessageData();
				data.setString(MessageData.KEY_DATA, phone);
				notify(data);
			}
		}
	}
	
	/**
	 * DEMO专用的Handler，主要用于更新ListView列表数据
	 * @author boyliang
	 */
	class DemoHandler extends AbsHandler{
		
		public void handle(MessageData data){
			int opcode = data.getInt(MessageData.KEY_OP);
			String phone = data.getString(MessageData.KEY_DATA);
			
			// 刷新数据列表
			String phonestr = String.format("(%s)%s", 
					(opcode == IFilter.OP_BLOCKED) ? "拦截" : 
						((opcode == IFilter.OP_PASS) ? "放行" : "跳过"), phone);
			String datestr = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", 
					Locale.CHINA).format(new Date());
			
			Bundle bundle = new Bundle();
			bundle.putString("phone", phonestr);
			bundle.putString("date", datestr);
			
			Message msg = mUIHandler.obtainMessage();
			msg.what = 11;
			msg.setData(bundle);
			
			msg.sendToTarget();
		}
	}
	

	private Button mBTEmulate;
	private TextView mTVPhone;
	private ListView mLVHistory;
	private LinkedList<Map<String, String>> mHistoryData = new LinkedList<Map<String, String>>();
	private IBlocker mBlocker;
	private AbsTrigger mTrigger = new DemoTrigger();
	private AbsHandler mHandler = new DemoHandler();
	
	private ListAdapter mAdapter;
	private Handler mUIHandler;
	
	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// View引用初始化
		mBTEmulate = (Button) findViewById(R.id.bt_emulate);
		mTVPhone = (TextView) findViewById(R.id.ed_phonenumber);
		mLVHistory = (ListView) findViewById(R.id.ls_history);
		
		//回调设置
		mBTEmulate.setOnClickListener(this);
		
		//数据绑定
		mAdapter = new SimpleAdapter(this, mHistoryData, android.R.layout.simple_list_item_2, new String[]{"phone", "date"}, new int[]{android.R.id.text1, android.R.id.text2});
		mLVHistory.setAdapter(mAdapter);
	
		//Blocker设置
		setupBlocker();
		
		//经过过滤后获得相应的号码的结果，可以根据结果来进行相应的处理。如拦截的话，直接进行挂断，如果
		mUIHandler = new Handler(){
			
			@Override
			public void handleMessage(Message msg) {
				String phonestr = msg.getData().getString("phone");
				String datestr = msg.getData().getString("date");


				HashMap<String, String> item = new HashMap<String, String>();
				item.put("phone", phonestr);
				item.put("date", datestr);
				
				mHistoryData.addFirst(item);
				mLVHistory.setAdapter(mAdapter);
				mLVHistory.invalidate();
				
				// 通知栏提示
				Context context = MainActivity.this;
				NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
				Notification.Builder builder = new Notification.Builder(context);
				Notification notification = builder
						.setContentTitle(phonestr)
						.setContentText(datestr)
						.setSmallIcon(android.R.drawable.ic_dialog_info)
						.build();
				
				notificationManager.notify(22, notification);
			}
		};
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	private void setupBlocker(){
		BlockerBuilder builder = new BlockerBuilder();
		
		mBlocker = builder
				.setTrigger(mTrigger)
				.setHandler(mHandler)
				.addFilters(new NumeralFilter(IFilter.OP_PASS, "95555", "95588")) 		 //实现白名单放行
				.addFilters(new NumeralFilter(IFilter.OP_BLOCKED, "106223", "107445"))   //实现黑名单放行
				.addFilters(new PrefixFilter(IFilter.OP_BLOCKED, "156", "10086", "134")) //前缀拦截
//				.addFilters(new LocationFilter()) //实现归属地拦截， 进阶课程的内容
//				.addFilters(new SystemContactFilter()) //系统联系人过滤， 进阶课程的内容
				.create();
		
		mBlocker.enable();
	}

	@Override
	public void onClick(View v) {
		String input = mTVPhone.getText().toString();
		mTVPhone.setText("");
		((DemoTrigger)mTrigger).emulateInComingCall(input);
	}
}
