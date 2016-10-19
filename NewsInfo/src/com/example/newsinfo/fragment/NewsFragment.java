package com.example.newsinfo.fragment;

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
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.newsinfo.R;
import com.example.newsinfo.UrlUtils;
import com.example.newsinfo.adapter.NewsAdapter;
import com.example.newsinfo.bean.NewsBean;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public final class NewsFragment extends Fragment {
	private static final String TAG = NewsFragment.class.getSimpleName();
    private static final String KEY_CONTENT = "TestFragment:Content";
    PullToRefreshListView mPullRefreshListView;
    ArrayList<NewsBean> newsBeanList = new ArrayList<NewsBean>();
    NewsAdapter mNewsAdapter;
    //热点
    String  href = "http://www.yidianzixun.com/home?page=channel&id=hot";
    int pageNo =1;
    
    public static NewsFragment newInstance(String content) {
    	NewsFragment fragment = new NewsFragment();
        fragment.mContent = content;
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
        mPullRefreshListView.setMode(Mode.PULL_FROM_START);
        mNewsAdapter = new NewsAdapter(getActivity(),newsBeanList);
     // Set a listener to be invoked when the list should be refreshed.
 		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
 			@Override
 			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
 				String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
 						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
 				// Update the LastUpdatedLabel
 				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
 				// Do work to refresh the list here.
     			new GetDataTask().execute();
 			}
 		});
 		
 		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				mPullRefreshListView.setRefreshing(true);
			}
		}, 1000);
        mPullRefreshListView.setAdapter(mNewsAdapter);
     // Do work to refresh the list here.
        return view;
    }

    private class GetDataTask extends AsyncTask<Void, Void, NewsBean[]> {

		@Override
		protected NewsBean[] doInBackground(Void... params) {
			// Simulates a background job.
			ArrayList<NewsBean> list = new ArrayList<NewsBean>();
			try {
				//解析网络标签
				list = parseList(href,pageNo);
			} catch (Exception e) {
			}
			return list.toArray(new NewsBean[0]);
		}

		@Override
		protected void onPostExecute(NewsBean[] result) {
			Log.i(TAG, "getMode ==="+mPullRefreshListView.getCurrentMode());
			if(mPullRefreshListView.getCurrentMode() == Mode.PULL_FROM_START){
				newsBeanList.clear();
				newsBeanList.addAll(Arrays.asList(result));
				pageNo = 1;
			}else if(mPullRefreshListView.getCurrentMode() == Mode.PULL_FROM_END){
				newsBeanList.addAll(Arrays.asList(result));
				pageNo++;
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
    
    public ArrayList<NewsBean>  parseList(String href, final int pageNo){
		ArrayList<NewsBean> list = new ArrayList<NewsBean>();
		try {
			href = makeURL(href, new HashMap<String, Object>(){{
			    put("PageNo", pageNo);
		    }});
			Log.i("url","url = " + href);
		    
		    //<div data-docid="0EhgxW5n" class="article">
		    //<a style="background-image:url('http://i1.go2yd.com/image.php?url=http://si1.go2yd.com/get-image/07pQgUYRn0q&amp;type=thumbnail_200x140');" 
		    //href="/home?page=article&amp;id=0EhgxW5n&amp;up=3952" target="_blank" class="article-img"></a>
		    //<div class="article-nofloat "><h3><a href="/home?page=article&amp;id=0EhgxW5n&amp;up=3952" target="_blank">
		    //王楠老公力挺宝强怒斥新华记者：谁会往心里插刀？</a></h3>
		    //<p>“徐记者：不知您是代表组织还是个人？谁会愿意朝心里插上一辈子都会痛的狠狠一刀！然后再来个花式包装？谁获得最大收益并极力推</p>
		    //<div class="article-info">
			//<div class="article-opts">
			//<div data-docid="0EhgxW5n" data-datatype="0" class="slide-del"></div>
		    //<div data-docid="0EhgxW5n" data-datatype="0" class="slide-like ">19</div>
		    //<a href="/home?page=article&amp;id=0EhgxW5n&amp;up=3952#comment" target="_blank" class="slide-comment">0</a>
		    //<div data-sharetitle="王楠老公力挺宝强怒斥新华记者：谁会往心里插刀？" data-shareimage="" data-shareurl="http://www.yidianzixun.com/article/0EhgxW5n?s=4" class="slide-share">转发</div>
			//</div>
		    //<span class="article-source">凤凰网</span><span class="article-date">2016-10-19 11:16:00</span>
		    //<div class="clear"></div>
			//</div></div><div class="clear"></div></div>
			
			Document doc = Jsoup.connect(href).userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31").timeout(10000).get(); 
		    Element masthead = doc.select("div.section-articles").first();
		    Elements beanElements =  masthead.select("div.article");	
		    //解析文件
		    for(int i = 0; i < beanElements.size(); i++) {
		    	NewsBean bean = new NewsBean();
			    try {
			    	//图片
				    Element imageElement = beanElements.get(i);
			    	Element astyle = imageElement.select("a").first();
				    String nexthref = UrlUtils.YI_DIAN_ZI_XUN+astyle.attr("href"); 
				    //3imageurl = background-image:url('http://i1.go2yd.com/image.php?url=http://si1.go2yd.com/get-image/07pb1QpTNho&type=thumbnail_200x140');
				    //;nexthref = http://www.yidianzixun.com//home?page=article&id=0Ehnnyy6&up=403
				    Log.i(TAG, i+"nexthref = "+nexthref);
				    bean.setNexthref(nexthref);
				    } catch (Exception e) {
					e.printStackTrace();
				}
			    
			    try {
			    	//图片
				    Element imageElement = beanElements.get(i);
				    Elements aElements =  imageElement.select("a");	
				    ArrayList<String> imgurlList = new ArrayList<String>();
				    for(int y = 0; y < aElements.size(); y++) {
				    	Element astyle =  aElements.get(y);
				    	//5=====0imageurl = 
				    	String imageurl = astyle.attr("style");
				    	if(imageurl!=null && imageurl.length()>=0 && imageurl.contains("background-image")){
				    		Log.i(TAG, i+"====="+y+"imageurl="+imageurl);
						    imgurlList.add(imageurl.replace("background-image:url('", "").replace("');", ""));
				    	}
				    }
				    bean.setImgurlList(imgurlList);
				    /**<a href="/home?page=article&amp;id=0EhdcsUn&amp;up=1514" target="_blank">强台风来袭 三沙岛礁“扶树哥”火了</a></h3>
				     * <div class="article-imgs">
				     * <a style="background-image:url('http://i1.go2yd.com/image.php?url=0EhdcstMtm&amp;type=thumbnail_200x140');" href="/home?page=article&amp;id=0EhdcsUn&amp;up=1514" target="_blank" class="article-img"></a>
				     * <a style="background-image:url('http://i1.go2yd.com/image.php?url=0EhdcsdCQl&amp;type=thumbnail_200x140');" href="/home?page=article&amp;id=0EhdcsUn&amp;up=1514" target="_blank" class="article-img"></a>
				     * <a style="background-image:url('http://i1.go2yd.com/image.php?url=http://si1.go2yd.com/get-image/07pLmw496GG&amp;type=thumbnail_200x140');" href="/home?page=article&amp;id=0EhdcsUn&amp;up=1514" target="_blank" class="article-img"></a>
				     * </div>
				    **/
				    } catch (Exception e) {
					e.printStackTrace();
				}
			    
			    try {
			    	//标题
				    Element titleElement = beanElements.get(i);
			    	Element h3 = titleElement.select("h3 a").first();
			    	String title = h3.text();
				    Log.i(TAG,  i+"title = "+title);
				    bean.setTitle(title);
				} catch (Exception e) {
					e.printStackTrace();
				}
			    
			    try {
			    	//内容
				    Element contentElement = beanElements.get(i);
			    	Element p = contentElement.select("div.article-nofloat p").first();
			    	String content = p.text();
				    Log.i(TAG,  i+"content = "+content);
				    bean.setContent(content);
				} catch (Exception e) {
					e.printStackTrace();
				}
			    
			    try {
			    	//其他
				    Element otherElement = beanElements.get(i);
			    	Element otherE = otherElement.select("div.article-info").first();
			    	String other = otherE.text();
				    Log.i(TAG,  i+"other = "+other);
				    bean.setOther(other);
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
    
    private  String makeURL(String p_url, Map<String, Object> params) {
		StringBuilder url = new StringBuilder(p_url);
		if (url.indexOf("?")<0)
			url.append('?');
		for (String name : params.keySet()) {
			url.append('&');
			url.append(name);
			url.append('=');
			url.append(String.valueOf(params.get(name)));
			//不做URLEncoder处理
			//url.append(URLEncoder.encode(String.valueOf(params.get(name)), UTF_8));
		}
		return url.toString().replace("?&", "?");
	}
}
