package com.lbh.dongfengnews.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class TopNewsViewPager extends ViewPager {

	private int startY;
	private int startX;

	public TopNewsViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TopNewsViewPager(Context context) {
		super(context);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:

			// 不要拦截,
			// 这样是为了保证ACTION_MOVE调用
			getParent().requestDisallowInterceptTouchEvent(true);
 
			startX = (int) ev.getRawX();
			startY = (int) ev.getRawY();
			break;

		case MotionEvent.ACTION_MOVE:
			getParent().requestDisallowInterceptTouchEvent(false);

			int endX = (int) ev.getRawX();
			int endY = (int) ev.getRawY();

			if (Math.abs(endX - startX) > Math.abs(endY - startY)) {// 左右滑

				if (endX > startX) { // 右滑
					if (getCurrentItem() == 0) {// 第一页不拉出菜单
						getParent().requestDisallowInterceptTouchEvent(false);
					} else if (getCurrentItem() == getAdapter().getCount() - 1) {// 最后一页
						getParent().requestDisallowInterceptTouchEvent(false);
					} else {
						getParent().requestDisallowInterceptTouchEvent(true);
					}
				}
			}
			break;

		default:
			break;
		}
		return super.dispatchTouchEvent(ev);
	}
}
