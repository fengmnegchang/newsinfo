package com.example.newsinfo.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.example.newsinfo.CommonFragmentActivity;
import com.example.newsinfo.R;
import com.example.newsinfo.UrlUtils;
import com.example.newsinfo.bean.NewsBean;
import com.example.newsinfo.fragment.CollectionFragment;
import com.example.newsinfo.indicator.TabPageIndicator;

public class OwnerTabsActivity extends CommonFragmentActivity {
	public static final String TAG = OwnerTabsActivity.class.getSimpleName();
	ArrayList<NewsBean> channelList = new ArrayList<NewsBean>();
	FragmentPagerAdapter adapter;
	TabPageIndicator indicator ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setCommonActivityLeftCanBack(true);
		setCommonActivityCenterEditSearch(false);
		setCommonActivityRightSearch(false);

		addContentView(R.layout.activity_owner_tabs,UrlUtils.NONE_STATUS_TAB_ACTIVITY_MARGIN_TOP);

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
		setRightNone();
		text_title.setText("个人主页");
		adapter = new GoogleMusicAdapter(getSupportFragmentManager());
		ViewPager pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(adapter);

		indicator = (TabPageIndicator) findViewById(R.id.indicator);
		indicator.setViewPager(pager);
		

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
	}

	class GoogleMusicAdapter extends FragmentPagerAdapter {
		public GoogleMusicAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return CollectionFragment.newInstance(channelList.get(position));
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
			Document doc = Jsoup.connect(href).userAgent(UrlUtils.userAgent).cookies(UrlUtils.getCookies())
					.timeout(10000).get();
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
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

}
