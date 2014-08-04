package com.eyougo.unlockword.activity;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.format.DateFormat;
import android.text.style.ForegroundColorSpan;
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

import com.eyougo.unlockword.layer.LockLayer;
import com.eyougo.unlockword.R;
import com.eyougo.unlockword.data.WordDatabaseHelper;
import com.eyougo.unlockword.data.WordItem;
import com.eyougo.unlockword.manager.TimeDateManager;

public class LockActivity extends Activity {
    private static String TAG = "UnlockWord";
    private String correctAnswer;
    private LockLayer lockLayer;
    private View lockView;
    private String word;
    private int process;
    private WordDatabaseHelper wordDatabaseHelper;
    private TimeDateManager timeDateManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        lockView = View.inflate(this, R.layout.lock, null);
        initViews(lockView);
        lockLayer = LockLayer.getInstance(this);
        lockLayer.setLockView(lockView);
        lockLayer.lock();
        timeDateManager = new TimeDateManager(lockView, this);
    }

    private void initViews(View lockView) {
        // 隐藏提示
        TextView tips = (TextView)lockView.findViewById(R.id.tips);
        tips.setVisibility(View.GONE);

        wordDatabaseHelper = WordDatabaseHelper.getInstance(this);
        WordItem wordItem = wordDatabaseHelper.getRandomWordItem("word_kaoyan", null);

        List<String> otherTrans = new ArrayList<String>();
        while (otherTrans.size() < 3) {
            String trans = wordDatabaseHelper.getRandomOtherTrans("word_kaoyan", wordItem.getWord());
            if (!otherTrans.contains(trans)) {
                otherTrans.add(trans);
            }
        }

        // 设置单词
        TextView textView = (TextView) lockView.findViewById(R.id.word);
        TextView phoneticTextView = (TextView) lockView.findViewById(R.id.phonetic);
        word = wordItem.getWord();
        process = wordItem.getProcess();
        String phonetic = wordItem.getPhonetic();
        textView.setText(word);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/lsansuni.ttf");
        phoneticTextView.setTypeface(typeface);
        phoneticTextView.setText("    "+phonetic);
        // 设置正确答案
        correctAnswer = wordItem.getTrans();

        // 随机一个位置
        Random random = new Random();
        int location = random.nextInt(4);

        // 初始化答案
        int other = 0;
        List<String> answers = new ArrayList<String>(4);
        for (int i = 0; i < 4; i++) {
            if (i == location) {
                answers.add(correctAnswer);
            } else {
                answers.add(otherTrans.get(other));
                other++;
            }
        }

        // 设置按钮
        RadioButton radioButton0 = (RadioButton) lockView.findViewById(R.id.radio0);
        radioButton0.setText(answers.get(0));
        RadioButton radioButton1 = (RadioButton) lockView.findViewById(R.id.radio1);
        radioButton1.setText(answers.get(1));
        RadioButton radioButton2 = (RadioButton) lockView.findViewById(R.id.radio2);
        radioButton2.setText(answers.get(2));
        RadioButton radioButton3 = (RadioButton) lockView.findViewById(R.id.radio3);
        radioButton3.setText(answers.get(3));

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
            String answer = String.valueOf(radioButton.getText());
            Log.i(TAG, "checkedId="+checkedId+",answer="+answer );
            if (answer == null) {
                lockLayer.unlock();
                wordDatabaseHelper.close();
            }
            if (answer.equals(correctAnswer)) {
                Toast.makeText(getBaseContext(), "恭喜你，答对了！", Toast.LENGTH_SHORT)
                        .show();
                wordDatabaseHelper.processForward(word, process, false);
                wordDatabaseHelper.close();
                lockLayer.unlock();
                finish();
            }else{
                RadioGroup radioGroup = (RadioGroup) lockView.findViewById(R.id.radioGroup);
                int c = radioGroup.getChildCount();
                for (int i=0; i < c; i++){
                    RadioButton button = (RadioButton)radioGroup.getChildAt(i);
                    if(button.getText().equals(correctAnswer)){
                        SpannableStringBuilder style=new SpannableStringBuilder(correctAnswer);
                        style.setSpan(new ForegroundColorSpan(Color.RED),0,correctAnswer.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        button.setText(style);
                    }
                }
                // 显示提示
                TextView tips = (TextView)lockView.findViewById(R.id.tips);
                tips.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timeDateManager.finish();
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
