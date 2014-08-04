package com.eyougo.unlockword.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

import com.eyougo.unlockword.layer.LockLayer;

/**
 * Created by mei on 8/4/14.
 */
public class PhoneCallReceiver extends BroadcastReceiver {

    private static LockLayer mLockLayer;

    public PhoneCallReceiver(LockLayer mLockLayer) {
        this.mLockLayer = mLockLayer;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        String phoneState = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

        if (TelephonyManager.EXTRA_STATE_RINGING.equals(phoneState)
                || TelephonyManager.EXTRA_STATE_OFFHOOK.equals(phoneState)){
            mLockLayer.unlock();
        } else {
            mLockLayer.lock();
        }


    }
}
