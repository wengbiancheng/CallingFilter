package com.flamingo.filterdemo.tool;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.flamingo.filterdemo.core.AbsHandler;
import com.flamingo.filterdemo.core.AbsTrigger;
import com.flamingo.filterdemo.core.BlockerBuilder;
import com.flamingo.filterdemo.core.IBlocker;
import com.flamingo.filterdemo.core.IFilter;
import com.flamingo.filterdemo.entity.ModeSelect;
import com.flamingo.filterdemo.impl.InCallingHandler;
import com.flamingo.filterdemo.impl.InCallingTrigger;
import com.flamingo.filterdemo.impl.LocationFilter;
import com.flamingo.filterdemo.impl.NumeralFilter;
import com.flamingo.filterdemo.impl.PrefixFilter;
import com.flamingo.filterdemo.impl.SystemContactFilter;
import com.flamingo.filterdemo.impl.TimeRangFilter;
import com.flamingo.filterdemo.utils.ModeSelectKeeper;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * 代替MainActivity来进行来电拦截的处理
 */
public class ServiceMain extends Service {

    private IBlocker mBlocker = null;
    private AbsTrigger mTrigger = new InCallingTrigger();
    private AbsHandler mHandler = new InCallingHandler(ServiceMain.this);

    private String phoneNumber;
    private BroadcastTrigger myBroadcast;

    private BlockerBuilder builder;
    private String province;

    private final String url = "https://tcc.taobao.com/cc/json/mobile_tel_segment.htm?tel=";

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
            getLoction(phoneNumber);
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
        builder = new BlockerBuilder();


        //自定义常见的白名单
        List<String> list = new ArrayList<String>();
        list.add("95588");
        list.add("110");

        ModeSelect modeSelect = ModeSelectKeeper.readAccessDate(this);
        String modeOne = modeSelect.getModeOne();
        String modeTwo = modeSelect.getModeTwo();
        String modeThree = modeSelect.getModeThree();
        String modeFour = modeSelect.getModeFour();

        mBlocker = builder
                .setTrigger(mTrigger)
                .setHandler(mHandler)
                .addFilters(new NumeralFilter(IFilter.OP_PASS, list))         //实现白名单放行.create();
                .create();

        if (modeOne.equals("1")) {
            builder.addFilters(new TimeRangFilter(this));
        }
        if (modeTwo.equals("1")) {
            builder.addFilters(new NumeralFilter(IFilter.OP_BLOCKED, ServiceMain.this));
        }
        if (modeThree.equals("1")) {
            builder.addFilters(new SystemContactFilter(ServiceMain.this));
        }
        if (modeFour.equals("1")) {
            if (!TextUtils.isEmpty(province)) {
                builder.addFilters(new LocationFilter(ServiceMain.this, province));
            } else {
                builder.addFilters(new LocationFilter(ServiceMain.this, null));
            }
        }
        mBlocker.enable();
        if (!TextUtils.isEmpty(phoneNumber)) {
            Log.i("PhoneService", "调用触发器");
            ((InCallingTrigger) mTrigger).catchInComingCall(phoneNumber);
        }
    }

    public void getLoction(final String phoneNumber) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                okHttpGet(url + phoneNumber);
            }
        }).start();
    }

    private void okHttpGet(String url) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Response response) throws IOException {
                String resStr = response.body().string();
                findProvince(resStr);
            }

            @Override
            public void onFailure(Request request, IOException e) {

            }
        });
    }

    private void findProvince(String jsonString) {

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < jsonString.length(); ++i) {
            if (jsonString.charAt(i) == 'p') {
                i += 10;
                for (int j = i; j < jsonString.length(); ++j) {
                    if (jsonString.charAt(j) == '\'') {
                        break;
                    }
                    stringBuilder.append(jsonString.charAt(j));
                }
                province = stringBuilder.toString();
                break;
            }
        }
    }


}
