package com.example.newsinfo.fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.newsinfo.BaseV4Fragment;
import com.example.newsinfo.CommonFragmentActivity;
import com.example.newsinfo.R;
import com.example.newsinfo.UrlUtils;
import com.example.newsinfo.activity.SettingsActivity;
import com.example.newsinfo.activity.WebViewActivity;
import com.example.newsinfo.adapter.NewsAdapter;
import com.example.newsinfo.bean.CommonT;
import com.example.newsinfo.bean.NewsBean;
import com.example.newsinfo.imageloader.ImageLoader;
import com.example.newsinfo.json.NewsBeanJson;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * 
 ***************************************************************************************************************************************************************************** 
 * 首页
 * 
 * @author :fengguangjing
 * @createTime:2016-10-28上午10:37:13
 * @version:4.2.4
 * @modifyTime:
 * @modifyAuthor:
 * @description:
 ***************************************************************************************************************************************************************************** 
 */
public final class HomeFragment extends BaseV4Fragment implements
		OnPageChangeListener {
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

	public static HomeFragment newInstance(String content, String href,
			String JSONDataUrl) {
		HomeFragment fragment = new HomeFragment();
		fragment.mContent = content;
		fragment.href = href;
		fragment.JSONDataUrl = JSONDataUrl;
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_news_list, null);
		mPullRefreshListView = (PullToRefreshListView) view
				.findViewById(R.id.pull_refresh_list);
		group = (ViewGroup) view.findViewById(R.id.viewGroup);
		viewPager = (ViewPager) view.findViewById(R.id.viewPager);
		mImageLoader = new ImageLoader(getActivity());
		mImageLoader.setRequiredSize(5 * (int) getActivity().getResources()
				.getDimension(R.dimen.litpic_width));

		mPullRefreshListView.setMode(Mode.BOTH);
		mNewsAdapter = new NewsAdapter(getActivity(), newsBeanList, mContent);
		// Set a listener to be invoked when the list should be refreshed.
		mPullRefreshListView
				.setOnRefreshListener(new OnRefreshListener<ListView>() {
					@Override
					public void onRefresh(
							PullToRefreshBase<ListView> refreshView) {
						String label = DateUtils.formatDateTime(getActivity(),
								System.currentTimeMillis(),
								DateUtils.FORMAT_SHOW_TIME
										| DateUtils.FORMAT_SHOW_DATE
										| DateUtils.FORMAT_ABBREV_ALL);
						// Update the LastUpdatedLabel
						refreshView.getLoadingLayoutProxy()
								.setLastUpdatedLabel(label);
						// Do work to refresh the list here.
						// new GetDataTask().execute();
						if (mPullRefreshListView.getCurrentMode() == Mode.PULL_FROM_START) {
//							new GetDataTask().execute();
							doAsync(HomeFragment.this, HomeFragment.this, HomeFragment.this);
						} else if (mPullRefreshListView.getCurrentMode() == Mode.PULL_FROM_END) {
							volleyJson();
						}
					}
				});
		mPullRefreshListView.setAdapter(mNewsAdapter);
		mPullRefreshListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
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

	/**
	 * 请求网络数据
	 * **http://www.yidianzixun.com/api/q/?path=channel|news-list-for-keyword
	 * &display
	 * =%E7%83%AD%E7%82%B9&word_type=token&fields=docid&fields=category&fields
	 * =date
	 * &fields=image&fields=image_urls&fields=like&fields=source&fields=title
	 * &fields=url&fields=comment_count&fields=summary&fields=up
	 * &cstart=10&cend=20&version=999999&infinite=true cstart=20&cend=30
	 */
	public void volleyJson() {
		RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
		if (pageNo > 0) {
			JSONDataUrl = JSONDataUrl + "&cstart=" + pageNo * 10 + "&cend="
					+ (pageNo + 1) * 10;
		}
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
				Request.Method.GET, JSONDataUrl, SettingsActivity.getHeaders(),
				null, this, this);
		requestQueue.add(jsonObjectRequest);
	}

	@Override
	public void onResponse(JSONObject response) {
		// TODO Auto-generated method stub
		super.onResponse(response);
		System.out.println("response=" + response);
		Gson gson = new Gson();
		NewsBeanJson mNewsBeanJson = gson.fromJson(response.toString(),
				NewsBeanJson.class);
		if (mNewsBeanJson != null && mNewsBeanJson.getResult() != null
				&& mNewsBeanJson.getResult().size() > 0) {
			newsBeanList.addAll(mNewsBeanJson.getResult());
			pageNo++;
			mNewsAdapter.notifyDataSetChanged();
		}
		mPullRefreshListView.onRefreshComplete();
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		// TODO Auto-generated method stub
		super.onErrorResponse(error);
		System.out.println("sorry,Error");
		mPullRefreshListView.onRefreshComplete();
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

			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT));
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

		if (pagerList.size() == 1) {
			viewPager.setVisibility(View.GONE);
			group.setVisibility(View.GONE);
		} else {
			viewPager.setVisibility(View.VISIBLE);
			group.setVisibility(View.VISIBLE);
		}
	}

	public ArrayList<NewsBean> parseList(String href) {
		ArrayList<NewsBean> list = new ArrayList<NewsBean>();
		try {
			href = ((CommonFragmentActivity)getActivity()).makeURL(href, new HashMap<String, Object>() {
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

			/**
			 * JSESSIONID=
			 * dc4dcbc5ecf258792d24c12faf0b8bb9d050763e38b8b0d2b43bf2d8cdd1640a;
			 * captcha=s%3Af075e21f410e9e2739aebd21d4817b45.QQ%2
			 * Bq05pDslaDGkdJFV0uL3ZZQBXtbqf36wYrGsvuQx8;
			 * Hm_lvt_15fafbae2b9b11d280c79eff3b840e45
			 * =1477272275,1477358473,1477445179,1477531397;
			 * Hm_lpvt_15fafbae2b9b11d280c79eff3b840e45=1477538812;
			 * CNZZDATA1255169715
			 * =1908767344-1456467599-http%253A%252F%252Fwww.yidianzixun
			 * .com%252F%7C1477536295;
			 * cn_9a154edda337ag57c050_dplus=%7B%22distinct_id
			 * %22%3A%20%221531c6fb95869
			 * -0a7506d4a-304a4d7d-1aeaa0-1531c6fb959c4%
			 * 22%2C%22%E6%9D%A5%E6%BA%90
			 * %E6%B8%A0%E9%81%93%22%3A%20%22%22%2C%22%
			 * 24session_id%22%3A%201456470997
			 * %2C%22%24initial_time%22%3A%20%221456467599
			 * %22%2C%22%24initial_referrer
			 * %22%3A%20%22http%3A%2F%2Fwww.yidianzixun
			 * .com%2Fhome%3Fpage%3Dchannel
			 * %26keyword%3D%25E4%25BA%2592%25E8%2581
			 * %2594%25E7%25BD%2591%22%2C%22
			 * %24initial_referring_domain%22%3A%20%
			 * 22www.yidianzixun.com%22%2C%22%
			 * 24_sessionid%22%3A%200%2C%22%24_sessionTime%22%3A%201477538873%2C%22%24dp%22%3A%200%2C%22%24_sessionPVTime%22%3A%201477538873%2C%22initial_view_time%22%3A%20%221468565764%22%2C%22initial_referrer%22%3A%20%22http%3A%2F%2Fwww.yidianzixun.com%2Fhome%22%2C%22initial_referrer_domain%22%3A%20%22www.yidianzixun.com%22%2C%22%24recent_outside_referrer%22%3A%20%22%24direct
			 * % 2 2 % 7 D
			 */
			Document doc = Jsoup.connect(href)
					.userAgent(SettingsActivity.userAgent)
					.cookies(SettingsActivity.getCookies()).timeout(10000)
					.get();
			try {

				Element tabcontent = doc.select("div.tab-content").first();
				// 图片导航
				Elements tabpannelElements = tabcontent
						.select("div.tab-pannel");
				/**
				 * <div class="tab-pannel"><a
				 * href="http://download.yidianzixun.com" target="_blank"
				 * class="slide-wrapper"><img src=
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

			} catch (Exception e) {
				pagerList.clear();
				pagerList.add(new NewsBean());
				e.printStackTrace();
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

			Elements sectionArticlesElements = sections
					.select("div.section-articles");
			for (int k = 0; k < sectionArticlesElements.size(); k++) {
				Elements beanElements = sectionArticlesElements.get(k).select(
						"div.article");
				// 其他标签
				// 解析文件
				for (int i = 0; i < beanElements.size(); i++) {
					NewsBean bean = new NewsBean();
					try {
						// 图片
						Element imageElement = beanElements.get(i);
						Element astyle = imageElement.select("a").first();
						String nexthref = UrlUtils.YI_DIAN_ZI_XUN
								+ astyle.attr("href");
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
							// 未登录<a
							// style="background-image:url(http://i1.go2yd.com/image.php?url=http://si1.go2yd.com/get-image/083pyOskXR2&amp;type=thumbnail_200x140);"
							// href="/home?page=article&amp;id=0EmjqaLo&amp;up=20"
							// target="_blank" class="article-img"></a>
							// 登录<a
							// style="background-image:url(http://si1.go2yd.com/get-image/083pyOskXR2&amp;type=thumbnail_200x140);"
							// href="/home?page=article&amp;id=0EmjqaLo&amp;up=20"
							// target="_blank" class="article-img"></a>
							if (SettingsActivity.getCookies() != null
									&& SettingsActivity.getCookies().size() > 0) {
								if (imageurl != null
										&& imageurl.length() >= 0
										&& imageurl
												.contains("background-image")) {
									Log.i(TAG, i + "=====" + y + "imageurl="
											+ imageurl);
									imgurlList.add(imageurl
											.replace("background-image:url(",
													"").replace(");", "")
											.replace("amp;", ""));
								}
							} else {
								if (imageurl != null
										&& imageurl.length() >= 0
										&& imageurl
												.contains("background-image")) {
									Log.i(TAG, i + "=====" + y + "imageurl="
											+ imageurl);
									imgurlList
											.add(imageurl
													.replace(
															"background-image:url(",
															"")
													.replace(");", "")
													.replace(
															"http://i1.go2yd.com/image.php?url=",
															"")
													.replace("amp;", ""));
								}
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
						Element p = contentElement.select(
								"div.article-nofloat p").first();
						String content = p.text();
						Log.i(TAG, i + "content = " + content);
						bean.setSummary(content);
					} catch (Exception e) {
						e.printStackTrace();
					}

					try {
						// 其他
						Element otherElement = beanElements.get(i);
						Element otherE = otherElement
								.select("div.article-info").first();
						String other = otherE.text();
						Log.i(TAG, i + "other = " + other);
						bean.setOther(other);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					try {
						// data-docid
						Element idElement = beanElements.get(i);
						Element idE = idElement.select("div.slide-like").first();
						String id = idE.attr("data-docid");
						String datatype = idE.attr("data-datatype");
						Log.i(TAG, i + "id = " + id+";datatype="+datatype);
						bean.setDocid(id);
						bean.setDtype(datatype);
						bean.setPath("interact|like-news");
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
	@Override
	protected void initUI(final boolean isVisibleToUser) {
		super.initUI(isVisibleToUser);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				if (mPullRefreshListView == null || getActivity() == null
						|| !isVisibleToUser) {
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
				((ViewPager) container).addView(mImageViews[position
						% mImageViews.length], 0);
			} catch (Exception e) {
				// handler something
			}
			return mImageViews[position % mImageViews.length];
		}

	}
}
