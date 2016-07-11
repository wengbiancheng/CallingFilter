package com.flamingo.filterdemo.aty;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.flamingo.filterdemo.R;
import com.flamingo.filterdemo.utils.TimeModeKeeper;

/**
 * Created by qq on 2016/7/10.
 */
public class TimeModeAty extends Activity implements View.OnClickListener {

    private EditText startTime, endTime;
    private Button btn;
    private ImageButton title_leftBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_timemode);

        initView();
        initListener();
        initData();
    }

    private void initView() {
        startTime = (EditText) findViewById(R.id.mode_time_edit1);
        endTime = (EditText) findViewById(R.id.mode_time_edit2);
        btn = (Button) findViewById(R.id.mode_time_btn);
        title_leftBtn = (ImageButton) findViewById(R.id.title_leftImageBtn);
        title_leftBtn.setVisibility(View.VISIBLE);
    }

    private void initListener() {
        startTime.setOnClickListener(this);
        endTime.setOnClickListener(this);
        btn.setOnClickListener(this);
        title_leftBtn.setOnClickListener(this);
    }

    private void initData() {
        String TotalTime = TimeModeKeeper.readAccessDate(this);
        String[] timespilt = TotalTime.split(",");
        startTime.setText(timespilt[0]);
        endTime.setText(timespilt[1]);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mode_time_edit1:
                SelectTime(0);
                break;
            case R.id.mode_time_edit2:
                SelectTime(1);
                break;
            case R.id.mode_time_btn:
                if (!TextUtils.isEmpty(startTime.getText().toString()) && !TextUtils.isEmpty(endTime.getText().toString())) {
                    TimeModeKeeper.writeAccessData(this, startTime.getText().toString(), endTime.getText().toString());
                    Toast.makeText(this, "保存时间段成功", Toast.LENGTH_SHORT).show();
                    TimeModeAty.this.finish();
                }
                break;
            case R.id.title_leftImageBtn:
                TimeModeAty.this.finish();
                break;
        }
    }

    private void SelectTime(final int flag) {

        int hourOfDay;
        int minute;
        if (flag == 0) {
            String[] timeSpilt = startTime.getText().toString().split(":");
            hourOfDay = Integer.parseInt(timeSpilt[0]);
            minute = Integer.parseInt(timeSpilt[1]);
        } else {
            String[] timeSpilt = endTime.getText().toString().split(":");
            hourOfDay = Integer.parseInt(timeSpilt[0]);
            minute = Integer.parseInt(timeSpilt[1]);
        }


        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if (flag == 0) {

                    String[] spilt = endTime.getText().toString().split(":");
                    int EndHour = Integer.parseInt(spilt[0]);
                    int EndMinute = Integer.parseInt(spilt[1]);
                    if (hourOfDay > EndHour) {
                        endTime.setText("0:"+minute);
                    } else if (hourOfDay == EndHour && minute >EndMinute) {
                        endTime.setText(hourOfDay + ":0");
                    }else if(hourOfDay<EndHour){
                        endTime.setText(hourOfDay+":"+minute);
                    }
                } else if (flag == 1) {
                    String[] spilt = startTime.getText().toString().split(":");
                    int StartHour = Integer.parseInt(spilt[0]);
                    int StartMinute = Integer.parseInt(spilt[1]);
                    if (hourOfDay < StartHour) {
                        endTime.setText("23:"+minute);
                    } else if (hourOfDay == StartHour && minute < StartMinute) {
                        endTime.setText(hourOfDay + ":59");
                    }else if(hourOfDay>StartHour){
                        endTime.setText(hourOfDay+":"+minute);
                    }
                }
            }
        }, hourOfDay, minute, true);

        timePickerDialog.show();
    }
}
