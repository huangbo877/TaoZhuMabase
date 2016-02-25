package com.ruiyu.taozhuma.widget;

import java.util.Timer;
import java.util.TimerTask;

import com.ruiyu.taozhuma.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint("HandlerLeak")
public class RushBuyCountDownTimerView extends LinearLayout {

	// 小时，十位
	private TextView tv_hour_decade;
	// 小时，个位
	private TextView tv_hour_unit;
	// 分钟，十位
	private TextView tv_min_decade;
	// 分钟，个位
	private TextView tv_min_unit;
	// 秒，十位
	private TextView tv_sec_decade;
	// 秒，个位
	private TextView tv_sec_unit;

	private long hour_decade;
	private long hour_unit;
	private long min_decade;
	private long min_unit;
	private long sec_decade;
	private long sec_unit;
	// 计时器
	private Timer timer;
	private TimeOverListener overListener;

	private LinearLayout ll_hour;
	private LinearLayout ll_min;
	private LinearLayout ll_sec;
	private TextView tv_text1;
	private TextView tv_text2;

	private Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			countDown();
		};
	};

	public RushBuyCountDownTimerView(Context context, AttributeSet attrs) {
		super(context, attrs);

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.view_countdowntimer, this);
		tv_hour_decade = (TextView) view.findViewById(R.id.tv_hour_decade);
		tv_hour_unit = (TextView) view.findViewById(R.id.tv_hour_unit);
		tv_min_decade = (TextView) view.findViewById(R.id.tv_min_decade);
		tv_min_unit = (TextView) view.findViewById(R.id.tv_min_unit);
		tv_sec_decade = (TextView) view.findViewById(R.id.tv_sec_decade);
		tv_sec_unit = (TextView) view.findViewById(R.id.tv_sec_unit);
		ll_hour = (LinearLayout) view.findViewById(R.id.ll_hour);
		ll_min = (LinearLayout) view.findViewById(R.id.ll_min);
		ll_sec = (LinearLayout) view.findViewById(R.id.ll_sec);
		tv_text1 = (TextView) view.findViewById(R.id.tv_text1);
		tv_text2 = (TextView) view.findViewById(R.id.tv_text2);

	}

	public void setOnTimeOverListener(TimeOverListener listener) {
		overListener = listener;
	}

	public interface TimeOverListener {
		public void isTimeOver(boolean over);
	}

	@SuppressLint("NewApi")
	public void turnBlackStyle() {
		ll_hour.setBackground(getResources().getDrawable(
				R.drawable.light_black_corner));
		ll_min.setBackground(getResources().getDrawable(
				R.drawable.light_black_corner));
		ll_sec.setBackground(getResources().getDrawable(
				R.drawable.light_black_corner));
		tv_text1.setTextColor(getResources().getColor(R.color.bg_light_black));
		tv_text2.setTextColor(getResources().getColor(R.color.bg_light_black));
	}

	/**
	 * 
	 * @Description: 开始计时
	 * @param
	 * @return void
	 * @throws
	 */
	public void start() {

		if (timer == null) {
			timer = new Timer();
			timer.schedule(new TimerTask() {

				@Override
				public void run() {
					handler.sendEmptyMessage(0);
				}
			}, 0, 1000);
		}
	}

	/**
	 * 
	 * @Description: 停止计时
	 * @param
	 * @return void
	 * @throws
	 */
	public void stop() {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}

	/**
	 * @throws Exception
	 * 
	 * @Description: 设置倒计时的时长
	 * @param
	 * @return void
	 * @throws
	 */
	public void setTime(long hours, long mins, long sec) {

		if (hours >= 60 || mins >= 60 || sec >= 60 || hours < 0 || mins < 0
				|| sec < 0) {
			throw new RuntimeException(
					"Time format is error,please check out your code");
		}

		hour_decade = hours / 10;
		hour_unit = hours - hour_decade * 10;

		min_decade = mins / 10;
		min_unit = mins - min_decade * 10;

		sec_decade = sec / 10;
		sec_unit = sec - sec_decade * 10;

		tv_hour_decade.setText(hour_decade + "");
		tv_hour_unit.setText(hour_unit + "");
		tv_min_decade.setText(min_decade + "");
		tv_min_unit.setText(min_unit + "");
		tv_sec_decade.setText(sec_decade + "");
		tv_sec_unit.setText(sec_unit + "");

	}

	/**
	 * 
	 * @Description: 倒计时
	 * @param
	 * @return boolean
	 * @throws
	 */
	private void countDown() {

		if (isCarry4Unit(tv_sec_unit)) {
			if (isCarry4Decade(tv_sec_decade)) {

				if (isCarry4Unit(tv_min_unit)) {
					if (isCarry4Decade(tv_min_decade)) {

						if (isCarry4Unit(tv_hour_unit)) {
							if (isCarry4Decade(tv_hour_decade)) {
								overListener.isTimeOver(true);
								stop();
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 
	 * @Description: 变化十位，并判断是否需要进位
	 * @param
	 * @return boolean
	 * @throws
	 */
	private boolean isCarry4Decade(TextView tv) {

		int time = Integer.valueOf(tv.getText().toString());
		time = time - 1;
		if (time < 0) {
			time = 5;
			tv.setText(time + "");
			return true;
		} else {
			tv.setText(time + "");
			return false;
		}

	}

	/**
	 * 
	 * @Description: 变化个位，并判断是否需要进位
	 * @param
	 * @return boolean
	 * @throws
	 */
	private boolean isCarry4Unit(TextView tv) {

		int time = Integer.valueOf(tv.getText().toString());
		time = time - 1;
		if (time < 0) {
			time = 9;
			tv.setText(time + "");
			return true;
		} else {
			tv.setText(time + "");
			return false;
		}

	}
}
