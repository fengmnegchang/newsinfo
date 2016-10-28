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
import com.example.newsinfo.fragment.PinDaoFragment;
import com.example.newsinfo.indicator.TabPageIndicator;

/**
 * 
 ***************************************************************************************************************************************************************************** 
 * 频道页面
 * 
 * @author :fengguangjing
 * @createTime:2016-10-28上午10:35:55
 * @version:4.2.4
 * @modifyTime:
 * @modifyAuthor:
 * @description:
 ***************************************************************************************************************************************************************************** 
 */
public class PinDaoTabsActivity extends CommonFragmentActivity {
	public static final String TAG = PinDaoTabsActivity.class.getSimpleName();
	ArrayList<NewsBean> channelList = new ArrayList<NewsBean>();
	FragmentPagerAdapter adapter;
	TabPageIndicator indicator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setCommonActivityLeftCanBack(false);
		setCommonActivityCenterEditSearch(false);
		setCommonActivityRightSearch(false);

		addContentView(R.layout.activity_pindao_tabs, UrlUtils.STATUS_TAB_ACTIVITY_MARGIN_TOP);

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
		text_title.setText("频道");
		setRightNone();
		setLeftNone();

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
			return PinDaoFragment.newInstance(channelList.get(position));
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
				list = parseList(UrlUtils.CHANNEL_LIST);
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
			Element masthead = doc.select("div.channellist").first();
			Elements beanElements = masthead.select("div.cate-box");

			/**
			 * <div class="cate-box"><h3>热门</h3>
			 * <ul>
			 * <li><a href="/home?page=channel&amp;id=t96" target="_blank"><img
			 * src=
			 * "http://i1.go2yd.com/image.php?url=http://s.go2yd.com/b/id5m6d96_b20sd1d1.jpg&amp;type=thumbnail_100x100"
			 * >
			 * <h4>台湾政坛</h4></a><span data-cname="台湾政坛" title="订阅"
			 * class="sub"></span></li>
			 * <li><a href="/home?page=channel&amp;id=e20502"
			 * target="_blank"><img src=
			 * "http://i1.go2yd.com/image.php?url=http://s.go2yd.com/b/iu6sk3cq_8y00b6b6.jpg&amp;type=thumbnail_100x100"
			 * >
			 * <h4>蔡英文</h4></a><span data-cname="蔡英文" title="订阅"
			 * class="sub"></span></li>
			 * <li><a href="/home?page=channel&amp;id=u630" target="_blank"><img
			 * src=
			 * "http://i1.go2yd.com/image.php?url=http://s.go2yd.com/b/idgzm5gn_7b01d1d1.jpg&amp;type=thumbnail_100x100"
			 * >
			 * <h4>我是歌手</h4></a><span data-cname="我是歌手" title="订阅"
			 * class="sub"></span></li>
			 * <li><a href="/home?page=channel&amp;id=e19695"
			 * target="_blank"><img src=
			 * "http://i1.go2yd.com/image.php?url=http://s.go2yd.com/b/iee1gfty_5j00d1d1.jpg&amp;type=thumbnail_100x100"
			 * >
			 * <h4>综艺</h4></a><span data-cname="综艺" title="订阅"
			 * class="sub"></span></li>
			 * <li><a href="/home?page=channel&amp;id=e23526"
			 * target="_blank"><img src=
			 * "http://i1.go2yd.com/image.php?url=http://s.go2yd.com/b/ij2ldlwl_490j8c8c.jpg&amp;type=thumbnail_100x100"
			 * >
			 * <h4>太子妃升职记</h4></a><span data-cname="太子妃升职记" title="订阅"
			 * class="sub"></span></li>
			 * <li><a href="/home?page=channel&amp;id=u13537"
			 * target="_blank"><img src=
			 * "http://i1.go2yd.com/image.php?url=http://s.go2yd.com/b/ihwsg44j_eq00d1d1.jpg&amp;type=thumbnail_100x100"
			 * >
			 * <h4>芈月传</h4></a><span data-cname="芈月传" title="订阅"
			 * class="sub"></span></li>
			 * <li><a href="/home?page=channel&amp;id=u6587"
			 * target="_blank"><img src=
			 * "http://i1.go2yd.com/image.php?url=http://s.go2yd.com/b/idsdav44_7d0fd1d1.jpg&amp;type=thumbnail_100x100"
			 * >
			 * <h4>快播</h4></a><span data-cname="快播" title="订阅"
			 * class="sub"></span></li>
			 * <li><a href="/home?page=channel&amp;id=u13386"
			 * target="_blank"><img src=
			 * "http://i1.go2yd.com/image.php?url=http://s.go2yd.com/b/ich75hjm_3e0ud1d1.jpg&amp;type=thumbnail_100x100"
			 * >
			 * <h4>股票</h4></a><span data-cname="股票" data-cnid="3474576933"
			 * title="取消订阅" class="subed"></span></li>
			 * <li><a href="/home?page=channel&amp;id=u7837"
			 * target="_blank"><img src=
			 * "http://i1.go2yd.com/image.php?url=http://s.go2yd.com/b/id9soju2_9e00d1d1.jpg&amp;type=thumbnail_100x100"
			 * >
			 * <h4>年终奖</h4></a><span data-cname="年终奖" title="订阅"
			 * class="sub"></span></li>
			 * <li><a href="/home?page=channel&amp;id=u6702"
			 * target="_blank"><img src=
			 * "http://i1.go2yd.com/image.php?url=http://s.go2yd.com/b/icih7ktn_2x0ud1d1.jpg&amp;type=thumbnail_100x100"
			 * >
			 * <h4>职场</h4></a><span data-cname="职场" title="订阅"
			 * class="sub"></span></li>
			 * <li><a href="/home?page=channel&amp;id=t77" target="_blank"><img
			 * src=
			 * "http://i1.go2yd.com/image.php?url=http://s.go2yd.com/b/idbbafdm_7i00d1d1.jpg&amp;type=thumbnail_100x100"
			 * >
			 * <h4>星座</h4></a><span data-cname="星座" title="订阅"
			 * class="sub"></span></li>
			 * <li><a href="/home?page=channel&amp;id=u8505"
			 * target="_blank"><img src=
			 * "http://i1.go2yd.com/image.php?url=http://s.go2yd.com/b/id5ice42_3y00d1d1.jpg&amp;type=thumbnail_100x100"
			 * >
			 * <h4>二次元</h4></a><span data-cname="二次元" title="订阅"
			 * class="sub"></span></li>
			 * <li><a href="/home?page=channel&amp;id=u7993"
			 * target="_blank"><img src=
			 * "http://i1.go2yd.com/image.php?url=http://s.go2yd.com/b/idayu855_1g0sd1d1.jpg&amp;type=thumbnail_100x100"
			 * >
			 * <h4>冬季时尚</h4></a><span data-cname="冬季时尚" title="订阅"
			 * class="sub"></span></li>
			 * <li><a href="/home?page=channel&amp;id=u7514"
			 * target="_blank"><img src=
			 * "http://i1.go2yd.com/image.php?url=http://s.go2yd.com/b/icn4vll1_cl01d1d1.jpg&amp;type=thumbnail_100x100"
			 * >
			 * <h4>滑雪</h4></a><span data-cname="滑雪" title="订阅"
			 * class="sub"></span></li>
			 * </ul>
			 * <ul class="channel-more">
			 * </ul>
			 * <div data-cid="hot" class="show-more">展开更多</div></div>
			 */
			// 解析文件
			for (int i = 0; i < beanElements.size(); i++) {
				NewsBean bean = new NewsBean();
				try {
					Element imageElement = beanElements.get(i).select("h3").first();
					String title = imageElement.text();
					Log.i(TAG, i + "title = " + title);
					bean.setTitle(title);

					
					Element dataElement = beanElements.get(i).select("div.show-more").first();
					String jsondataurl = UrlUtils.YI_DIAN_ZI_XUN + "/home/q?type=getmorechannels&cid="+dataElement.attr("data-cid");
					Log.i(TAG, i + "jsondataurl = " + jsondataurl);
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
