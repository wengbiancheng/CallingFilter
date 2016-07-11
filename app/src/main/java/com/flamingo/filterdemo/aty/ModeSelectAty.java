package com.flamingo.filterdemo.aty;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.flamingo.filterdemo.R;
import com.flamingo.filterdemo.entity.ModeSelect;
import com.flamingo.filterdemo.utils.ModeSelectKeeper;

/**
 * Created by qq on 2016/7/10.
 */
public class ModeSelectAty extends Activity implements View.OnClickListener{


    private CheckBox time,black,no,place;
    private TextView time1,black1,no1,place1;
    private String timeCheck="0",blackCheck="0",noCheck="0",placeCheck="0";
    private ImageButton title_leftBtn;
    private Button SureBtn;

    private ModeSelect modeSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_select);
        initView();


        initData();
    }
    private void initView(){
        time= (CheckBox) findViewById(R.id.select_time);
        black= (CheckBox) findViewById(R.id.select_black);
        no= (CheckBox) findViewById(R.id.select_no);
        place= (CheckBox) findViewById(R.id.select_place);
        time1= (TextView) findViewById(R.id.select_time1);
        black1= (TextView) findViewById(R.id.select_black1);
        no1= (TextView) findViewById(R.id.select_no1);
        place1= (TextView) findViewById(R.id.select_place1);
        time.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.i("TestActivity","OnChecked触发1");
                if(isChecked){
                    timeCheck="1";
                }else{
                    timeCheck="0";
                }
            }
        });
        black.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.i("TestActivity","OnChecked触发2");
                if(isChecked){
                    blackCheck="1";
                }else{
                    blackCheck="0";
                }
            }
        });
        no.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.i("TestActivity","OnChecked触发3");
                if(isChecked){
                    noCheck="1";
                }else{
                    noCheck="0";
                }
            }
        });
        place.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.i("TestActivity","OnChecked触发4");
                if(isChecked){
                    placeCheck="1";
                }else{
                    placeCheck="0";
                }
            }
        });
        time1.setOnClickListener(this);
        black1.setOnClickListener(this);
        no1.setOnClickListener(this);
        place1.setOnClickListener(this);

        title_leftBtn= (ImageButton) findViewById(R.id.title_leftImageBtn);
        title_leftBtn.setVisibility(View.VISIBLE);
        title_leftBtn.setOnClickListener(this);

        SureBtn= (Button) findViewById(R.id.select_btn);
        SureBtn.setOnClickListener(this);
    }

    private void initData(){
        modeSelect= ModeSelectKeeper.readAccessDate(this);
        timeCheck=modeSelect.getModeOne();
        blackCheck=modeSelect.getModeTwo();
        noCheck=modeSelect.getModeThree();
        placeCheck=modeSelect.getModeFour();

        time.setChecked(timeCheck.equals("1")?true:false);
        black.setChecked(blackCheck.equals("1")?true:false);
        no.setChecked(noCheck.equals("1")?true:false);
        place.setChecked(placeCheck.equals("1")?true:false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.select_time1:
                Log.i("TestActivity","OnClick触发1");
                Intent intent1=new Intent(ModeSelectAty.this,TimeModeAty.class);
                startActivity(intent1);
                break;
            case R.id.select_black1:
                Log.i("TestActivity","OnClick触发2");
                Intent intent2=new Intent(ModeSelectAty.this,BlackModeAty.class);
                startActivity(intent2);
                break;
            case R.id.select_no1:
                Log.i("TestActivity","OnClick触发3");
                break;
            case R.id.select_place1:
                Log.i("TestActivity","OnClick触发4");
                Intent intent3=new Intent(this,LocationModeAty.class);
                startActivity(intent3);
                break;
            case R.id.select_btn:
                ModeSelectKeeper.writeAccessData(ModeSelectAty.this, timeCheck, blackCheck, noCheck, placeCheck);
                Toast.makeText(this,"模式选择保存成功",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(ModeSelectAty.this,MyActivity.class);
                startActivity(intent);
                break;
            case R.id.title_leftImageBtn:
                ModeSelectAty.this.finish();
                break;
        }
    }
}
