package com.lbh.dongfengnews.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;

import com.lbh.dongfengnews.R;
import com.lbh.dongfengnews.utils.PrefUtils;

public class SplashActivity extends Activity {

	private AnimationSet amSet;
	private LinearLayout ll_splash;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		ll_splash = (LinearLayout) findViewById(R.id.ll_splash);

		startAnimation();
	}

	/**
	 * 开启动画
	 */
	private void startAnimation() {
		// 动画集
		amSet = new AnimationSet(false);

		RotateAnimation rotate = new RotateAnimation(0, 360,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);

		rotate.setDuration(1000);
		rotate.setFillAfter(true);

		ScaleAnimation scale = new ScaleAnimation(0, 1, 0, 1,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		scale.setDuration(1000);
		scale.setFillAfter(true);

		AlphaAnimation alpha = new AlphaAnimation(0, 1);
		alpha.setDuration(2000);
		alpha.setFillAfter(true);

		// 添加所有动画到动画集合中
		amSet.addAnimation(rotate);
		amSet.addAnimation(scale);
		amSet.addAnimation(alpha);

		amSet.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {

				// 动画结束后实现跳转
				jumpToNextPage();
			}
		});

		// 开启动画
		ll_splash.startAnimation(amSet);

	}

	/**
	 * 跳转到下一个页面
	 */
	private void jumpToNextPage() {

		boolean isFirsted = PrefUtils.getBoolean(this, "is_first", false);
		if (isFirsted) {
			startActivity(new Intent(this, MainActivity.class));

		} else {
			startActivity(new Intent(this, WelcomeActivity.class));
		}
		finish();

	}
}
