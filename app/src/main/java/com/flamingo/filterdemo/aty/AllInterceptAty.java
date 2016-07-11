package com.flamingo.filterdemo.aty;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.flamingo.filterdemo.R;

/**
 * Created by qq on 2016/7/11.
 */
public class AllInterceptAty extends Activity {

    private ListView listView;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_all_intercept);

        initView();
    }
    private void initView(){
        listView= (ListView) findViewById(R.id.intercept_listView);
    }
}
