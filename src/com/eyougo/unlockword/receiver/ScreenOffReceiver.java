package com.eyougo.unlockword.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

import com.eyougo.unlockword.activity.LockActivity;

/**
 * Created by mei on 8/4/14.
 */
public class ScreenOffReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context , Intent intent) {
        String action = intent.getAction() ;

        if(action.equals("android.intent.action.SCREEN_OFF") ){
            TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService((Context.TELEPHONY_SERVICE));
            int callState = telephonyManager.getCallState();

            if (callState != TelephonyManager.CALL_STATE_OFFHOOK
                    && callState != TelephonyManager.CALL_STATE_RINGING){


                Intent lockIntent = new Intent(context , LockActivity.class);
                //lockIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                lockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                lockIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                context.startActivity(lockIntent);
            }
        }
    }
}
