package com.lbh.dongfengnews.activitys;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

import com.lbh.dongfengnews.R;
import com.lbh.dongfengnews.utils.PrefUtils;

public class WelcomeActivity extends Activity {

	private ViewPager vp_splash;

	private LinearLayout ll_point;

	private ArrayList<ImageView> guideImgViewList;
	/**
	 * 点集合id
	 */
	private int[] guideImgId = new int[] { R.drawable.guide_1,
			R.drawable.guide_2, R.drawable.guide_3 };

	/**
	 * 两个灰点之间的距离
	 */
	private int mPointWidth;

	/**
	 * 监听viewpager
	 */
	private GuideViewPagerAdapter pagerAdapter;

	/**
	 * 会移动的红点
	 */
	private View viewRedPoint;

	private Button startBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initViews();

		pagerAdapter = new GuideViewPagerAdapter();
		vp_splash.setAdapter(pagerAdapter);

		/**
		 * 监听page滑动
		 */

		vp_splash.setOnPageChangeListener(new GuidePageListener());

		startBtn = (Button) findViewById(R.id.btn_start);
		startBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				PrefUtils.setBoolean(WelcomeActivity.this,
						"is_first", true);
				startActivity(new Intent(WelcomeActivity.this,
						MainActivity.class));

				finish();
			}
		});
	}

	/**
	 * 初始化界面
	 */
	private void initViews() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_welcome);
		vp_splash = (ViewPager) findViewById(R.id.vp_guide);

		// 本身就是一个view
		viewRedPoint = findViewById(R.id.v_red_point);

		guideImgViewList = new ArrayList<ImageView>();
		for (int i = 0; i < guideImgId.length; i++) {

			ImageView iv_guide = new ImageView(this);
			iv_guide.setImageResource(guideImgId[i]);
			guideImgViewList.add(iv_guide);
		}

		ll_point = (LinearLayout) findViewById(R.id.ll_point_group);

		for (int i = 0; i < guideImgId.length; i++) {
			View point = new View(this);
			// 默认的背景 灰点
			point.setBackgroundResource(R.drawable.shape_point_gray);
			LayoutParams params = new LinearLayout.LayoutParams(10, 10);

			// 判断是否第一个灰点，如果不是，就设置两个点之间的距离为10
			if (i > 0) {
				params.leftMargin = 10;
			}

			// 给点设置大小为10
			point.setLayoutParams(params);
			// 依次把三个点加入线性布局中
			ll_point.addView(point);
		}

		/**
		 * 取得灰点之间的距离 视图树，保证linearlayout执行完
		 */

		// 获取视图树, 对layout结束事件进行监听
		ll_point.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					// 当layout执行结束后回调此方法
					@Override
					public void onGlobalLayout() {
						System.out.println("layout 结束");
						ll_point.getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);
						mPointWidth = ll_point.getChildAt(1).getLeft()
								- ll_point.getChildAt(0).getLeft();
						System.out.println("圆点距离:" + mPointWidth);
					}
				});
	}

	/**
	 * viewpager适配器
	 * 
	 * @author Administrator
	 * 
	 */
	private class GuideViewPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return guideImgId.length;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {

			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {

			container.addView(guideImgViewList.get(position));
			return guideImgViewList.get(position);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {

			container.removeView((View) object);
		}

	}

	/**
	 * pager滑动监听
	 * 
	 * @author Administrator
	 * 
	 */
	private class GuidePageListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
			// System.out.println("当前位置:" + position + ";百分比:" + positionOffset
			// + ";移动距离:" + positionOffsetPixels);
			int len = (int) (mPointWidth * positionOffset) + position
					* mPointWidth;
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewRedPoint
					.getLayoutParams();// 获取当前红点的布局参数
			params.leftMargin = len;// 设置左边距

			viewRedPoint.setLayoutParams(params);// 重新给小红点设置布局参数
		}

		@Override
		public void onPageSelected(int position) {

			// 在最后一个page显示跳转btn,其他页面不可见
			if (position == (guideImgId.length - 1)) {
				startBtn.setVisibility(View.VISIBLE);
			} else {
				startBtn.setVisibility(View.INVISIBLE);
			}
		}

	}
}
