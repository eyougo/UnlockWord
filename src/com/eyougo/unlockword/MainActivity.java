package com.eyougo.unlockword;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private static String TAG = "UnlockWord";
	private String correctAnster;
	private LockLayer lockLayer;
	private View lockView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		lockView = View.inflate(this, R.layout.main, null);
		initViews(lockView);
		lockLayer = LockLayer.getInstance(this);
		lockLayer.setLockView(lockView);
		lockLayer.lock();
		//System.out.println(android.os.Build.VERSION.SDK);
		//lockView.setVisibility(View.VISIBLE);

		startService(new Intent(MainActivity.this, LockService.class));
	}

	private void initViews(View lockView) {
		// 设置单词
		TextView textView = (TextView) lockView.findViewById(R.id.word);
		textView.setText("maybe");

		// 设置正确答案
		correctAnster = "也许";

		// 获取干扰答案
		List<String> otherAnsters = new ArrayList<String>(3);
		otherAnsters.add("测试你妹的啊啊");
		otherAnsters.add("这只是开始");
		otherAnsters.add("我擦他大爷");

		// 随机一个位置
		Random random = new Random();
		int location = random.nextInt(4);

		// 初始化答案
		int other = 0;
		List<String> ansters = new ArrayList<String>(4);
		for (int i = 0; i < 4; i++) {
			if (i == location) {
				ansters.add(correctAnster);
			} else {
				ansters.add(otherAnsters.get(other));
				other++;
			}
		}

		// 设置按钮
		RadioButton radioButton0 = (RadioButton) lockView.findViewById(R.id.radio0);
		radioButton0.setText(ansters.get(0));
		RadioButton radioButton1 = (RadioButton) lockView.findViewById(R.id.radio1);
		radioButton1.setText(ansters.get(1));
		RadioButton radioButton2 = (RadioButton) lockView.findViewById(R.id.radio2);
		radioButton2.setText(ansters.get(2));
		RadioButton radioButton3 = (RadioButton) lockView.findViewById(R.id.radio3);
		radioButton3.setText(ansters.get(3));

		// 设置处理
		RadioGroup radioGroup = (RadioGroup) lockView.findViewById(R.id.radioGroup);
		radioGroup
				.setOnCheckedChangeListener(new RadioGroupOnCheckedChangeListener());

	}

	private class RadioGroupOnCheckedChangeListener implements
			OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			RadioButton radioButton = (RadioButton) lockView.findViewById(checkedId);
			String anster = String.valueOf(radioButton.getText());
			Log.i(TAG, "checkedId="+checkedId+",anster="+anster );
			if (anster != null && anster.equals(correctAnster)) {
				Toast.makeText(getBaseContext(), "回答正确！解锁成功", Toast.LENGTH_SHORT)
						.show();
				lockLayer.unlock();
				finish();
			}

		}

	}

	// 屏蔽掉Home键
	@Override
	public void onAttachedToWindow() {
		// this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
		super.onAttachedToWindow();
	}

	// 屏蔽掉Back键
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK)
			return true;
		else
			return super.onKeyDown(keyCode, event);

	}

}
