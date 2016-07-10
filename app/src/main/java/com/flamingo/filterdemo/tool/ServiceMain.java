package com.flamingo.filterdemo.tool;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import com.flamingo.filterdemo.core.AbsHandler;
import com.flamingo.filterdemo.core.AbsTrigger;
import com.flamingo.filterdemo.core.BlockerBuilder;
import com.flamingo.filterdemo.core.IBlocker;
import com.flamingo.filterdemo.core.IFilter;
import com.flamingo.filterdemo.impl.InCallingHandler;
import com.flamingo.filterdemo.impl.InCallingTrigger;
import com.flamingo.filterdemo.impl.NumeralFilter;
import com.flamingo.filterdemo.impl.PrefixFilter;


/**
 * 代替MainActivity来进行来电拦截的处理
 */
public class ServiceMain extends Service {

    private IBlocker mBlocker = null;
    private AbsTrigger mTrigger = new InCallingTrigger();
    private AbsHandler mHandler = new InCallingHandler();

    private String phoneNumber;
    private BroadcastTrigger myBroadcast;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.i("PhoneService", "开启服务onCreate");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("PhoneService", "开启服务onStartCommand");
        if (!TextUtils.isEmpty(intent.getStringExtra("phone"))) {
            phoneNumber = intent.getStringExtra("phone");
        }
        setupBlocker();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.i("PhoneService", "关闭服务OnDestroy");
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void setupBlocker() {
        BlockerBuilder builder = new BlockerBuilder();

        mBlocker = builder
                .setTrigger(mTrigger)
                .setHandler(mHandler)
                .addFilters(new NumeralFilter(IFilter.OP_PASS, "95555", "95588"))         //实现白名单放行
                .addFilters(new NumeralFilter(IFilter.OP_BLOCKED, "106223", "107445"))   //实现黑名单放行
                .addFilters(new PrefixFilter(IFilter.OP_BLOCKED, "156", "10086", "134", "188")) //前缀拦截
//				.addFilters(new LocationFilter()) //实现归属地拦截， 进阶课程的内容
//				.addFilters(new SystemContactFilter()) //系统联系人过滤， 进阶课程的内容
                .create();

        mBlocker.enable();
        if (!TextUtils.isEmpty(phoneNumber)) {
            Log.i("PhoneService", "调用触发器");
            ((InCallingTrigger) mTrigger).catchInComingCall(phoneNumber);
        }
    }


}
