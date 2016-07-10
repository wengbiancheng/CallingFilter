package com.flamingo.filterdemo.impl;

import android.util.Log;

import com.flamingo.filterdemo.core.AbsTrigger;
import com.flamingo.filterdemo.core.MessageData;

/**
 * 来电触发器，主要用于捕获来电事件， 作为进阶课程内容
 *
 * @author boyliang
 */
public final class InCallingTrigger extends AbsTrigger {

    private boolean mState = false;

    @Override
    protected void enable() {
        mState = true;
    }

    @Override
    protected void disable() {
        mState = false;
    }

    public void catchInComingCall(String phone) {
        if (mState) {
            Log.i("PhoneService", "Trggier触发，调用过滤器");
            MessageData data = new MessageData();
            data.setString(MessageData.KEY_DATA, phone);
            notify(data);
        }
}


}
