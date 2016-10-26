package com.example.newsinfo.fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.newsinfo.R;
import com.example.newsinfo.UrlUtils;
import com.example.newsinfo.activity.WebViewActivity;
import com.example.newsinfo.adapter.NewsAdapter;
import com.example.newsinfo.bean.NewsBean;
import com.example.newsinfo.imageloader.ImageLoader;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public final class HomeFragment extends Fragment implements OnPageChangeListener {
	private static final String TAG = HomeFragment.class.getSimpleName();
	private static final String KEY_CONTENT = "HomeFragment:Content";
	PullToRefreshListView mPullRefreshListView;
	ArrayList<NewsBean> newsBeanList = new ArrayList<NewsBean>();
	NewsAdapter mNewsAdapter;
	// 热点
	String href;
	int pageNo = 1;
	String JSONDataUrl;

	/**
	 * ViewPager
	 */
	private ViewPager viewPager;

	/**
	 * 装点点的ImageView数组
	 */
	private ImageView[] tips;

	/**
	 * 装ImageView数组
	 */
	private ImageView[] mImageViews;

	private ArrayList<NewsBean> pagerList = new ArrayList<NewsBean>();
	ViewGroup group;
	ImageLoader mImageLoader;
	boolean isFirst = true;

	public static HomeFragment newInstance(String content, String href, String JSONDataUrl) {
		HomeFragment fragment = new HomeFragment();
		fragment.mContent = content;
		fragment.href = href;
		fragment.JSONDataUrl = JSONDataUrl;
		return fragment;
	}

	private String mContent = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT)) {
			mContent = savedInstanceState.getString(KEY_CONTENT);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_news_list, null);
		mPullRefreshListView = (PullToRefreshListView) view.findViewById(R.id.pull_refresh_list);
		group = (ViewGroup) view.findViewById(R.id.viewGroup);
		viewPager = (ViewPager) view.findViewById(R.id.viewPager);
		mImageLoader = new ImageLoader(getActivity());
		mImageLoader.setRequiredSize(5 * (int) getActivity().getResources().getDimension(R.dimen.litpic_width));

		mPullRefreshListView.setMode(Mode.PULL_FROM_START);
		mNewsAdapter = new NewsAdapter(getActivity(), newsBeanList, mContent);
		// Set a listener to be invoked when the list should be refreshed.
		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				// Update the LastUpdatedLabel
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				// Do work to refresh the list here.
				new GetDataTask().execute();
			}
		});
		mPullRefreshListView.setAdapter(mNewsAdapter);
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

	private class GetDataTask extends AsyncTask<Void, Void, NewsBean[]> {

		@Override
		protected NewsBean[] doInBackground(Void... params) {
			// Simulates a background job.
			ArrayList<NewsBean> list = new ArrayList<NewsBean>();
			try {
				// 解析网络标签
				list = parseList(href);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return list.toArray(new NewsBean[0]);
		}

		@Override
		protected void onPostExecute(NewsBean[] result) {
			Log.i(TAG, "getMode ===" + mPullRefreshListView.getCurrentMode());
			if (mPullRefreshListView.getCurrentMode() == Mode.PULL_FROM_START) {
				newsBeanList.clear();
				newsBeanList.addAll(Arrays.asList(result));
				pageNo = 1;
			}
			mNewsAdapter.notifyDataSetChanged();
			// Call onRefreshComplete when the list has been refreshed.
			mPullRefreshListView.onRefreshComplete();

			// 将点点加入到ViewGroup中
			group.removeAllViews();
			tips = new ImageView[pagerList.size()];
			for (int i = 0; i < tips.length; i++) {
				ImageView imageView = new ImageView(getActivity());
				imageView.setLayoutParams(new LayoutParams(10, 10));
				tips[i] = imageView;
				if (i == 0) {
					tips[i].setBackgroundResource(R.drawable.page_indicator_focused);
				} else {
					tips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
				}

				LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				layoutParams.leftMargin = 5;
				layoutParams.rightMargin = 5;
				group.addView(imageView, layoutParams);
			}

			// 将图片装载到数组中
			mImageViews = new ImageView[pagerList.size()];
			for (int i = 0; i < mImageViews.length; i++) {
				ImageView imageView = new ImageView(getActivity());
				mImageViews[i] = imageView;
				final NewsBean bean = pagerList.get(i);
				imageView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent();
						intent.setClass(getActivity(), WebViewActivity.class);
						intent.putExtra("NEWSBEAN", bean);
						startActivity(intent);
					}
				});
				// imageView.setBackgroundResource(imgIdArray[i]);
				mImageLoader.DisplayImage(bean.getImage(), imageView);
			}

			// 设置Adapter
			viewPager.setAdapter(new ViewPagerAdapter());
			// 设置监听，主要是设置点点的背景
			viewPager.setOnPageChangeListener(HomeFragment.this);
			// 设置ViewPager的默认项, 设置为长度的100倍，这样子开始就能往左滑动
			viewPager.setCurrentItem((mImageViews.length) * 100);
			viewPager.setVisibility(View.VISIBLE);
			group.setVisibility(View.VISIBLE);
			super.onPostExecute(result);
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(KEY_CONTENT, mContent);
	}

	public ArrayList<NewsBean> parseList(String href) {
		ArrayList<NewsBean> list = new ArrayList<NewsBean>();
		try {
			href = makeURL(href, new HashMap<String, Object>() {
				{
				}
			});
			Log.i("url", "url = " + href);

			/**
			 * <div class="article" data-docid="{{docid}}"> {{{oneimg}}} <div
			 * class="{{floatcls}}"> <h3>
			 * <a href="/home?page=article&id={{docid}}&up={{up}}"
			 * target="_blank">{{title}}</a></h3> {{{mulimg}}} <div
			 * class="article-info"> <div class="article-opts"> <div
			 * data-docid="{{docid}}" data-datatype="0" class="slide-del"></div>
			 * {{{likestr}}} <a
			 * href="/home?page=article&id={{docid}}&up={{up}}#comment"
			 * class="slide-comment" target="_blank">{{comment_count}}</a> <div
			 * data-sharetitle="{{title}}" class="slide-share"
			 * data-shareimage="{{shareimage}}" data-shareurl=
			 * "http://www.yidianzixun.com/article/{{docid}}?s=4")>转发</div>
			 * </div> <span class="article-source">{{source}}</span> <span
			 * class="article-date">{{date}}</span> <div class="clear"></div>
			 * </div> </div> <div class="clear"></div> </div>
			 */

			Document doc = Jsoup.connect(href).userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31").timeout(10000).get();
			Element tabcontent = doc.select("div.tab-content").first();
			// 图片导航
			Elements tabpannelElements = tabcontent.select("div.tab-pannel");
			/**
			 * <div class="tab-pannel"><a href="http://download.yidianzixun.com"
			 * target="_blank" class="slide-wrapper"><img src=
			 * "http://staticimg.yidianzixun.com/s/editor/201601/BGQX3NO1y.png"
			 * ><div class="slide-front anim"><h2>一点资讯客户端下载</h2>
			 * </div></a></div><div class="tab-pannel"><a
			 * href="http://www.yidianzixun.com/0EiCsTCa" target="_blank"
			 * class="slide-wrapper"><img
			 * src="http://si1.go2yd.com/get-image/07qxcVnr9Q8"><div
			 * class="slide-front anim"><h2>菲总统表态“不来硬的”：中国对南海有历史权利</h2>
			 * </div></a></div><div class="tab-pannel"><a
			 * href="http://www.yidianzixun.com/0EhfIAAl" target="_blank"
			 * class="slide-wrapper"><img
			 * src="http://si1.go2yd.com/get-image/07qxXjC4AzI"><div
			 * class="slide-front anim"><h2>丹麦农夫打造世界岛</h2></div></a></div><
			 */
			pagerList.clear();
			for (int i = 0; i < tabpannelElements.size(); i++) {
				NewsBean bean = new NewsBean();
				try {
					// 图片
					Element imageElement = tabpannelElements.get(i);
					Element astyle = imageElement.select("a").first();
					String nexthref = astyle.attr("href");
					Log.i(TAG, i + "nexthref = " + nexthref);
					bean.setUrl(nexthref);
				} catch (Exception e) {
					e.printStackTrace();
				}

				try {
					// 图片
					Element imageElement = tabpannelElements.get(i);
					Element astyle = imageElement.select("img").first();
					String imageurl = astyle.attr("src");
					if (imageurl != null && imageurl.length() >= 0) {
						Log.i(TAG, i + "=====" + "imageurl=" + imageurl);
						bean.setImage(imageurl);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				pagerList.add(bean);
			}

			Element sections = doc.select("div.sections").first();
			/**
			 * <div data-docid="0EiPGidw" class="article"><a style=
			 * "background-image:url(http://i1.go2yd.com/image.php?url=http://si1.go2yd.com/get-image/07r6NzSCPmy&amp;type=thumbnail_200x140);"
			 * href="/home?page=article&amp;id=0EiPGidw&amp;up=1875"
			 * target="_blank" class="article-img"></a><div
			 * class="article-nofloat "><h3><a
			 * href="/home?page=article&amp;id=0EiPGidw&amp;up=1875"
			 * target="_blank">靠一条铁链上下山！悬崖上的中国村落</a></h3>
			 * <p>
			 * 而百丈悬崖上的铁链是上下山唯一的路，稍有不慎便会命丧崖底。这里就是链子崖，位于湖北省宜昌市秭归县屈原镇的长江西陵峡南岸，
			 * </p>
			 * <div class="article-info"><div class="article-opts"><div
			 * data-docid="0EiPGidw" data-datatype="0"
			 * class="slide-del"></div><div data-docid="0EiPGidw"
			 * data-datatype="0" class="slide-like ">41</div><a
			 * href="/home?page=article&amp;id=0EiPGidw&amp;1875#comment"
			 * target="_blank" class="slide-comment">4</a><div
			 * data-sharetitle="靠一条铁链上下山！悬崖上的中国村落" data-shareimage=""
			 * data-shareurl="http://www.yidianzixun.com/article/0EiPGidw?s=4"
			 * class="slide-share">转发</div></div><span
			 * class="article-source">东方网</span><span
			 * class="article-date">2016-10-20 11:00:00</span><div
			 * class="clear"></div></div></div><div class="clear"></div></div>
			 */

			Elements sectionArticlesElements = sections.select("div.section-articles");
			for (int k = 0; k < sectionArticlesElements.size(); k++) {
				Elements beanElements = sectionArticlesElements.get(k).select("div.article");
				// 其他标签
				// 解析文件
				for (int i = 0; i < beanElements.size(); i++) {
					NewsBean bean = new NewsBean();
					try {
						// 图片
						Element imageElement = beanElements.get(i);
						Element astyle = imageElement.select("a").first();
						String nexthref = UrlUtils.YI_DIAN_ZI_XUN + astyle.attr("href");
						/**
						 * <a style=
						 * "background-image:url(http://i1.go2yd.com/image.php?url=http://si1.go2yd.com/get-image/07r6NzSCPmy&amp;type=thumbnail_200x140);"
						 * href="/home?page=article&amp;id=0EiPGidw&amp;up=1875"
						 * target="_blank" class="article-img"></a>
						 */
						Log.i(TAG, i + "nexthref = " + nexthref);
						bean.setUrl(nexthref);
					} catch (Exception e) {
						e.printStackTrace();
					}

					try {
						// 图片
						Element imageElement = beanElements.get(i);
						Elements aElements = imageElement.select("a");
						ArrayList<String> imgurlList = new ArrayList<String>();
						for (int y = 0; y < aElements.size(); y++) {
							Element astyle = aElements.get(y);
							// 5=====0imageurl =
							String imageurl = astyle.attr("style");
							if (imageurl != null && imageurl.length() >= 0 && imageurl.contains("background-image")) {
								Log.i(TAG, i + "=====" + y + "imageurl=" + imageurl);
								imgurlList.add(imageurl.replace("background-image:url(", "").replace(");", "").replace("http://i1.go2yd.com/image.php?url=", ""));
							}
						}
						bean.setImage_urls(imgurlList);
						/**
						 * <a href=
						 * "/home?page=article&amp;id=0EhdcsUn&amp;up=1514"
						 * target="_blank">强台风来袭 三沙岛礁“扶树哥”火了</a></h3> <div
						 * class="article-imgs"> <a style=
						 * "background-image:url('http://i1.go2yd.com/image.php?url=0EhdcstMtm&amp;type=thumbnail_200x140');"
						 * href="/home?page=article&amp;id=0EhdcsUn&amp;up=1514"
						 * target="_blank" class="article-img"></a> <a style=
						 * "background-image:url('http://i1.go2yd.com/image.php?url=0EhdcsdCQl&amp;type=thumbnail_200x140');"
						 * href="/home?page=article&amp;id=0EhdcsUn&amp;up=1514"
						 * target="_blank" class="article-img"></a> <a style=
						 * "background-image:url('http://i1.go2yd.com/image.php?url=http://si1.go2yd.com/get-image/07pLmw496GG&amp;type=thumbnail_200x140');"
						 * href="/home?page=article&amp;id=0EhdcsUn&amp;up=1514"
						 * target="_blank" class="article-img"></a> </div>
						 **/
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
						Element p = contentElement.select("div.article-nofloat p").first();
						String content = p.text();
						Log.i(TAG, i + "content = " + content);
						bean.setSummary(content);
					} catch (Exception e) {
						e.printStackTrace();
					}

					try {
						// 其他
						Element otherElement = beanElements.get(i);
						Element otherE = otherElement.select("div.article-info").first();
						String other = otherE.text();
						Log.i(TAG, i + "other = " + other);
						bean.setOther(other);
					} catch (Exception e) {
						e.printStackTrace();
					}
					list.add(bean);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	private String makeURL(String p_url, Map<String, Object> params) {
		StringBuilder url = new StringBuilder(p_url);
		if (url.indexOf("?") < 0)
			url.append('?');
		for (String name : params.keySet()) {
			url.append('&');
			url.append(name);
			url.append('=');
			url.append(String.valueOf(params.get(name)));
			// 不做URLEncoder处理
			// url.append(URLEncoder.encode(String.valueOf(params.get(name)),
			// UTF_8));
		}
		return url.toString().replace("?&", "?");
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
		isVisibleToUser = true;
		initUI(isVisibleToUser);
	}

	protected void initUI(final boolean isVisibleToUser) {
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.view.ViewPager.OnPageChangeListener#
	 * onPageScrollStateChanged(int)
	 */
	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.view.ViewPager.OnPageChangeListener#onPageScrolled
	 * (int, float, int)
	 */
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.view.ViewPager.OnPageChangeListener#onPageSelected
	 * (int)
	 */
	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		setImageBackground(arg0 % mImageViews.length);
	}

	/**
	 * 设置选中的tip的背景
	 * 
	 * @param selectItems
	 */
	private void setImageBackground(int selectItems) {
		for (int i = 0; i < tips.length; i++) {
			if (i == selectItems) {
				tips[i].setBackgroundResource(R.drawable.page_indicator_focused);
			} else {
				tips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
			}
		}
	}

	public class ViewPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return Integer.MAX_VALUE;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			// ((ViewPager) container).removeView(mImageViews[position %
			// mImageViews.length]);

		}

		/**
		 * 载入图片进去，用当前的position 除以 图片数组长度取余数是关键
		 */
		@Override
		public Object instantiateItem(View container, int position) {
			try {
				((ViewPager) container).addView(mImageViews[position % mImageViews.length], 0);
			} catch (Exception e) {
				// handler something
			}
			return mImageViews[position % mImageViews.length];
		}

	}
}
