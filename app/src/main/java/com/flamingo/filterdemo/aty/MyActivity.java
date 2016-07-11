package com.flamingo.filterdemo.aty;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
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
    private Button exit;
    private Button all;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_mymain);
        initView();
    }

    private void initView(){
        start= (Button) findViewById(R.id.my_start_service);
        stop= (Button) findViewById(R.id.my_stop_service);
        change= (Button) findViewById(R.id.my_change);
        exit= (Button) findViewById(R.id.my_exit);
        all= (Button) findViewById(R.id.my_all);
        start.setOnClickListener(this);
        stop.setOnClickListener(this);
        change.setOnClickListener(this);
        exit.setOnClickListener(this);
        all.setOnClickListener(this);
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
            case R.id.my_change:
                Intent intent2=new Intent(this,ModeSelectAty.class);
                startActivity(intent2);
                break;
            case R.id.my_exit:
                System.exit(0);
                break;
            case R.id.my_all:
                Intent intent3=new Intent(this,AllInterceptAty.class);
                startActivity(intent3);
                break;
        }
    }
}
