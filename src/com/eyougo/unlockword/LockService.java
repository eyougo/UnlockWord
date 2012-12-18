package com.eyougo.unlockword;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

public class LockService extends Service {

	private static String TAG = "LockService";
	private Intent lockIntent = null ;
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	public void onCreate(){
		super.onCreate();
		Log.i(TAG, "service create");
		
		lockIntent = new Intent(LockService.this , MainActivity.class);
		//lockIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT); 
		lockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
		
		/*注册广播*/
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
	private BroadcastReceiver mScreenOffReceiver = new BroadcastReceiver(){
		@Override
		public void onReceive(Context context , Intent intent) {
			String action = intent.getAction() ;
			
		    Log.i(TAG, intent.toString());

			if(action.equals("android.intent.action.SCREEN_OFF") ){
				startActivity(lockIntent);
			}
		}
		
	};
	
}
