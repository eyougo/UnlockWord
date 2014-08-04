package com.eyougo.unlockword.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.eyougo.unlockword.activity.LockActivity;
import com.eyougo.unlockword.receiver.ScreenOffReceiver;

public class LockService extends Service {

	private static String TAG = "UnlockWord.LockService";
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	public void onCreate(){
		super.onCreate();
		Log.i(TAG, "service create");
		
		/*注册广播*/
        mScreenOffReceiver = new ScreenOffReceiver();
		IntentFilter mScreenOffFilter = new IntentFilter("android.intent.action.SCREEN_OFF");
		LockService.this.registerReceiver(mScreenOffReceiver, mScreenOffFilter);
	}
	@Override
	public int onStartCommand(Intent intent , int flags , int startId){
		
		return Service.START_STICKY;
		
	}
	@Override
	public void onDestroy(){
		super.onDestroy();
		LockService.this.unregisterReceiver(mScreenOffReceiver);
		//在此重新启动
		startService(new Intent(LockService.this, LockService.class));
	}
	
	//屏幕变暗的广播
	private BroadcastReceiver mScreenOffReceiver;
	
}
