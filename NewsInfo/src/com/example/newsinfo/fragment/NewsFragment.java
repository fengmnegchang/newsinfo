package com.example.newsinfo.fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.newsinfo.R;
import com.example.newsinfo.UrlUtils;
import com.example.newsinfo.activity.WebViewActivity;
import com.example.newsinfo.adapter.NewsAdapter;
import com.example.newsinfo.bean.NewsBean;
import com.example.newsinfo.json.NewsBeanJson;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class NewsFragment extends Fragment {
	private static final String TAG = NewsFragment.class.getSimpleName();
	private static final String KEY_CONTENT = "TestFragment:Content";
	PullToRefreshListView mPullRefreshListView;
	ArrayList<NewsBean> newsBeanList = new ArrayList<NewsBean>();
	NewsAdapter mNewsAdapter;
	// 热点
	String href;
	int pageNo = 1;
	String JSONDataUrl;

	public static NewsFragment newInstance(String content, String href, String JSONDataUrl) {
		NewsFragment fragment = new NewsFragment();
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
		mPullRefreshListView.setMode(Mode.BOTH);
		mNewsAdapter = new NewsAdapter(getActivity(), newsBeanList,mContent);
		// Set a listener to be invoked when the list should be refreshed.
		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				// Update the LastUpdatedLabel
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				// Do work to refresh the list here.
				if (mPullRefreshListView.getCurrentMode() == Mode.PULL_FROM_START) {
					new GetDataTask().execute();
				} else if (mPullRefreshListView.getCurrentMode() == Mode.PULL_FROM_END) {
					volleyJson();
				}
			}
		});
		mPullRefreshListView.setAdapter(mNewsAdapter);
		mPullRefreshListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent();		
				intent.setClass(getActivity(), WebViewActivity.class);
				intent.putExtra("NEWSBEAN", newsBeanList.get((int)id));
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
			super.onPostExecute(result);
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(KEY_CONTENT, mContent);
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
			JSONDataUrl = JSONDataUrl + "&cstart=" + pageNo * 10 + "&cend=" + (pageNo + 1) * 10;
		}
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, JSONDataUrl, null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				System.out.println("response=" + response);
				Gson gson = new Gson();
				NewsBeanJson mNewsBeanJson = gson.fromJson(response.toString(), NewsBeanJson.class);
				if (mNewsBeanJson != null && mNewsBeanJson.getResult() != null && mNewsBeanJson.getResult().size() > 0) {
					newsBeanList.addAll(mNewsBeanJson.getResult());
					pageNo++;
					mNewsAdapter.notifyDataSetChanged();
				}
				mPullRefreshListView.onRefreshComplete();
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError arg0) {
				System.out.println("sorry,Error");
				mPullRefreshListView.onRefreshComplete();
			}
		});
		requestQueue.add(jsonObjectRequest);
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
			Element masthead = doc.select("div.section-articles").first();
			Elements beanElements = masthead.select("div.article");

			/**
			 * <div data-docid="{{docid}}" class="article article-pic"> <a
			 * href="/home?page=article&id={{docid}}&up={{up}}" target="_blank"
			 * class="article-pic-item"><img src="{{image}}"> </a> <div
			 * class="article-nofloat"> <div class="article-info"> <div
			 * class="article-opts"> <div data-docid="{{docid}}"
			 * data-datatype="0" class="slide-del"></div> <div
			 * data-docid="{{docid}}" data-datatype="0"
			 * class="slide-like ">{{like}}</div><a
			 * href="/home?page=article&id={{docid}}&up={{up}}#comment"
			 * target="_blank" class="slide-comment">{{comment_count}}</a> <div
			 * data-sharetitle="{{title}}" data-shareimage="{{image}}"
			 * data-shareurl="http://www.yidianzixun.com/article/{{docid}}?s=4"
			 * class="slide-share">转发</div> </div><span
			 * class="article-source">{{source}}</span><span
			 * class="article-date">{{date}}</span> <div class="clear"></div>
			 * </div> </div> </div>
			 */

			/**
			 * <div data-docid="0EhiwNzx" class="article article-pic">
			 * <a href="/home?page=article&amp;id=0EhiwNzx&amp;up=3535" target="_blank" class="article-pic-item">
			 * <img src="http://static.yidianzixun.com/beauty/imgs/i_000cETSS.jpg"></a><
			 * div class="article-nofloat">
			 * <div class="article-info"><div class="article-opts"><div data-docid="0EhiwNzx" data-datatype="0" class="slide-del"></div>
			 * <div data-docid="0EhiwNzx" data-datatype="0" class="slide-like ">99</div>
			 * <a href="/home?page=article&amp;id=0EhiwNzx&amp;up=3535#comment" target="_blank" class="slide-comment">2</a>
			 * <div data-sharetitle="看到眼神心都化了，别总惦记往下看" data-shareimage="" data-shareurl="http://www.yidianzixun.com/article/0EhiwNzx?s=4" class="slide-share">转发</div>
			 * </div><span class="article-source">一点资讯</span>
			 * <span class="article-date">2016-10-19 11:26:36</span><div class="clear"></div></div></div></div>
			 */
			//美女标签
			Elements picElements = masthead.select("div.article-pic");
			if (picElements != null && picElements.size() > 0) {
				for (int i = 0; i < picElements.size(); i++) {
					NewsBean bean = new NewsBean();
					try {
						// 图片
						Element imageElement = picElements.get(i);
						Element astyle = imageElement.select("a").first();
						String nexthref = UrlUtils.YI_DIAN_ZI_XUN + astyle.attr("href");
						Log.i(TAG, i + "nexthref = " + nexthref);
						bean.setUrl(nexthref);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					try {
						// 图片
						//<img src="http://static.yidianzixun.com/beauty/imgs/i_000cETSS.jpg">
						Element imageElement = beanElements.get(i);
						Elements aElements = imageElement.select("img");
						ArrayList<String> imgurlList = new ArrayList<String>();
						for (int y = 0; y < aElements.size(); y++) {
							Element astyle = aElements.get(y);
							String imageurl = astyle.attr("src");
							if (imageurl != null && imageurl.length() >= 0 ) {
								Log.i(TAG, i + "=====" + y + "imageurl=" + imageurl);
								imgurlList.add(imageurl);
							}
						}
						bean.setImage_urls(imgurlList);
					}catch (Exception e) {
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
			} else {
				//其他标签
				// 解析文件
				for (int i = 0; i < beanElements.size(); i++) {
					NewsBean bean = new NewsBean();
					try {
						// 图片
						Element imageElement = beanElements.get(i);
						Element astyle = imageElement.select("a").first();
						String nexthref = UrlUtils.YI_DIAN_ZI_XUN + astyle.attr("href");
						// 3imageurl =
						// background-image:url('http://i1.go2yd.com/image.php?url=http://si1.go2yd.com/get-image/07pb1QpTNho&type=thumbnail_200x140');
						// ;nexthref =
						// http://www.yidianzixun.com//home?page=article&id=0Ehnnyy6&up=403
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
								imgurlList.add(imageurl.replace("background-image:url('", "").replace("');", ""));
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
}
