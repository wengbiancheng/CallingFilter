package com.flamingo.filterdemo.aty;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.flamingo.filterdemo.R;
import com.flamingo.filterdemo.utils.DButil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qq on 2016/7/10.
 */
public class BlackModeAty extends Activity {



    private ImageButton title_leftBtn;
    private TextView title_middle;
    private ListView listView;
    private EditText addContent;
    private Button addBtn;

    private List<String> list = new ArrayList<String>();
    private ArrayAdapter<String> adapter;

    private DButil dButil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_blackmode);

        initView();
        initListener();
        initData();
    }

    @Override
    protected void onDestroy() {
        dButil.close();
        super.onDestroy();
    }

    private void initView() {
        title_leftBtn = (ImageButton) findViewById(R.id.title_leftImageBtn);
        title_leftBtn.setVisibility(View.VISIBLE);
        listView = (ListView) findViewById(R.id.mode_black_listView);
        addContent = (EditText) findViewById(R.id.mode_black_editText);
        addBtn = (Button) findViewById(R.id.mode_black_btn);
        title_middle= (TextView) findViewById(R.id.title_middleTextView);
    }

    private void initListener() {
        title_leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BlackModeAty.this.finish();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ShowAlertDialogDelete(position);
                return false;
            }
        });
        //添加黑名单
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(addContent.getText().toString())) {
                    Toast.makeText(BlackModeAty.this, "请输入电话号码", Toast.LENGTH_SHORT).show();
                } else {
                    if (!judgePhoneNums(addContent.getText().toString())) {
                        Toast.makeText(BlackModeAty.this, "请输入正确的电话号码", Toast.LENGTH_SHORT).show();
                    } else {
                        dButil.addPhone(addContent.getText().toString());
                        list.add(addContent.getText().toString());
                        addContent.setText("");
                        Toast.makeText(BlackModeAty.this, "添加黑名单成功", Toast.LENGTH_SHORT).show();
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    /**
     * 获取黑名单列表
     */
    private void initData() {
        title_middle.setText("拦截黑名单设置");
        dButil = new DButil(this);
        list = dButil.getAllPhone();
        if (list!=null) {
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, list);
        }else{
            list=new ArrayList<String>();
            adapter=new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1, list);
        }

        listView.setAdapter(adapter);
    }

    /**
     * 删除黑名单
     *
     * @param position
     */
    private void ShowAlertDialogDelete(final int position) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).setMessage("您真的要删除这个电话号码吗?").setPositiveButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setNegativeButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dButil.delPhone(list.get(position));
                list.remove(position);
                adapter.notifyDataSetChanged();
                Toast.makeText(BlackModeAty.this, "删除黑名单成功", Toast.LENGTH_SHORT).show();
            }
        }).create();
        alertDialog.show();
    }

    /**
     * 判断手机号码是否合理
     *
     * @param phoneNums
     */
    private boolean judgePhoneNums(String phoneNums) {
        if (isMatchLength(phoneNums, 11) && isMobileNO(phoneNums)) {
            return true;
        }
        Toast.makeText(this, "手机号码输入有误！", Toast.LENGTH_SHORT).show();
        return false;
    }

    /**
     * 判断一个字符串的位数
     *
     * @param str
     * @param length
     * @return
     */
    public static boolean isMatchLength(String str, int length) {
        if (str.isEmpty()) {
            return false;
        } else {
            return str.length() == length ? true : false;
        }
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobileNums) {
        /*
         * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
		 * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		 */
        String telRegex = "[1][358]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobileNums))
            return false;
        else
            return mobileNums.matches(telRegex);
    }
}
