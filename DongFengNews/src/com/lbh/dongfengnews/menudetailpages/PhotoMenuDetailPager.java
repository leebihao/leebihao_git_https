package com.lbh.dongfengnews.menudetailpages;

import java.util.List;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lbh.dongfengnews.R;
import com.lbh.dongfengnews.domain.PhotoData;
import com.lbh.dongfengnews.domain.PhotoData.NewsChild;
import com.lbh.dongfengnews.global.InternetPlaces;
import com.lbh.dongfengnews.utils.CacheUtils;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * 菜单详情页-组图
 * 
 * @author Kevin
 * 
 */
public class PhotoMenuDetailPager extends BaseMenuDetailPager {

	private ListView lvPhoto;
	private GridView gvPhoto;

	private ImageButton btnPhoto;
	private List<NewsChild> photoNewsList;
	private PhotoAdapter adapter;

	public PhotoMenuDetailPager(Activity activity, ImageButton btnPhoto) {
		super(activity);
		this.btnPhoto = btnPhoto;

		btnPhoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				switchDisplay();
			}
		});
	}

	@Override
	public View initViews() {
		View view = View.inflate(mActivity, R.layout.menu_photo_pager, null);
		lvPhoto = (ListView) view.findViewById(R.id.lv_photo);
		gvPhoto = (GridView) view.findViewById(R.id.gv_photo);
		return view;
	}

	@Override
	public void initData() {

		String cache = CacheUtils.getCache(mActivity,
				InternetPlaces.PHOTOS_URL, "");

		if (!TextUtils.isEmpty(cache)) {
			parseData(cache);// 不为空直接解析
		}
		getDataFromServer();

	}

	/**
	 * 联网获取数据
	 */
	private void getDataFromServer() {

		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, InternetPlaces.PHOTOS_URL,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						System.out.println(arg1);
						Toast.makeText(mActivity, "获取数据失败！", 0).show();
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {

						String result = (String) arg0.result;
						System.out.println(result);

						parseData(result);
						CacheUtils.setCache(mActivity,
								InternetPlaces.PHOTOS_URL, result);
						System.out
								.println("=====================================");

					}
				});
	}

	/**
	 * 解析数据
	 * 
	 * @param result
	 */
	protected void parseData(String result) {

		Gson gson = new Gson();
		PhotoData photoData = gson.fromJson(result, PhotoData.class);
		photoNewsList = photoData.data.news;

		if (photoNewsList != null) {

			adapter = new PhotoAdapter();
			lvPhoto.setAdapter(adapter);
			gvPhoto.setAdapter(adapter);
		}
	}

	private class PhotoAdapter extends BaseAdapter {

		private BitmapUtils bitmapUtils;

		public PhotoAdapter() {
			bitmapUtils = new BitmapUtils(mActivity);
			bitmapUtils.configDefaultLoadingImage(R.drawable.news_pic_default);

		}

		@Override
		public int getCount() {
			return photoNewsList.size();
		}

		@Override
		public NewsChild getItem(int position) {
			return photoNewsList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(mActivity, R.layout.list_photo_item,
						null);
				holder = new ViewHolder();
				holder.tvTitle = (TextView) convertView
						.findViewById(R.id.tv_title);
				holder.ivPic = (ImageView) convertView
						.findViewById(R.id.iv_pic);

				convertView.setTag(holder);
			} else {

				holder = (ViewHolder) convertView.getTag();
			}

			NewsChild newsChild = getItem(position);
			String imageUrl = newsChild.listimage;
			holder.tvTitle.setText(newsChild.title);
			bitmapUtils.display(holder.ivPic, imageUrl);

			return convertView;
		}
	}

	static class ViewHolder {
		ImageView ivPic;
		TextView tvTitle;
	}

	private boolean isListDisplay = true;// 是否是列表展示

	/**
	 * 切换展示图组的形式
	 */
	protected void switchDisplay() {

		if (isListDisplay) {
			isListDisplay = false;
			lvPhoto.setVisibility(View.GONE);
			gvPhoto.setVisibility(View.VISIBLE);

			btnPhoto.setImageResource(R.drawable.icon_pic_list_type);

		} else {
			isListDisplay = true;
			lvPhoto.setVisibility(View.VISIBLE);
			gvPhoto.setVisibility(View.GONE);
			btnPhoto.setImageResource(R.drawable.icon_pic_grid_type);
		}
	}
}
