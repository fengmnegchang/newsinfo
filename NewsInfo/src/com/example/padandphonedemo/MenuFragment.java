/**
 *****************************************************************************************************************************************************************************
 * 
 * @author :fengguangjing
 * @createTime:2016-10-21下午2:45:45
 * @version:4.2.4
 * @modifyTime:
 * @modifyAuthor:
 * @description:
 *****************************************************************************************************************************************************************************
 */
package com.example.padandphonedemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.newsinfo.R;
import com.example.newsinfo.fragment.HomeFragment;
import com.example.newsinfo.fragment.NewsFragment;

/**
 ***************************************************************************************************************************************************************************** 
 * 
 * @author :fengguangjing
 * @createTime:2016-10-21下午2:45:45
 * @version:4.2.4
 * @modifyTime:
 * @modifyAuthor:
 * @description:
 ***************************************************************************************************************************************************************************** 
 */
public class MenuFragment extends Fragment implements OnItemClickListener {

	/**
	 * 菜单界面中只包含了一个ListView。
	 */
	private ListView menuList;

	/**
	 * ListView的适配器。
	 */
	private ArrayAdapter<String> adapter;

	/**
	 * 用于填充ListView的数据，这里就简单只用了两条数据。
	 */
	private String[] menuItems = { "首页", "热点", "社会", "股票", "美女", "漫画", "搞笑", "科技", "互联网", "财经", "军事", "体育", "趣图", "汽车", "健康", "时尚", "科学" };
	public static final String[] CONTENT = new String[] { "首页", "热点", "社会", "股票", "美女", "漫画",
   	 "搞笑", "科技", "互联网", "财经", "军事", "体育"
   	 ,"趣图","汽车","健康","时尚","科学"};
	
	public static final String[] URL = new String[] {
   	"http://www.yidianzixun.com/home",
   	"http://www.yidianzixun.com/home?page=channel&id=hot", 
   	"http://www.yidianzixun.com/home?page=channel&keyword=%E7%A4%BE%E4%BC%9A",
   	"http://www.yidianzixun.com/home?page=channel&keyword=%E8%82%A1%E7%A5%A8", 
   	"http://www.yidianzixun.com/home?page=channel&keyword=%E7%BE%8E%E5%A5%B3", 
   	"http://www.yidianzixun.com/home?page=channel&keyword=%E6%BC%AB%E7%94%BB",
  	    "http://www.yidianzixun.com/home?page=channel&keyword=%E6%90%9E%E7%AC%91", 
  	    "http://www.yidianzixun.com/home?page=channel&keyword=%E7%A7%91%E6%8A%80", 
  	    "http://www.yidianzixun.com/home?page=channel&keyword=%E4%BA%92%E8%81%94%E7%BD%91", 
  	    "http://www.yidianzixun.com/home?page=channel&keyword=%E8%B4%A2%E7%BB%8F", 
  	    "http://www.yidianzixun.com/home?page=channel&keyword=%E5%86%9B%E4%BA%8B", 
  	    "http://www.yidianzixun.com/home?page=channel&keyword=%E4%BD%93%E8%82%B2",
  	    
  	    "http://www.yidianzixun.com/home?page=channel&keyword=%E8%B6%A3%E5%9B%BE",
  	    "http://www.yidianzixun.com/home?page=channel&keyword=%E6%B1%BD%E8%BD%A6",
  	    "http://www.yidianzixun.com/home?page=channel&keyword=%E5%81%A5%E5%BA%B7",
  	    "http://www.yidianzixun.com/home?page=channel&keyword=%E6%97%B6%E5%B0%9A",
  	    "http://www.yidianzixun.com/home?page=channel&keyword=%E7%A7%91%E5%AD%A6"
   };
   
	public static final String[] JSON_DATA_URL = new String[] {
   	"http://www.yidianzixun.com/api/q/?path=channel|news-list-for-keyword&display=%E7%83%AD%E7%82%B9&word_type=token&fields=docid&fields=category&fields=date&fields=image&fields=image_urls&fields=like&fields=source&fields=title&fields=url&fields=comment_count&fields=summary&fields=up&version=999999&infinite=true",
   	"http://www.yidianzixun.com/api/q/?path=channel|news-list-for-keyword&display=%E7%83%AD%E7%82%B9&word_type=token&fields=docid&fields=category&fields=date&fields=image&fields=image_urls&fields=like&fields=source&fields=title&fields=url&fields=comment_count&fields=summary&fields=up&version=999999&infinite=true", 
   	"http://www.yidianzixun.com/api/q/?path=channel|news-list-for-keyword&display=%E7%A4%BE%E4%BC%9A&word_type=token&fields=docid&fields=category&fields=date&fields=image&fields=image_urls&fields=like&fields=source&fields=title&fields=url&fields=comment_count&fields=summary&fields=up&version=999999&infinite=true",
   	"http://www.yidianzixun.com/api/q/?path=channel|news-list-for-keyword&display=%E8%82%A1%E7%A5%A8&word_type=token&fields=docid&fields=category&fields=date&fields=image&fields=image_urls&fields=like&fields=source&fields=title&fields=url&fields=comment_count&fields=summary&fields=up&version=999999&infinite=true",
   	"http://www.yidianzixun.com/api/q/?path=channel|news-list-for-channel&channel_id=u241&fields=docid&fields=category&fields=date&fields=image&fields=image_urls&fields=like&fields=source&fields=title&fields=url&fields=comment_count&fields=summary&fields=up&version=999999&infinite=true",
   	"http://www.yidianzixun.com/api/q/?path=channel|news-list-for-keyword&display=%E6%BC%AB%E7%94%BB&word_type=token&fields=docid&fields=category&fields=date&fields=image&fields=image_urls&fields=like&fields=source&fields=title&fields=url&fields=comment_count&fields=summary&fields=up&version=999999&infinite=true",
   	"http://www.yidianzixun.com/api/q/?path=channel|news-list-for-keyword&display=%E6%90%9E%E7%AC%91&word_type=token&fields=docid&fields=category&fields=date&fields=image&fields=image_urls&fields=like&fields=source&fields=title&fields=url&fields=comment_count&fields=summary&fields=up&version=999999&infinite=true",
   	"http://www.yidianzixun.com/api/q/?path=channel|news-list-for-keyword&display=%E7%A7%91%E6%8A%80&word_type=token&fields=docid&fields=category&fields=date&fields=image&fields=image_urls&fields=like&fields=source&fields=title&fields=url&fields=comment_count&fields=summary&fields=up&version=999999&infinite=true",
   	"http://www.yidianzixun.com/api/q/?path=channel|news-list-for-keyword&display=%E4%BA%92%E8%81%94%E7%BD%91&word_type=token&fields=docid&fields=category&fields=date&fields=image&fields=image_urls&fields=like&fields=source&fields=title&fields=url&fields=comment_count&fields=summary&fields=up&version=999999&infinite=true",
   	"http://www.yidianzixun.com/api/q/?path=channel|news-list-for-keyword&display=%E8%B4%A2%E7%BB%8F&word_type=token&fields=docid&fields=category&fields=date&fields=image&fields=image_urls&fields=like&fields=source&fields=title&fields=url&fields=comment_count&fields=summary&fields=up&version=999999&infinite=true",
   	"http://www.yidianzixun.com/api/q/?path=channel|news-list-for-keyword&display=%E5%86%9B%E4%BA%8B&word_type=token&fields=docid&fields=category&fields=date&fields=image&fields=image_urls&fields=like&fields=source&fields=title&fields=url&fields=comment_count&fields=summary&fields=up&version=999999&infinite=true",
   	"http://www.yidianzixun.com/api/q/?path=channel|news-list-for-keyword&display=%E4%BD%93%E8%82%B2&word_type=token&fields=docid&fields=category&fields=date&fields=image&fields=image_urls&fields=like&fields=source&fields=title&fields=url&fields=comment_count&fields=summary&fields=up&version=999999&infinite=true",
   	
   	"http://www.yidianzixun.com/api/q/?path=channel|news-list-for-keyword&display=%E8%B6%A3%E5%9B%BE&word_type=token&fields=docid&fields=category&fields=date&fields=image&fields=image_urls&fields=like&fields=source&fields=title&fields=url&fields=comment_count&fields=summary&fields=up&version=999999&infinite=true",
   	"http://www.yidianzixun.com/api/q/?path=channel|news-list-for-keyword&display=%E6%B1%BD%E8%BD%A6&word_type=token&fields=docid&fields=category&fields=date&fields=image&fields=image_urls&fields=like&fields=source&fields=title&fields=url&fields=comment_count&fields=summary&fields=up&version=999999&infinite=true",
   	"http://www.yidianzixun.com/api/q/?path=channel|news-list-for-keyword&display=%E5%81%A5%E5%BA%B7&word_type=token&fields=docid&fields=category&fields=date&fields=image&fields=image_urls&fields=like&fields=source&fields=title&fields=url&fields=comment_count&fields=summary&fields=up&version=999999&infinite=true",
   	"http://www.yidianzixun.com/api/q/?path=channel|news-list-for-keyword&display=%E6%97%B6%E5%B0%9A&word_type=token&fields=docid&fields=category&fields=date&fields=image&fields=image_urls&fields=like&fields=source&fields=title&fields=url&fields=comment_count&fields=summary&fields=up&version=999999&infinite=true",
   	"http://www.yidianzixun.com/api/q/?path=channel|news-list-for-keyword&display=%E7%A7%91%E5%AD%A6&word_type=token&fields=docid&fields=category&fields=date&fields=image&fields=image_urls&fields=like&fields=source&fields=title&fields=url&fields=comment_count&fields=summary&fields=up&version=999999&infinite=true"
   	
   	};
	/**
	 * 是否是双页模式。如果一个Activity中包含了两个Fragment，就是双页模式。
	 */
	private boolean isTwoPane;

	/**
	 * 当Activity和Fragment建立关联时，初始化适配器中的数据。
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, menuItems);
	}

	/**
	 * 加载menu_fragment布局文件，为ListView绑定了适配器，并设置了监听事件。
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.menu_fragment, container, false);
		menuList = (ListView) view.findViewById(R.id.menu_list);
		menuList.setAdapter(adapter);
		menuList.setOnItemClickListener(this);
		return view;
	}

	/**
	 * 当Activity创建完毕后，尝试获取一下布局文件中是否有details_layout这个元素，如果有说明当前
	 * 是双页模式，如果没有说明当前是单页模式。
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (getActivity().findViewById(R.id.details_layout) != null) {
			isTwoPane = true;
			onItemClick(menuList,null,0,0);
		} else {
			isTwoPane = false;
		}
	}

	/**
	 * 处理ListView的点击事件，会根据当前是否是双页模式进行判断。如果是双页模式，则会动态添加Fragment。
	 * 如果不是双页模式，则会打开新的Activity。
	 */
	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int index, long arg3) {
		if (isTwoPane) {
			if(index==0){
				Fragment fragment = HomeFragment.newInstance(menuItems[index],URL[index],JSON_DATA_URL[index]);
				fragment.setUserVisibleHint(true);
				getFragmentManager().beginTransaction().replace(R.id.details_layout, fragment).commit();
			}else{
				Fragment fragment = NewsFragment.newInstance(menuItems[index],URL[index],JSON_DATA_URL[index]);
				fragment.setUserVisibleHint(true);
				getFragmentManager().beginTransaction().replace(R.id.details_layout, fragment).commit();
			}
		} else {
			Intent intent = null;
			intent = new Intent(getActivity(), NewsActivity.class);
			intent.putExtra("TITLE", menuItems[index]);
			intent.putExtra("URL", URL[index]);
			intent.putExtra("JSON_DATA_URL", JSON_DATA_URL[index]);
			startActivity(intent);
		}
	}

}