package com.lbh.dongfengnews.activitys;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.lbh.dongfengnews.R;
import com.lbh.dongfengnews.fragments.ContentFragment;
import com.lbh.dongfengnews.fragments.LeftMenuFragment;

public class MainActivity extends SlidingFragmentActivity {

	private static final String CONTENT_FRAGMENT = "content_fragment";
	private static final String LEFT_MENU_FRAGMENT = "left_menu_fragment";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		initSlidingMenu();

		initFragment();
	}

	/**
	 * 初始化slidingmenu
	 */
	private void initSlidingMenu() {

		setBehindContentView(R.layout.left_menu);// 设置侧边栏布局

		SlidingMenu slidingMenu = getSlidingMenu();// 获取侧边栏对象
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);// 设置全屏触摸

		// slidingMenu.setSecondaryMenu(R.layout.left_menu);// 设置右侧边栏
		// slidingMenu.setMode(SlidingMenu.LEFT_RIGHT);// 设置展现模式

		slidingMenu.setMode(SlidingMenu.LEFT);
		slidingMenu.setBehindOffset(200);// 设置预留屏幕的宽度
	}

	/**
	 * 初始话两个fragmnet，left_menu_fragment和content_fragment
	 */
	private void initFragment() {

		// 兼容3.0以下版本
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.fl_content, new ContentFragment(), CONTENT_FRAGMENT);
		ft.replace(R.id.fl_left_menu, new LeftMenuFragment(),
				LEFT_MENU_FRAGMENT);

		ft.commit();
	}

	/**
	 * 暴露接口，便于分支获得ContentFragment对象
	 */
	public ContentFragment getContentFragment() {
		FragmentManager fm = getSupportFragmentManager();
		ContentFragment fragment = (ContentFragment) fm
				.findFragmentByTag(CONTENT_FRAGMENT);

		return fragment;
	}

	/**
	 * 暴露接口，便于分支获得getLeftMenuFragment对象
	 */
	public LeftMenuFragment getLeftMenuFragment() {
		FragmentManager fm = getSupportFragmentManager();
		LeftMenuFragment fragment = (LeftMenuFragment) fm
				.findFragmentByTag(LEFT_MENU_FRAGMENT);
		return fragment;

	}

}
