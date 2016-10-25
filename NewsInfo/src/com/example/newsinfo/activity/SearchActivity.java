/**
 *****************************************************************************************************************************************************************************
 * 
 * @author :fengguangjing
 * @createTime:2016-10-25下午2:47:49
 * @version:4.2.4
 * @modifyTime:
 * @modifyAuthor:
 * @description:
 *****************************************************************************************************************************************************************************
 */
package com.example.newsinfo.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.newsinfo.R;
import com.example.newsinfo.UrlUtils;
import com.example.newsinfo.adapter.SearchAdapter;
import com.example.newsinfo.bean.NewsBean;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

/**
 ***************************************************************************************************************************************************************************** 
 * 
 * @author :fengguangjing
 * @createTime:2016-10-25下午2:47:49
 * @version:4.2.4
 * @modifyTime:
 * @modifyAuthor:
 * @description:
 ***************************************************************************************************************************************************************************** 
 */
public class SearchActivity extends Activity {
	private static final String TAG = SearchActivity.class.getSimpleName();
	PullToRefreshGridView gridview;
	SearchAdapter searchAdapter;
	ArrayList<NewsBean> list = new ArrayList<NewsBean>();
	ImageView search_btn,owner_logo;
	EditText edit_search;
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		search_btn = (ImageView) findViewById(R.id.search_btn);
		owner_logo = (ImageView) findViewById(R.id.owner_logo);
		edit_search = (EditText) findViewById(R.id.edit_search);
		search_btn.setVisibility(View.VISIBLE);
		owner_logo.setVisibility(View.GONE);
		search_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			   //http://www.yidianzixun.com/home?page=channel&keyword=%E5%AE%9E%E7%9B%98
				String search = edit_search.getText().toString();
				if(search.length()>0){
					NewsBean bean = new NewsBean();
					try {
						bean.setKeyword(URLEncoder.encode(search, "UTF-8"));
						bean.setUrl(UrlUtils.YI_DIAN_ZI_XUN+"/home?page=channel&keyword="+URLEncoder.encode(search, "UTF-8"));
						bean.setTitle(search);
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					Intent intent = new Intent();
					intent.setClass(SearchActivity.this, SearchResultActivity.class);
					intent.putExtra("NEWSBEAN", bean );
					startActivity(intent);
				}
				
			}
		});
		
		searchAdapter = new SearchAdapter(this, list);
		
		gridview = (PullToRefreshGridView) findViewById(R.id.gridview);
		gridview.setAdapter(searchAdapter);
		gridview.setMode(Mode.PULL_FROM_START);
		gridview.setOnRefreshListener(new OnRefreshListener<GridView>() {

			@Override
			public void onRefresh(PullToRefreshBase<GridView> refreshView) {
				String label = DateUtils.formatDateTime(SearchActivity.this, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
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
				Intent intent = new Intent();
				intent.setClass(SearchActivity.this, SearchResultActivity.class);
				intent.putExtra("NEWSBEAN", list.get((int) id));
				startActivity(intent);
			}
		});
		
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				gridview.setRefreshing(true);
			}
		}, 500);
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
			Log.i(TAG, "getMode ===" + gridview.getCurrentMode());
			if (gridview.getCurrentMode() == Mode.PULL_FROM_START) {
				list.clear();
				list.addAll(Arrays.asList(result));
			}
			searchAdapter.notifyDataSetChanged();
			// Call onRefreshComplete when the list has been refreshed.
			gridview.onRefreshComplete();
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

			/**
			 * <div class="search-wrapper"><div class="section-recent_sub"><h2>
			 * 大家正在搜</h2>
			 * <ul>
			 * <li><a href=
			 * "/home?page=channel&amp;keyword=%E9%87%91%E5%BA%B8%E7%8A%B6%E5%91%8A%E6%B1%9F%E5%8D%97"
			 * >大家都在搜#金庸状告江南#</a></li>
			 * <li><a
			 * href="/home?page=channel&amp;keyword=%E7%BB%98%E6%9C%AC">大家都在搜
			 * #绘本#</a></li>
			 * <li><a href=
			 * "/home?page=channel&amp;keyword=%E6%B5%B7%E5%B2%9B%E6%97%85%E6%B8%B8"
			 * >大家都在搜#海岛旅游#</a></li>
			 * <li><a href=
			 * "/home?page=channel&amp;keyword=%E8%92%8B%E9%9B%AF%E4%B8%BD"
			 * >大家都在搜#蒋雯丽#</a></li>
			 * <li><a href=
			 * "/home?page=channel&amp;keyword=%E5%B9%BF%E5%B7%9E%E5%91%A8%E8%BE%B9%E6%B8%B8"
			 * >大家都在搜#广州周边游#</a></li>
			 * <li><a href=
			 * "/home?page=channel&amp;keyword=%E5%9B%BD%E5%86%85%E6%B8%B8"
			 * >大家都在搜#国内游#</a></li>
			 * <li><a href=
			 * "/home?page=channel&amp;keyword=%E5%AE%9D%E5%AE%9D%E8%90%A5%E5%85%BB"
			 * >大家都在搜#宝宝营养#</a></li>
			 * <li><a href=
			 * "/home?page=channel&amp;keyword=%E7%8E%8B%E5%AE%9D%E5%BC%BA%E4%B9%A0%E6%AD%A6%E7%85%A7"
			 * >大家都在搜#王宝强习武照#</a></li>
			 * <li><a href=
			 * "/home?page=channel&amp;keyword=%E5%84%BF%E7%AB%A5%E5%81%A5%E5%BA%B7"
			 * >大家都在搜#儿童健康#</a></li>
			 * </ul>
			 * </div><div class="section-hotwords_more"><h2>热门搜词</h2>
			 * <ul>
			 * <li><a href=
			 * "/home?page=channel&amp;keyword=%E4%B8%AD%E5%9B%BD%E8%88%B9%E5%91%98%E4%BA%BA%E8%B4%A8%E8%8E%B7%E6%95%91"
			 * style="background-color: rgb(233, 243, 234);">中国船员人质获救</a></li>
			 * <li><a href=
			 * "/home?page=channel&amp;keyword=%E8%A3%B4%E5%8B%87%E4%BF%8A%E7%88%B1%E5%A6%BB%E7%94%9F%E5%AD%90"
			 * style="background-color: rgb(237, 235, 232);">裴勇俊爱妻生子</a></li>
			 * <li><a href=
			 * "/home?page=channel&amp;keyword=%E7%94%B7%E7%AB%A5%E9%81%AD%E5%A7%91%E5%A7%A5%E9%94%A4%E6%9D%80"
			 * style="background-color: rgb(234, 230, 249);">男童遭姑姥锤杀</a></li>
			 * <li><a href=
			 * "/home?page=channel&amp;keyword=100%E4%B8%87%E5%85%83%E6%89%93%E8%B5%8F%E4%B8%BB%E6%92%AD"
			 * style="background-color: rgb(236, 244, 235);">100万元打赏主播</a></li>
			 * <li><a href=
			 * "/home?page=channel&amp;keyword=%E9%87%91%E5%BA%B8%E7%8A%B6%E5%91%8A%E6%B1%9F%E5%8D%97"
			 * style="background-color: rgb(237, 234, 245);">金庸状告江南</a></li>
			 * <li><a href=
			 * "/home?page=channel&amp;keyword=%E9%87%91%E6%98%9F%E8%87%AA%E6%8B%8D%E5%B0%91%E5%A5%B3%E9%A3%8E"
			 * style="background-color: rgb(241, 233, 238);">金星自拍少女风</a></li>
			 * <li><a href=
			 * "/home?page=channel&amp;keyword=%E9%98%BF%E8%81%94%E4%B8%BB%E5%8A%A8%E8%A6%81%E6%B1%82%E8%A2%AB%E8%A3%81"
			 * style="background-color: rgb(235, 240, 244);">阿联主动要求被裁</a></li>
			 * <li><a href=
			 * "/home?page=channel&amp;keyword=%E5%9D%90%E5%86%A4%E7%8B%B1%E7%94%B7%E5%AD%90%E7%BB%93%E5%A9%9A"
			 * style="background-color: rgb(240, 241, 249);">坐冤狱男子结婚</a></li>
			 * <li><a href=
			 * "/home?page=channel&amp;keyword=%E7%BE%8E%E5%9B%BD%E4%BA%92%E8%81%94%E7%BD%91%E7%98%AB%E7%97%AA"
			 * style="background-color: rgb(241, 249, 249);">美国互联网瘫痪</a></li>
			 * <li><a href=
			 * "/home?page=channel&amp;keyword=%E6%AC%A7%E9%98%B3%E5%A8%9C%E5%A8%9C%E6%83%8A%E5%91%86%E8%87%AA%E5%B7%B1"
			 * style="background-color: rgb(241, 235, 239);">欧阳娜娜惊呆自己</a></li>
			 * <li><a href=
			 * "/home?page=channel&amp;keyword=%E6%9B%9D%E8%B5%B5%E8%96%87%E8%B1%AA%E5%AE%85%E5%86%85%E5%A4%96%E6%99%AF"
			 * style="background-color: rgb(244, 241, 244);">曝赵薇豪宅内外景</a></li>
			 * <li><a href=
			 * "/home?page=channel&amp;keyword=%E6%A3%89%E7%BA%B1%E5%A0%B5%E9%87%87%E6%A0%B7%E5%99%A8%E9%80%A0%E5%81%87"
			 * style="background-color: rgb(234, 233, 231);">棉纱堵采样器造假</a></li>
			 * <li><a href=
			 * "/home?page=channel&amp;keyword=%E9%87%8C%E7%9A%AE%E5%87%BA%E4%BB%BB%E5%9B%BD%E8%B6%B3%E4%B8%BB%E5%B8%85"
			 * style="background-color: rgb(237, 235, 238);">里皮出任国足主帅</a></li>
			 * <li><a href=
			 * "/home?page=channel&amp;keyword=%E5%A5%B3%E5%AD%A9%E6%98%86%E6%98%8E%E7%9B%B8%E4%BA%B2%E8%A2%AB%E9%AA%97"
			 * style="background-color: rgb(235, 235, 240);">女孩昆明相亲被骗</a></li>
			 * <li><a href=
			 * "/home?page=channel&amp;keyword=%E6%95%91%E5%8A%A9%E5%9F%BA%E5%9C%B0%E7%8C%AB%E7%8B%97%E9%A5%BF%E6%AD%BB"
			 * style="background-color: rgb(242, 239, 242);">救助基地猫狗饿死</a></li>
			 * <li><a href=
			 * "/home?page=channel&amp;keyword=%E6%8B%86%E8%BF%81%E5%BA%9F%E5%A2%9F%E7%8E%B0%E5%8D%83%E4%BA%BA%E5%AE%B4"
			 * style="background-color: rgb(236, 240, 237);">拆迁废墟现千人宴</a></li>
			 * <li><a href=
			 * "/home?page=channel&amp;keyword=%E8%8B%8F%E9%86%92%E5%86%99%E6%8A%A4%E5%A3%AB%E6%B2%A1%E5%90%83%E9%A5%AD"
			 * style="background-color: rgb(249, 238, 249);">苏醒写护士没吃饭</a></li>
			 * <li><a href=
			 * "/home?page=channel&amp;keyword=%E8%A5%BF%E6%B8%B8%E4%BC%8F%E5%A6%96%E7%AF%87%E5%AE%9A%E6%A1%A3"
			 * style="background-color: rgb(243, 235, 240);">西游伏妖篇定档</a></li>
			 * <li><a href=
			 * "/home?page=channel&amp;keyword=%E5%BC%A0%E4%B8%B9%E5%B3%B0%E5%8A%9B%E6%8C%BA%E6%B4%AA%E6%AC%A3"
			 * style="background-color: rgb(245, 237, 244);">张丹峰力挺洪欣</a></li>
			 * <li><a href=
			 * "/home?page=channel&amp;keyword=%E4%BA%A4%E8%AD%A6%E8%AE%BE%E5%B2%97%E6%94%B6%E9%92%B1"
			 * style="background-color: rgb(245, 241, 234);">交警设岗收钱</a></li>
			 * <li><a href=
			 * "/home?page=channel&amp;keyword=%E8%8A%B115%E4%B8%87%E8%A3%85%E9%94%99%E6%88%BF%E5%AD%90"
			 * style="background-color: rgb(232, 235, 247);">花15万装错房子</a></li>
			 * <li><a href=
			 * "/home?page=channel&amp;keyword=Ella%E5%AE%A3%E5%B8%83%E6%80%80%E5%AD%95"
			 * style="background-color: rgb(234, 234, 232);">Ella宣布怀孕</a></li>
			 * <li><a href=
			 * "/home?page=channel&amp;keyword=%E8%92%8B%E9%9B%AF%E4%B8%BD%E7%A7%81%E7%85%A7%E6%9B%9D%E5%85%89"
			 * style="background-color: rgb(245, 244, 239);">蒋雯丽私照曝光</a></li>
			 * <li><a href=
			 * "/home?page=channel&amp;keyword=%E8%91%A3%E5%8A%9B%E6%8A%B1%E9%98%BF%E6%8B%89%E8%95%BE%E4%BA%AE%E7%9B%B8"
			 * style="background-color: rgb(243, 242, 234);">董力抱阿拉蕾亮相</a></li>
			 * <li><a href=
			 * "/home?page=channel&amp;keyword=%E7%A9%BA%E8%B0%83%E5%B7%A513%E6%A5%BC%E5%9D%A0%E4%BA%A1"
			 * style="background-color: rgb(237, 244, 249);">空调工13楼坠亡</a></li>
			 * <li><a href=
			 * "/home?page=channel&amp;keyword=%E5%8A%A0%E6%B2%B9%E7%AB%99%E6%B1%BD%E6%B2%B9%E9%87%8C%E6%8E%BA%E6%B0%B4"
			 * style="background-color: rgb(243, 239, 237);">加油站汽油里掺水</a></li>
			 * <li><a href=
			 * "/home?page=channel&amp;keyword=360%E4%BA%91%E7%9B%98%E5%85%B3%E9%97%AD"
			 * style="background-color: rgb(231, 237, 233);">360云盘关闭</a></li>
			 * <li><a href=
			 * "/home?page=channel&amp;keyword=%E6%B3%95%E5%9B%BD%E4%BE%A6%E5%AF%9F%E6%9C%BA%E5%9D%A0%E6%AF%81"
			 * style="background-color: rgb(243, 248, 242);">法国侦察机坠毁</a></li>
			 * </ul>
			 * </div></div></div>
			 */
			Document doc = Jsoup.connect(href).userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31").timeout(10000).get();
			Element masthead = doc.select("div.search-wrapper").first();
			Elements beanElements = masthead.select("li");

			// 解析文件
			for (int i = 0; i < beanElements.size(); i++) {
				NewsBean bean = new NewsBean();
				try {
					// 图片
					Element imageElement = beanElements.get(i);
					Element astyle = imageElement.select("a").first();
					String nexthref = UrlUtils.YI_DIAN_ZI_XUN + astyle.attr("href");
					Log.i(TAG, i + "nexthref = " + nexthref);
					bean.setUrl(nexthref);
					
					String keyword = astyle.attr("href").replace("/home?page=channel&amp;keyword=", "");
					Log.i(TAG, i + "keyword = " + keyword);
					bean.setKeyword(keyword);
					
					String title = astyle.text();
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

}
