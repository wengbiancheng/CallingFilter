package com.flamingo.filterdemo.aty;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.flamingo.filterdemo.R;
import com.flamingo.filterdemo.adapter.LocationAdapter;
import com.flamingo.filterdemo.utils.DButil1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qq on 2016/7/11.
 */
public class LocationModeAty extends Activity {

    private ImageButton title_leftBtn;

    private DButil1 dButil1;
    private ListView listView;
    private LocationAdapter adapter;
    private Button SureBtn;
    private List<String> list=new ArrayList<String>();
    private List<String> listLocation=new ArrayList<String>();
    private final String s="北京市，天津市，重庆市，上海市，河北省，山西省，辽宁省，吉林省，" +
            "黑龙江省，江苏省，浙江省，安徽省，福建省，江西省，山东省，河南省，湖北省，湖南省，" +
            "广东，海南省，四川省，贵州省，云南省，陕西省，甘肃省，青海省，台湾省，内蒙古自治区，广西壮族自治区，" +
            "西藏自治区，宁夏回族自治区，新疆维吾尔自治区，香港特别行政区，澳门特别行政区";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_locationmode);
        initView();
        initListener();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dButil1.close();
    }

    private void initView(){
        listView= (ListView) findViewById(R.id.mode_location_listView);
        SureBtn= (Button) findViewById(R.id.mode_location_btn);
        title_leftBtn= (ImageButton) findViewById(R.id.title_leftImageBtn);
        title_leftBtn.setVisibility(View.VISIBLE);
    }
    private void initListener(){
        SureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dButil1.clear();
                dButil1.addLocation(listLocation);
                Toast.makeText(LocationModeAty.this,"保存选择的归属地成功",Toast.LENGTH_SHORT).show();
                LocationModeAty.this.finish();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("PhoneService", "归属地拦截设置点击的一项是:" + list.get(position));
                LocationAdapter.ViewHolder holder = (LocationAdapter.ViewHolder) view.getTag();
                holder.cb.toggle();
                if (holder.cb.isChecked() == true) {
                    listLocation.add(list.get(position));
                } else {
                    listLocation.remove(list.get(position));
                }
                Log.i("PhoneService", "归属地拦截设置点击后的List的大小是:" + listLocation.size());
            }
        });
        title_leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationModeAty.this.finish();
            }
        });
    }
    private void initData(){
        String []split=s.split("，");
        for(String temp:split){
            list.add(temp);
        }


        dButil1=new DButil1(this);
        List<String> Location=dButil1.getAllLocation();
        if(Location==null){
            Location=new ArrayList<String>();
        }else{
            listLocation=Location;
        }
        Map<Integer,Boolean> map=new HashMap<Integer, Boolean>();
        for(int i=0;i<Location.size();i++){
            for(int j=0;j<list.size();j++){
                if(list.get(j).equals(Location.get(i))){
                    map.put(j,true);
                }
            }
        }
        adapter = new LocationAdapter(this, list, map);
        listView.setAdapter(adapter);
    }
}
