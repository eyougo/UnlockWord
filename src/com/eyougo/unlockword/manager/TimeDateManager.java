package com.eyougo.unlockword.manager;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TextView;

import com.eyougo.unlockword.R;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by mei on 8/2/14.
 */
public class TimeDateManager {

    private final static String M12 = "h:mm";
    private final static String M24 = "kk:mm";

    private static Activity mActivity;
    private String mTimeFormat;
    private TextView mTimeView;
    private TextView mDateView;
    private AmPm mAmPm;

    private Calendar mCalendar;
    private BroadcastReceiver mTimeChangeReceiver;

    public TimeDateManager(View view, Activity activity) {
        mActivity = activity;
        initViews(view);

        //注册监听
        mTimeChangeReceiver = new TimeChangedReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        filter.addAction(Intent.ACTION_TIME_CHANGED);
        filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        mActivity.registerReceiver(mTimeChangeReceiver, filter);

        updateTimeDate();
    }

    public void finish(){
        mActivity.unregisterReceiver(mTimeChangeReceiver);
    }

    public void initViews(View view) {
        mTimeView = (TextView) view.findViewById(R.id.time);
        mDateView = (TextView) view.findViewById(R.id.date);

        /*创建AmPm对象，参数为设置的字体风格(如可设为Typeface.DEFAULT_BOLD粗体)，
         * 此处参数为空，默认情况。
         */
        mAmPm = new AmPm(view);
        //获取mCalendar对象
        mCalendar = Calendar.getInstance();

        setDateFormat();


    }

    class AmPm {
        private TextView amPmTextView;
        private String amString, pmString;

        AmPm(View view) {
            amPmTextView = (TextView) view.findViewById(R.id.am_pm);

            //获取显示上午、下午的字符串数组
            String[] ampm = new DateFormatSymbols().getAmPmStrings();
            amString = ampm[0];
            pmString = ampm[1];
        }

        void setShowAmPm(boolean show) {
            if (amPmTextView != null) {
                amPmTextView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        }

        void setIsMorning(boolean isMorning) {
            if (amPmTextView != null) {
                amPmTextView.setText(isMorning ? amString : pmString);
            }
        }
    }


    private class TimeChangedReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // Post a runnable to avoid blocking the broadcast.
            final boolean timezoneChanged =
                    intent.getAction().equals(Intent.ACTION_TIMEZONE_CHANGED);
            new Handler().post(new Runnable() {
                public void run() {
                    if (timezoneChanged) {
                        mCalendar = Calendar.getInstance();
                    }
                    updateTimeDate();
                }
            });
        }
    }

    private void updateTimeDate() {
        mCalendar.setTimeInMillis(System.currentTimeMillis());

        CharSequence newTime = DateFormat.format(mTimeFormat, mCalendar);
        mTimeView.setText(newTime);
        mAmPm.setIsMorning(mCalendar.get(Calendar.AM_PM) == 0);

        mDateView.setText(DateFormat.getDateFormat(mActivity).format(mCalendar.getTime()));
    }

    private void setDateFormat() {
        mTimeFormat = DateFormat.is24HourFormat(mActivity)
                ? M24 : M12;
        mAmPm.setShowAmPm(!DateFormat.is24HourFormat(mActivity));
    }
}
