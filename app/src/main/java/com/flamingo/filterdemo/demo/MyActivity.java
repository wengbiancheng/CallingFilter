package com.flamingo.filterdemo.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.flamingo.filterdemo.R;
import com.flamingo.filterdemo.tool.ServiceMain;

/**
 * Created by qq on 2016/7/10.
 */
public class MyActivity extends Activity implements View.OnClickListener{


    private Button start;
    private Button stop;
    private Button change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mymain);
        initView();
    }

    private void initView(){
        start= (Button) findViewById(R.id.my_start_service);
        stop= (Button) findViewById(R.id.my_stop_service);
        start.setOnClickListener(this);
        stop.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.my_start_service:
                Intent intent=new Intent(this, ServiceMain.class);
                startService(intent);
                break;
            case R.id.my_stop_service:
                Intent intent1=new Intent(this, ServiceMain.class);
                stopService(intent1);
                break;
        }
    }
}
