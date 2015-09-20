package com.lbh.dongfengnews.utils;

import android.content.Context;

public class CacheUtils {

	/**
	 * 设置缓存
	 * 
	 * @param context
	 *            上下文
	 * @param key
	 *            url
	 * @param value
	 *            json
	 */
	public static void setCache(Context context, String key, String value) {
		PrefUtils.setString(context, key, value);
		// 可以将缓存放在文件中, 文件名就是Md5(url), 文件内容是json
	}

	/**
	 * 获取缓存 key 是url
	 * 
	 * @param context
	 * @param key
	 * @param defuatValue
	 * @return
	 */
	public static String getCache(Context context, String key,
			String defuatValue) {

		return PrefUtils.getString(context, key, null);
	}
}
