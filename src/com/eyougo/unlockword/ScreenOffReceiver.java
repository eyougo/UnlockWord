package com.eyougo.unlockword;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ScreenOffReceiver extends BroadcastReceiver{
	private static String TAG = "ScreenOffReceiver";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(TAG, "receive screen off broadcast" + intent.toString());
		Intent lockIntent = new Intent(context , MainActivity.class);
		lockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(lockIntent);
	}

}
