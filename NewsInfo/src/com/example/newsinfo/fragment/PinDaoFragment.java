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
import android.widget.GridView;

import com.example.newsinfo.R;
import com.example.newsinfo.UrlUtils;
import com.example.newsinfo.activity.PinDaoTabsActivity;
import com.example.newsinfo.activity.SearchResultActivity;
import com.example.newsinfo.activity.SettingsActivity;
import com.example.newsinfo.adapter.PinDaoAdapter;
import com.example.newsinfo.bean.NewsBean;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

/**
 ***************************************************************************************************************************************************************************** 
 * 频道
 * @author :fengguangjing
 * @createTime:2016-10-21下午2:48:41
 * @version:4.2.4
 * @modifyTime:
 * @modifyAuthor:
 * @description:
 ***************************************************************************************************************************************************************************** 
 */
public class PinDaoFragment extends Fragment {
	public static final String TAG = PinDaoFragment.class.getSimpleName();
	NewsBean newsBean;
	PullToRefreshGridView gridview;
	PinDaoAdapter pinDaoAdapter;
	ArrayList<NewsBean> list = new ArrayList<NewsBean>();

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
		gridview.setMode(Mode.PULL_FROM_START);
		gridview.setOnRefreshListener(new OnRefreshListener<GridView>() {

			@Override
			public void onRefresh(PullToRefreshBase<GridView> refreshView) {
				String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				// Update the LastUpdatedLabel
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				// Do work to refresh the list here.
				if (gridview.getCurrentMode() == Mode.PULL_FROM_START) {
					new GetDataTask().execute();
				}
			}
		});
		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//http://www.yidianzixun.com/api/q/?path=channel|news-list-for-channel&fields=docid&fields=category&fields=date&fields=image&fields=image_urls&fields=like&fields=source&fields=title&fields=url&fields=comment_count&fields=summary&fields=up&version=999999&infinite=true
		        //http://www.yidianzixun.com/home?page=channel&id=t96
				
				String jsondataurl = "http://www.yidianzixun.com/api/q/?path=channel|news-list-for-channel&fields=docid&fields=category&fields=date&fields=image&fields=image_urls&fields=like&fields=source&fields=title&fields=url&fields=comment_count&fields=summary&fields=up&version=999999&infinite=true";
				jsondataurl = jsondataurl + "&channel_id=" + list.get((int) id).getItemid();
				Intent intent = new Intent();
				intent.setClass(getActivity(), SearchResultActivity.class);
				intent.putExtra("NEWSBEAN", list.get((int) id));
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

	private class GetDataTask extends AsyncTask<Void, Void, NewsBean[]> {
		@Override
		protected NewsBean[] doInBackground(Void... params) {
			// Simulates a background job.
			ArrayList<NewsBean> list = new ArrayList<NewsBean>();
			try {
				// 解析网络标签
				list = parseList(UrlUtils.CHANNEL_LIST);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return list.toArray(new NewsBean[0]);
		}

		@Override
		protected void onPostExecute(NewsBean[] result) {
			super.onPostExecute(result);
			if (gridview.getCurrentMode() == Mode.PULL_FROM_START) {
				list.clear();
				list.addAll(Arrays.asList(result));
			}
			pinDaoAdapter.notifyDataSetChanged();
			// Call onRefreshComplete when the list has been refreshed.
			gridview.onRefreshComplete();
		}
	}

	public ArrayList<NewsBean> parseList(String href) {
		ArrayList<NewsBean> list = new ArrayList<NewsBean>();
		try {
			href = ((PinDaoTabsActivity) getActivity()).makeURL(href, new HashMap<String, Object>() {
				{
				}
			});
			Log.i("url", "url = " + href);
			Document doc = Jsoup.connect(href).userAgent(SettingsActivity.userAgent).cookies(SettingsActivity.getCookies())
					.timeout(10000).get();
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
							bean.setTitle(aElement.text());
							
							bean.setItemid(aElement.attr("href").replace("/home?page=channel&amp;id=", ""));
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