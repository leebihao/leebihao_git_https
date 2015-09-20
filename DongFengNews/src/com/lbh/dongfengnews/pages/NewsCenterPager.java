package com.lbh.dongfengnews.pages;

import java.util.ArrayList;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lbh.dongfengnews.activitys.MainActivity;
import com.lbh.dongfengnews.domain.NewsData;
import com.lbh.dongfengnews.domain.NewsData.NewsMenuData;
import com.lbh.dongfengnews.fragments.LeftMenuFragment;
import com.lbh.dongfengnews.global.InternetPlaces;
import com.lbh.dongfengnews.menudetailpages.BaseMenuDetailPager;
import com.lbh.dongfengnews.menudetailpages.InteractMenuDetailPager;
import com.lbh.dongfengnews.menudetailpages.NewsMenuDetailPager;
import com.lbh.dongfengnews.menudetailpages.PhotoMenuDetailPager;
import com.lbh.dongfengnews.menudetailpages.TopicMenuDetailPager;
import com.lbh.dongfengnews.utils.CacheUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * 新闻中心
 * 
 * @author Kevin
 * 
 */
public class NewsCenterPager extends BasePager {

	private ArrayList<BaseMenuDetailPager> mPagers;// 4个菜单详情页的集合
	private NewsData mNewsData;

	public NewsCenterPager(Activity activity) {
		super(activity);
	}

	@Override
	public void initData() {
		System.out.println("初始化新闻中心数据....");

		tvTitle.setText("新闻");
		setSlidingMenuEnable(true);// 打开侧边栏

		String cache = CacheUtils.getCache(mActivity,
				InternetPlaces.CATEGORIES_URL, "");

		if (!TextUtils.isEmpty(cache)) {

			parseData(cache);// 如果有缓存就直接解析，无须访问
		}

		getDataFromServer();
	}

	/**
	 * 从服务器获取数据
	 */
	private void getDataFromServer() {
		HttpUtils utils = new HttpUtils();

		// 使用xutils发送请求
		utils.send(HttpMethod.GET, InternetPlaces.CATEGORIES_URL,
				new RequestCallBack<String>() {

					// 访问成功
					@Override
					public void onSuccess(ResponseInfo responseInfo) {
						String result = (String) responseInfo.result;
						System.out.println("返回结果:" + result);

						CacheUtils.setCache(mActivity,
								InternetPlaces.CATEGORIES_URL, result);
						parseData(result);
					}

					// 访问失败
					@Override
					public void onFailure(HttpException error, String msg) {
						Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT)
								.show();
						error.printStackTrace();
					}

				});
	}

	/**
	 * 解析网络数据
	 * 
	 * @param result
	 */
	protected void parseData(String result) {
		Gson gson = new Gson();
		mNewsData = gson.fromJson(result, NewsData.class);
		System.out.println("解析结果:" + mNewsData);

		// 刷新测边栏的数据
		MainActivity mainUi = (MainActivity) mActivity;
		LeftMenuFragment leftMenuFragment = mainUi.getLeftMenuFragment();
		leftMenuFragment.setMenuData(mNewsData);

		// 准备4个菜单详情页
		mPagers = new ArrayList<BaseMenuDetailPager>();
		mPagers.add(new NewsMenuDetailPager(mActivity,
				mNewsData.data.get(0).children));
		mPagers.add(new TopicMenuDetailPager(mActivity));
		mPagers.add(new PhotoMenuDetailPager(mActivity, btnSwitchPhoto));
		mPagers.add(new InteractMenuDetailPager(mActivity));

		setCurrentMenuDetailPager(0);// 设置菜单详情页-新闻为默认当前页
	}

	/**
	 * 设置当前菜单详情页
	 */
	public void setCurrentMenuDetailPager(int position) {
		BaseMenuDetailPager pager = mPagers.get(position);// 获取当前要显示的菜单详情页
		flContent.removeAllViews();// 清除之前的布局
		flContent.addView(pager.mRootView);// 将菜单详情页的布局设置给帧布局

		// 设置当前页的标题
		NewsMenuData menuData = mNewsData.data.get(position);
		tvTitle.setText(menuData.title);

		pager.initData();// 初始化当前页面的数据

		if (pager instanceof PhotoMenuDetailPager) {

			btnSwitchPhoto.setVisibility(View.VISIBLE);
		} else {
			btnSwitchPhoto.setVisibility(View.GONE);
		}
	}

}
