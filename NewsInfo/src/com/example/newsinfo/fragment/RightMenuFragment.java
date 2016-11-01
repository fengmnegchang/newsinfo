package com.example.newsinfo.fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.example.newsinfo.BaseV4Fragment;
import com.example.newsinfo.CommonFragmentActivity;
import com.example.newsinfo.R;
import com.example.newsinfo.UrlUtils;
import com.example.newsinfo.activity.SettingsActivity;
import com.example.newsinfo.activity.WebViewActivity;
import com.example.newsinfo.adapter.RightMenuAdapter;
import com.example.newsinfo.bean.CommonT;
import com.example.newsinfo.bean.NewsBean;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * 
 ***************************************************************************************************************************************************************************** 
 * 右侧菜单列表
 * 
 * @author :fengguangjing
 * @createTime:2016-10-28上午10:37:29
 * @version:4.2.4
 * @modifyTime:
 * @modifyAuthor:
 * @description:
 ***************************************************************************************************************************************************************************** 
 */
public class RightMenuFragment extends BaseV4Fragment {
	PullToRefreshListView mPullRefreshListView;
	ArrayList<NewsBean> newsBeanList = new ArrayList<NewsBean>();
	RightMenuAdapter mRightMenuAdapter;
	// 热点
	String href;

	public static RightMenuFragment newInstance(String content, String href) {
		RightMenuFragment fragment = new RightMenuFragment();
		fragment.mContent = content;
		fragment.href = href;
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_news_list, null);
		mPullRefreshListView = (PullToRefreshListView) view.findViewById(R.id.pull_refresh_list);
		mPullRefreshListView.setMode(Mode.BOTH);
		mRightMenuAdapter = new RightMenuAdapter(getActivity(), newsBeanList, mContent);
		// Set a listener to be invoked when the list should be refreshed.
		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				// Update the LastUpdatedLabel
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				// Do work to refresh the list here.
				if (mPullRefreshListView.getCurrentMode() == Mode.PULL_FROM_START) {
					doAsync(RightMenuFragment.this, RightMenuFragment.this, RightMenuFragment.this);
				}
			}
		});
		mPullRefreshListView.setAdapter(mRightMenuAdapter);
		mPullRefreshListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), WebViewActivity.class);
				intent.putExtra("NEWSBEAN", newsBeanList.get((int) id));
				intent.putExtra("TITLE", mContent);
				startActivity(intent);
			}
		});
		// Do work to refresh the list here.
		return view;
	}

	@Override
	public CommonT call() throws Exception {
		// TODO Auto-generated method stub
		// Simulates a background job.
		CommonT mCommonT = new CommonT();
		ArrayList<NewsBean> list = new ArrayList<NewsBean>();
		try {
			// 解析网络标签
			list = parseList(href);
		} catch (Exception e) {
			e.printStackTrace();
		}
		mCommonT.setNewsBeanList(list);
		return mCommonT;
	}

	@Override
	public void onCallback(CommonT pCallbackValue) {
		// TODO Auto-generated method stub
		super.onCallback(pCallbackValue);
		Log.i(TAG, "getMode ===" + mPullRefreshListView.getCurrentMode());
		if (mPullRefreshListView.getCurrentMode() == Mode.PULL_FROM_START) {
			newsBeanList.clear();
			newsBeanList.addAll(pCallbackValue.getNewsBeanList());
		}
		mRightMenuAdapter.notifyDataSetChanged();
		// Call onRefreshComplete when the list has been refreshed.
		mPullRefreshListView.onRefreshComplete();
	}

	public ArrayList<NewsBean> parseList(String href) {
		ArrayList<NewsBean> list = new ArrayList<NewsBean>();
		try {
			href = ((CommonFragmentActivity) getActivity()).makeURL(href, new HashMap<String, Object>() {
				{
				}
			});
			Log.i("url", "url = " + href);

			Document doc = Jsoup.connect(href).userAgent(SettingsActivity.userAgent).cookies(SettingsActivity.getCookies()).timeout(10000).get();

			// 图片导航
			Element sideElement = doc.select("div.side-section").first();
			Elements saElements = sideElement.select("a");
			/**
			 * <div class="side-section"><a href="//mp.yidianzixun.com"
			 * class="media-ad"><div class="media-ad-front"></div></a> <a
			 * href="http://toptest.yidianzixun.com/LocalService/computer/"
			 * target="_blank" class="local-service"><img src=
			 * "http://staticimg.yidianzixun.com/s/editor/201601/Bn21stoMz.jpg"
			 * ></a> <a href="/campus" class="campus"><img src=
			 * "http://static.yidianzixun.com/modules/images/home/campus.png"
			 * ></a>
			 */
			for (int i = 0; i < saElements.size(); i++) {
				Element imageElement = saElements.get(i);
				if (imageElement != null && (imageElement.attr("class").equals("media-ad") || imageElement.attr("class").equals("local-service") || imageElement.attr("class").equals("campus"))) {
					NewsBean bean = new NewsBean();
					try {
						String nexthref = null;
						Element astyle = imageElement.select("a").first();
						if (imageElement.attr("class").equals("media-ad")) {
							  nexthref = "http:" + astyle.attr("href").replace("amp;", "");
						}

						if (imageElement.attr("class").equals("local-service")) {
							  nexthref =   astyle.attr("href").replace("amp;", "");
						}

						if (imageElement.attr("class").equals("campus")) {
							  nexthref = UrlUtils.YI_DIAN_ZI_XUN + astyle.attr("href").replace("amp;", "");
						}
						Log.i(TAG, i + "nexthref = " + nexthref);
						bean.setUrl(nexthref);

					} catch (Exception e) {
						e.printStackTrace();
					}

					try {
						// 图片
						Elements aElements = imageElement.select("img");
						String imageurl = aElements.attr("src");
						if (imageurl != null && imageurl.length() > 0) {
							Log.i(TAG, i + "=====" + "imageurl=" + imageurl);
							bean.setImage(imageurl);
						}

					} catch (Exception e) {
						e.printStackTrace();
					}

					bean.setType("image");
					list.add(bean);
				}
			}

			// 今日热搜
			Element sectionElement = doc.select("div.section-hotwords").first();
			Elements liElements = sectionElement.select("li");
			/**
			 * <li><a href=
			 * "/home?page=channel&amp;keyword=%E8%9B%87%E7%B2%BE%E7%94%B7%E7%BA%AF%E5%A4%A9%E7%84%B6%E6%97%A7%E7%85%A7"
			 * style="background-color: rgb(248, 236, 230);">蛇精男纯天然旧照</a></li>
			 */
			for (int i = 0; i < liElements.size(); i++) {
				NewsBean bean = new NewsBean();
				try {
					Element imageElement = liElements.get(i);
					Element astyle = imageElement.select("a").first();
					String nexthref = UrlUtils.YI_DIAN_ZI_XUN + astyle.attr("href").replace("amp;", "");
					Log.i(TAG, i + "nexthref = " + nexthref);
					bean.setUrl(nexthref);
					String title = imageElement.text();
					Log.i(TAG, i + "title = " + title);
					bean.setTitle(title);
					bean.setType("hot");
					list.add(bean);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			// 热门频道
			Element masthead = doc.select("div.hotchannels").first();
			Elements beanElements = masthead.select("div.hotchannel");

			/**
			 * <div class="hotchannel"><a style=
			 * "background-image:url('http://i1.go2yd.com/image.php?url=http://s.go2yd.com/b/idbbafdm_7i00d1d1.jpg&amp;type=thumbnail_90x70');"
			 * href="/home?page=channel&amp;id=t77" class="channel-img"></a><div
			 * class="channel-sub"><h3><a
			 * href="/home?page=channel&amp;id=t77">星座</a></h3><a href="#"
			 * data-cname="星座" class="subscribe">订阅</a>
			 * <p class="bookcount">
			 * 410.7万人订阅
			 * </p>
			 * </div></div>
			 */
			// 解析文件
			for (int i = 0; i < beanElements.size(); i++) {
				NewsBean bean = new NewsBean();
				try {
					Element imageElement = beanElements.get(i);
					Element astyle = imageElement.select("a").first();
					String nexthref = UrlUtils.YI_DIAN_ZI_XUN + astyle.attr("href");
					Log.i(TAG, i + "nexthref = " + nexthref);
					bean.setUrl(nexthref);
				} catch (Exception e) {
					e.printStackTrace();
				}

				try {
					// 图片
					Element imageElement = beanElements.get(i);
					Elements aElements = imageElement.select("a");
					// 0=====imageurl=background-image:url('http://i1.go2yd.com/image.php?url=http://s.go2yd.com/b/iezaxely_770fd1d1.jpg&type=thumbnail_90x70');
					// 3=====imageurl=background-image:url('http://i1.go2yd.com/image.php?url=http://s.go2yd.com/b/icn4vll1_cl01d1d1.jpg&type=thumbnail_90x70');
					String imageurl = aElements.attr("style");
					if (imageurl != null && imageurl.length() >= 0 && imageurl.contains("background-image")) {
						Log.i(TAG, i + "=====" + "imageurl=" + imageurl);
						imageurl = imageurl.replace("background-image:url('", "").replace("');", "");
					}
					bean.setImage(imageurl);

				} catch (Exception e) {
					e.printStackTrace();
				}

				try {
					// 标题
					Element titleElement = beanElements.get(i);
					Element h3 = titleElement.select("h3 a").first();
					String title = h3.text();
					Log.i(TAG, i + "title = " + title);
					bean.setTitle(title);
				} catch (Exception e) {
					e.printStackTrace();
				}

				try {
					// 内容
					Element contentElement = beanElements.get(i);
					Element p = contentElement.select("p.bookcount").first();
					String content = p.text();
					Log.i(TAG, i + "content = " + content);
					bean.setSummary(content);
				} catch (Exception e) {
					e.printStackTrace();
				}

				try {
					// 其他
					Element otherElement = beanElements.get(i);
					Element otherE = otherElement.select("a.subscribe").first();
					String other = otherE.text();
					Log.i(TAG, i + "other = " + other);
					bean.setOther(other);
				} catch (Exception e) {
					e.printStackTrace();
				}

				bean.setType("pindao");
				list.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#setUserVisibleHint(boolean)
	 */
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		// TODO Auto-generated method stub
		super.setUserVisibleHint(isVisibleToUser);
		initUI(isVisibleToUser);
	}

	@Override
	protected void initUI(final boolean isVisibleToUser) {
		super.initUI(isVisibleToUser);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				if (mPullRefreshListView == null || getActivity() == null || !isVisibleToUser) {
					initUI(isVisibleToUser);
				} else {
					mPullRefreshListView.setRefreshing(true);
				}
			}
		}, 500);
	}
}
