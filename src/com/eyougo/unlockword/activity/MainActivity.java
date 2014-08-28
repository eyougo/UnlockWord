package com.eyougo.unlockword.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.eyougo.unlockword.R;
import com.eyougo.unlockword.data.WordDatabaseHelper;
import com.eyougo.unlockword.service.LockService;

public class MainActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		startService(new Intent(MainActivity.this, LockService.class));

       // mMainHandler.sendEmptyMessageDelayed(0, 200);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                WordDatabaseHelper.init(getApplication());
                Intent mainIntent = new Intent(MainActivity.this,SettingsActivity.class);
                MainActivity.this.startActivity(mainIntent);
                MainActivity.this.finish();
            }

        }, 1000);
    }
}
