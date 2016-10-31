/**
 *****************************************************************************************************************************************************************************
 * 
 * @author :fengguangjing
 * @createTime:2016-10-21下午2:48:41
 * @version:4.2.4
 * @modifyTime:
 * @modifyAuthor:
 * @description:
 *****************************************************************************************************************************************************************************
 */
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
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.newsinfo.BaseV4Fragment;
import com.example.newsinfo.R;
import com.example.newsinfo.UrlUtils;
import com.example.newsinfo.activity.PinDaoTabsActivity;
import com.example.newsinfo.activity.SearchResultActivity;
import com.example.newsinfo.activity.SettingsActivity;
import com.example.newsinfo.adapter.PinDaoAdapter;
import com.example.newsinfo.bean.NewsBean;
import com.example.newsinfo.json.NewsBeanJson;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

/**
 ***************************************************************************************************************************************************************************** 
 * 频道
 * 
 * @author :fengguangjing
 * @createTime:2016-10-21下午2:48:41
 * @version:4.2.4
 * @modifyTime:
 * @modifyAuthor:
 * @description:
 ***************************************************************************************************************************************************************************** 
 */
public class PinDaoFragment extends BaseV4Fragment {
	NewsBean newsBean;
	PullToRefreshGridView gridview;
	PinDaoAdapter pinDaoAdapter;
	ArrayList<NewsBean> list = new ArrayList<NewsBean>();
	int pageNo = 1;
	String JSONDataUrl;

	public static PinDaoFragment newInstance(NewsBean newsBean) {
		PinDaoFragment fragment = new PinDaoFragment();
		fragment.newsBean = newsBean;
		return fragment;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_pindao, container, false);
		gridview = (PullToRefreshGridView) view.findViewById(R.id.gridview);

		pinDaoAdapter = new PinDaoAdapter(getActivity(), list);
		gridview.setAdapter(pinDaoAdapter);
		gridview.setMode(Mode.BOTH);
		gridview.setOnRefreshListener(new OnRefreshListener<GridView>() {

			@Override
			public void onRefresh(PullToRefreshBase<GridView> refreshView) {
				String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				// Update the LastUpdatedLabel
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				// Do work to refresh the list here.
				if (gridview.getCurrentMode() == Mode.PULL_FROM_START) {
					// new GetDataTask().execute();

					doAsync(PinDaoFragment.this, PinDaoFragment.this, PinDaoFragment.this);
				} else if (gridview.getCurrentMode() == Mode.PULL_FROM_END) {
					if (pageNo == 1) {
						volleyJson();
					}
				}
			}
		});
		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// http://www.yidianzixun.com/api/q/?path=channel|news-list-for-channel&fields=docid&fields=category&fields=date&fields=image&fields=image_urls&fields=like&fields=source&fields=title&fields=url&fields=comment_count&fields=summary&fields=up&version=999999&infinite=true
				// http://www.yidianzixun.com/home?page=channel&id=t96

				String jsondataurl = "http://www.yidianzixun.com/api/q/?path=channel|news-list-for-channel&fields=docid&fields=category&fields=date&fields=image&fields=image_urls&fields=like&fields=source&fields=title&fields=url&fields=comment_count&fields=summary&fields=up&version=999999&infinite=true";
				jsondataurl = jsondataurl + "&channel_id=" + list.get((int) id).getId();

				NewsBean intentBean = list.get((int) id);
				intentBean.setTitle(intentBean.getName());
				if (intentBean.getUrl() == null || "".equals(intentBean.getUrl())) {
					intentBean.setUrl(UrlUtils.YI_DIAN_ZI_XUN + "/home?page=channel&id=" + intentBean.getId());
				}

				Intent intent = new Intent();
				intent.setClass(getActivity(), SearchResultActivity.class);
				intent.putExtra("NEWSBEAN", intentBean);
				intent.putExtra("JSON_DATA_URL", jsondataurl);
				startActivity(intent);
			}
		});

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				gridview.setRefreshing(true);
			}
		}, 500);
		return view;
	}

	// private class GetDataTask extends AsyncTask<Void, Void, NewsBean[]> {
	// @Override
	// protected NewsBean[] doInBackground(Void... params) {
	// // Simulates a background job.
	// ArrayList<NewsBean> list = new ArrayList<NewsBean>();
	// try {
	// // 解析网络标签
	// list = parseList(UrlUtils.CHANNEL_LIST);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return list.toArray(new NewsBean[0]);
	// }
	//
	// @Override
	// protected void onPostExecute(NewsBean[] result) {
	// super.onPostExecute(result);
	// if (gridview.getCurrentMode() == Mode.PULL_FROM_START) {
	// list.clear();
	// list.addAll(Arrays.asList(result));
	// pageNo = 1;
	// }
	// pinDaoAdapter.notifyDataSetChanged();
	// // Call onRefreshComplete when the list has been refreshed.
	// gridview.onRefreshComplete();
	// }
	// }
	//

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.example.newsinfo.BaseV4Fragment#call()
	 */
	@Override
	public NewsBean[] call() throws Exception {
		// TODO Auto-generated method stub
		ArrayList<NewsBean> list = new ArrayList<NewsBean>();
		try {
			// 解析网络标签
			list = parseList(UrlUtils.CHANNEL_LIST);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list.toArray(new NewsBean[0]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.example.newsinfo.BaseV4Fragment#onCallback(com.example.newsinfo.bean
	 * .NewsBean[])
	 */
	@Override
	public void onCallback(NewsBean[] pCallbackValue) {
		// TODO Auto-generated method stub
		super.onCallback(pCallbackValue);
		if (gridview.getCurrentMode() == Mode.PULL_FROM_START) {
			list.clear();
			list.addAll(Arrays.asList(pCallbackValue));
			pageNo = 1;
		}
		pinDaoAdapter.notifyDataSetChanged();
		// Call onRefreshComplete when the list has been refreshed.
		gridview.onRefreshComplete();
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
			JSONDataUrl = newsBean.getJsondataurl();
		}
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, JSONDataUrl, SettingsActivity.getHeaders(), null, this, this);
		requestQueue.add(jsonObjectRequest);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.example.newsinfo.BaseV4Fragment#onResponse(org.json.JSONObject)
	 */
	@Override
	public void onResponse(JSONObject response) {
		// TODO Auto-generated method stub
		super.onResponse(response);
		System.out.println("response=" + response);
		Gson gson = new Gson();
		NewsBeanJson mNewsBeanJson = gson.fromJson(response.toString(), NewsBeanJson.class);
		if (mNewsBeanJson != null && mNewsBeanJson.getChannels() != null && mNewsBeanJson.getChannels().size() > 0) {
			list.addAll(mNewsBeanJson.getChannels());
			pageNo++;
			pinDaoAdapter.notifyDataSetChanged();
		}
		gridview.onRefreshComplete();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.example.newsinfo.BaseV4Fragment#onErrorResponse(com.android.volley
	 * .VolleyError)
	 */
	@Override
	public void onErrorResponse(VolleyError error) {
		// TODO Auto-generated method stub
		super.onErrorResponse(error);
		System.out.println("sorry,Error");
		gridview.onRefreshComplete();
	}

	public ArrayList<NewsBean> parseList(String href) {
		ArrayList<NewsBean> list = new ArrayList<NewsBean>();
		try {
			href = ((PinDaoTabsActivity) getActivity()).makeURL(href, new HashMap<String, Object>() {
				{
				}
			});
			Log.i("url", "url = " + href);
			Document doc = Jsoup.connect(href).userAgent(SettingsActivity.userAgent).cookies(SettingsActivity.getCookies()).timeout(10000).get();
			Element masthead = doc.select("div.channellist").first();
			Elements beanElements = masthead.select("div.cate-box");

			// 解析文件
			for (int i = 0; i < beanElements.size(); i++) {
				try {
					Element imageElement = beanElements.get(i).select("h3").first();
					String title = imageElement.text();
					Log.i(TAG, i + "title = " + title);
					if (title.equals(newsBean.getTitle())) {
						// 解析子列表数据
						/**
						 * <li><a href="/home?page=channel&amp;id=t96"
						 * target="_blank"> <img src=
						 * "http://i1.go2yd.com/image.php?url=http://s.go2yd.com/b/id5m6d96_b20sd1d1.jpg&amp;type=thumbnail_100x100"
						 * >
						 * <h4>台湾政坛</h4>
						 * </a><span data-cname="台湾政坛" title="订阅"
						 * class="sub"></span></li>
						 */
						Elements liElements = beanElements.get(i).select("li");
						for (int y = 0; y < liElements.size(); y++) {
							NewsBean bean = new NewsBean();
							Element aElement = liElements.get(y).select("a").first();
							bean.setName(aElement.text());

							bean.setId(aElement.attr("href").replace("/home?page=channel&amp;id=", ""));
							bean.setUrl(UrlUtils.YI_DIAN_ZI_XUN + aElement.attr("href"));

							Element imgElement = liElements.get(y).select("img").first();
							bean.setImage(imgElement.attr("src"));

							list.add(bean);
						}
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}
}