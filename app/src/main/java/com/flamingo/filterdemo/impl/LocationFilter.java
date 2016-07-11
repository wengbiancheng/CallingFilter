package com.flamingo.filterdemo.impl;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.flamingo.filterdemo.aty.LocationModeAty;
import com.flamingo.filterdemo.core.IFilter;
import com.flamingo.filterdemo.core.MessageData;
import com.flamingo.filterdemo.utils.DButil1;

import java.util.ArrayList;
import java.util.List;

/**
 * 基于号码归属地的过滤器，走网络请求，所以需求时间，作为进阶课程内容
 * HTTP API: https://tcc.taobao.com/cc/json/mobile_tel_segment.htm?tel=1111111111
 *
 * @author boyliang
 */
public final class LocationFilter implements IFilter {


    private Context context;
    private String province;
    private List<String> list = new ArrayList<String>();
    private DButil1 dButil1;

    public LocationFilter(Context context, String province) {
        this.context = context;
        this.province = province;
        this.dButil1 = new DButil1(context);
        getData();
        Log.i("PhoneService", "归属地拦截初始化,要拦截的归属地有:" + list.size());
    }

    private void getData() {
        list = dButil1.getAllLocation();
        if (list == null) {
            list = new ArrayList<String>();
        }
    }

    @Override
    public int onFiltering(MessageData data) {

        String phone = data.getString(MessageData.KEY_DATA);

        Log.i("PhoneService", "归属地拦截过滤触发;打电话过来的归属地是:"+province);
        if (province == null){
            return IFilter.OP_SKIP;
        }else{
            for (int i = 0; i < list.size(); i++)
            {
                if (list.get(i).equals(province)) {
                    return IFilter.OP_BLOCKED;
                }
            }
            return IFilter.OP_SKIP;
        }
    }

}
