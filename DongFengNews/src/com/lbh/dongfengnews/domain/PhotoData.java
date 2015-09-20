package com.lbh.dongfengnews.domain;

import java.util.List;

public class PhotoData {

	public int retcode;

	public PhotoDetailData data;

	public class PhotoDetailData {
		public String countcommenturl;
		public String more;

		public List<NewsChild> news;
		public String title;

		public List<TopicChild> topic;
	}

	public class NewsChild {
		public String id;
		public String listimage;
		public String pubdate;
		public String title;
		public String type;
		public String url;
	}

	public class TopicChild {

	}
}
