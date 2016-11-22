package com.flamingo.filterdemo.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.flamingo.filterdemo.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qq on 2016/7/11.
 */
public class LocationAdapter extends BaseAdapter {

    private Context context;

    //更新分支操作
    //分支操作成功
    private Handler handler;
    private List<String> list = new ArrayList<String>();
    Map<Integer, Boolean> isCheckMap = new HashMap<Integer, Boolean>();

    public LocationAdapter(Context context, List<String> list,  Map<Integer, Boolean> isCheckMap) {
        this.context = context;
        this.list = list;
        this.isCheckMap=isCheckMap;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            // 获得ViewHolder对象
            holder = new ViewHolder();
            // 导入布局并赋值给convertview
            convertView = LayoutInflater.from(context).inflate(R.layout.item, null);
            holder.tv = (TextView) convertView.findViewById(R.id.item_textView);
            holder.cb = (CheckBox) convertView.findViewById(R.id.item_checkbox);
            // 为view设置标签
            convertView.setTag(holder);
        } else {
            // 取出holder
            holder = (ViewHolder) convertView.getTag();
        }
        // 设置list中TextView的显示
        holder.tv.setText(list.get(position));

        holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //将选中的放入hashmap中
                    isCheckMap.put(position, isChecked);
                } else {
                    //取消选中的则剔除
                    isCheckMap.remove(position);
                }
            }
        });

        //找到需要选中的条目
        if (isCheckMap.size()>0 && isCheckMap != null && isCheckMap.containsKey(position)) {
            holder.cb.setChecked(true);
        } else {
            holder.cb.setChecked(false);
        }
        return convertView;
    }

    public static class ViewHolder {
        public TextView tv;
        public CheckBox cb;
    }
}
