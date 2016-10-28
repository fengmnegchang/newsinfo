package com.example.newsinfo.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.newsinfo.CommonFragmentActivity;
import com.example.newsinfo.R;
import com.example.newsinfo.UrlUtils;
import com.example.newsinfo.bean.NewsBean;
import com.example.newsinfo.fragment.CollectionFragment;
import com.example.newsinfo.fragment.OwnerPinDaoFragment;
import com.example.newsinfo.imageloader.ImageLoader;
import com.example.newsinfo.indicator.TabPageIndicator;
import com.example.newsinfo.widget.CircleImageView;
/**
 * 
 *****************************************************************************************************************************************************************************
 * wo页面 
 * @author :fengguangjing
 * @createTime:2016-10-28上午10:35:45
 * @version:4.2.4
 * @modifyTime:
 * @modifyAuthor:
 * @description:
 *****************************************************************************************************************************************************************************
 */
public class OwnerTabsActivity extends CommonFragmentActivity {
	public static final String TAG = OwnerTabsActivity.class.getSimpleName();
	ArrayList<NewsBean> channelList = new ArrayList<NewsBean>();
	FragmentPagerAdapter adapter;
	TabPageIndicator indicator;
	ImageView back_img;// 左边返回
	ImageView image_settings;
	TextView text_settings;// 设置

	CircleImageView image_logo;
	TextView text_name;
	NewsBean ownerBean;
	NewsBean settingsBean;
	ImageLoader mImageLoader;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setExtendsCommonActivity(false);
		setContentView(R.layout.activity_owner_tabs);
		init();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.example.newsinfo.CommonActivity#findView()
	 */
	@Override
	protected void findView() {
		// TODO Auto-generated method stub
		super.findView();
		back_img = (ImageView) findViewById(R.id.back_img);
		image_settings = (ImageView) findViewById(R.id.image_settings);
		text_settings = (TextView) findViewById(R.id.text_settings);

		image_logo = (CircleImageView) findViewById(R.id.image_logo);
		text_name = (TextView) findViewById(R.id.text_name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.example.newsinfo.CommonActivity#initValue()
	 */
	@Override
	protected void initValue() {
		// TODO Auto-generated method stub
		super.initValue();
		adapter = new GoogleMusicAdapter(getSupportFragmentManager());
		ViewPager pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(adapter);

		indicator = (TabPageIndicator) findViewById(R.id.indicator);
		indicator.setViewPager(pager);

		mImageLoader = new ImageLoader(this);
		mImageLoader.setRequiredSize(5 * (int) getResources().getDimension(R.dimen.litpic_width));
		
		new GetDataTask().execute();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.example.newsinfo.CommonActivity#bindEvent()
	 */
	@Override
	protected void bindEvent() {
		// TODO Auto-generated method stub
		super.bindEvent();
		back_img.setOnClickListener(this);
		image_settings.setOnClickListener(this);
		text_settings.setOnClickListener(this);
		image_logo.setOnClickListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.example.newsinfo.CommonFragmentActivity#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		Intent intent;
		switch (v.getId()) {
		case R.id.image_settings:
			intent = new Intent();
			intent.setClass(this, SettingsActivity.class);
			intent.putExtra("CHANNEL", "OwnerTabsActivity");
			startActivity(intent);
			break;
		case R.id.text_settings:
		case R.id.image_logo:
			if(settingsBean!=null){
				intent = new Intent();
				intent.setClass(this, WebViewActivity.class);
				intent.putExtra("NEWSBEAN", settingsBean);
				intent.putExtra("TITLE", "账号设置");
				startActivity(intent);
			}
			break;
		default:
			break;
		}
	}

	class GoogleMusicAdapter extends FragmentPagerAdapter {
		public GoogleMusicAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			if (position == 0) {
				return CollectionFragment.newInstance(channelList.get(position));
			} else {
				return OwnerPinDaoFragment.newInstance(channelList.get(position));
			}

		}

		@Override
		public CharSequence getPageTitle(int position) {
			return channelList.get(position).getTitle();
		}

		@Override
		public int getCount() {
			return channelList.size();
		}
	}

	private class GetDataTask extends AsyncTask<Void, Void, NewsBean[]> {
		@Override
		protected NewsBean[] doInBackground(Void... params) {
			// Simulates a background job.
			ArrayList<NewsBean> list = new ArrayList<NewsBean>();
			try {
				// 解析网络标签
				list = parseList(UrlUtils.PROFILE);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return list.toArray(new NewsBean[0]);
		}

		@Override
		protected void onPostExecute(NewsBean[] result) {
			channelList.addAll(Arrays.asList(result));
			adapter.notifyDataSetChanged();
			indicator.notifyDataSetChanged();
			//更新ui
			if(ownerBean!=null){
//				mImageLoader.DisplayImage(ownerBean.getImage(), image_logo);
				text_name.setText(ownerBean.getTitle());
			}
			super.onPostExecute(result);
		}
	}

	public ArrayList<NewsBean> parseList(String href) {
		ArrayList<NewsBean> list = new ArrayList<NewsBean>();
		try {
			href = makeURL(href, new HashMap<String, Object>() {
				{
				}
			});
			Log.i("url", "url = " + href);
			Document doc = Jsoup.connect(href).userAgent(SettingsActivity.userAgent).cookies(SettingsActivity.getCookies()).timeout(10000).get();
			Element masthead = doc.select("ul.profile-nav").first();
			Elements beanElements = masthead.select("li");
			// 解析文件
			for (int i = 0; i < beanElements.size(); i++) {
				NewsBean bean = new NewsBean();
				try {
					Element imageElement = beanElements.get(i).select("li").first();
					String title = imageElement.text();
					Log.i(TAG, i + "title = " + title);
					bean.setTitle(title);
				} catch (Exception e) {
					e.printStackTrace();
				}

				list.add(bean);
			}

			// 解析个人信息
			/**
			 * <div class="profile-hd"><a href="/home?page=profile-modify"
			 * class="settings">编辑我的资料</a><a
			 * href="/home?page=profile-modify"><img src=
			 * "http://i1.go2yd.com/avatar/h3rd/group1/M02/D6/B8/CmUCBlgESSGAHHeqAAAm-_zpfeE1322387.jpg"
			 * ></a><h2>御守</h2></div>
			 */
			
			settingsBean = new NewsBean();
			Element ownerElement = doc.select("div.profile-hd").first();
			Element aElement = ownerElement.select("a").first();
			settingsBean.setUrl(UrlUtils.YI_DIAN_ZI_XUN+aElement.attr("href"));
			settingsBean.setTitle(aElement.text());
			
			ownerBean = new NewsBean();
			Element anextElement = aElement.nextElementSibling();
			Element imgElement = anextElement.select("img").first();
			ownerBean.setImage(imgElement.attr("src"));
			
			ownerBean.setTitle(ownerElement.select("h2").first().text());
			ownerBean.setUrl(aElement.attr("href"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

}
