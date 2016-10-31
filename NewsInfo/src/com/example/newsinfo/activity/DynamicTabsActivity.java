package com.example.newsinfo.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

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
import com.example.newsinfo.fragment.HomeFragment;
import com.example.newsinfo.fragment.NewsFragment;
import com.example.newsinfo.fragment.RightMenuFragment;
import com.example.newsinfo.indicator.TabPageIndicator;
/**
 * 
 *****************************************************************************************************************************************************************************
 * 首页tab页面 动态数据
 * @author :fengguangjing
 * @createTime:2016-10-28上午10:35:33
 * @version:4.2.4
 * @modifyTime:
 * @modifyAuthor:
 * @description:
 *****************************************************************************************************************************************************************************
 */
public class DynamicTabsActivity extends CommonFragmentActivity {
	public static final String TAG = DynamicTabsActivity.class.getSimpleName();
	ArrayList<NewsBean> channelList = new ArrayList<NewsBean>();
	FragmentPagerAdapter adapter;
	TabPageIndicator indicator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setCommonActivityLeftCanBack(false);
		setCommonActivityCenterEditSearch(true);
		setCommonActivityRightSearch(false);

		addContentView(R.layout.activity_simple_tabs, UrlUtils.STATUS_TAB_ACTIVITY_MARGIN_TOP);

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
//			if ("首页".equals(channelList.get(position).getTitle())) {
//				return HomeFragment.newInstance(channelList.get(position).getTitle(), channelList.get(position).getUrl(), channelList.get(position).getJsondataurl());
//			} else {
//				return NewsFragment.newInstance(channelList.get(position).getTitle(), channelList.get(position).getUrl(), channelList.get(position).getJsondataurl());
//			}
			
			return RightMenuFragment.newInstance(channelList.get(position).getTitle(), channelList.get(position).getUrl());
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
				list = parseList(UrlUtils.YI_DIAN_ZI_XUN);
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

			Document doc = Jsoup.connect(href).userAgent(SettingsActivity.userAgent).cookies(SettingsActivity.getCookies()).timeout(10000).get();
			Element masthead = doc.select("div.top-bar-nav-items").first();
			Elements beanElements = masthead.select("li");

			//<li class="active"><a href="/home" class="anim"><span>首页</span></a></li>
			//<li><a href="/home?page=channel&amp;id=hot" class="anim"><span>热点</span></a></li>
			//<li><a href="/home?page=channel&amp;keyword=%E8%A7%86%E9%A2%91" class="anim"><span>视频</span></a></li>
			// 解析文件
			for (int i = 0; i < beanElements.size(); i++) {
				NewsBean bean = new NewsBean();
				try {
					Element imageElement = beanElements.get(i).select("a").first();
					String title = imageElement.text();
					Log.i(TAG, i + "title = " + title);
					bean.setTitle(title);
					
					String ahref = imageElement.attr("href").replace("amp;", "");
					String url = UrlUtils.YI_DIAN_ZI_XUN+ahref;
					Log.i(TAG, i + "url = " + url);
					bean.setUrl(url);
					
					if(ahref.contains("keyword=")){
						String keyword = ahref.replace("/home?page=channel&keyword=", "");
						bean.setKeyword(keyword);
					}
					
					String jsondataurl="";
					if(title.equals("首页")){
						jsondataurl = "http://www.yidianzixun.com/api/q/?path=channel|news-list-for-best-channel&fields=docid&fields=category&fields=date&fields=image&fields=image_urls&fields=like&fields=up&fields=source&fields=title&fields=url&fields=comment_count&fields=summary&version=999999&infinite=true";
					}else if(title.equals("热点")){
						jsondataurl = "http://www.yidianzixun.com/api/q/?path=channel|news-list-for-keyword&display=%E7%83%AD%E7%82%B9&word_type=token&fields=docid&fields=category&fields=date&fields=image&fields=image_urls&fields=like&fields=source&fields=title&fields=url&fields=comment_count&fields=summary&fields=up&version=999999&infinite=true";
					}else if(title.equals("美女")){
						jsondataurl = "http://www.yidianzixun.com/api/q/?path=channel|news-list-for-channel&channel_id=4297898837&fields=docid&fields=category&fields=date&fields=image&fields=image_urls&fields=like&fields=source&fields=title&fields=url&fields=comment_count&fields=summary&fields=up&version=999999&infinite=true";
					}else{
						jsondataurl = "http://www.yidianzixun.com/api/q/?path=channel|news-list-for-keyword&word_type=token&fields=docid&fields=category&fields=date&fields=image&fields=image_urls&fields=like&fields=source&fields=title&fields=url&fields=comment_count&fields=summary&fields=up&version=999999&infinite=true"
								+"display="+bean.getKeyword();
					}
					bean.setJsondataurl(jsondataurl);
					
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
