/**
 *****************************************************************************************************************************************************************************
 * 
 * @author :fengguangjing
 * @createTime:2016-10-26上午10:09:57
 * @version:4.2.4
 * @modifyTime:
 * @modifyAuthor:
 * @description:
 *****************************************************************************************************************************************************************************
 */
package com.example.newsinfo;

import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.newsinfo.activity.SearchActivity;

/**
 ***************************************************************************************************************************************************************************** 
 * 
 * @author :fengguangjing
 * @createTime:2016-10-26上午10:09:57
 * @version:4.2.4
 * @modifyTime:
 * @modifyAuthor:
 * @description:
 ***************************************************************************************************************************************************************************** 
 */
public class CommonActivity extends BaseActivity {
	protected ImageView yidian_img;// 左边logo
	protected EditText edit_search;// 搜索框
	protected ImageView owner_logo;// 我logo
	protected ImageView search_btn;// 右边搜索功能
	protected ImageView back_img;// 左边返回
	protected TextView text_title;// 中间
	protected View lay_content;// 内容

	/**
	 * 是否继承CommonActivity 默认true
	 */
	protected boolean isExtendsCommonActivity = true;
	/**
	 * 左边返回按钮;与左边一点资讯重叠 默认true；可以返回
	 */
	protected boolean isCommonActivityLeftCanBack = true;

	/**
	 * 中间搜索按钮;与中间title重叠 默认false；显示title
	 */
	protected boolean isCommonActivityCenterEditSearch = false;

	/**
	 * 右边搜索按钮;与右边我重叠 默认false；显示wo
	 */
	protected boolean isCommonActivityRightSearch = false;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(@Nullable Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);

		if (isExtendsCommonActivity) {
			setContentView(R.layout.activity_common);
		}
	}

	/**
	 * 添加view
	 */
	public void addContentView(int layoutResID) {
		if (isExtendsCommonActivity) {
			LayoutInflater mLayoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			lay_content = mLayoutInflater.inflate(layoutResID, null);
			FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
			lp.gravity = Gravity.TOP | Gravity.LEFT;
			lp.topMargin = (int) ScreenUtils.getIntToDip(this, 55);
			addContentView(lay_content, lp);
			init();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.example.newsinfo.BaseActivity#findView()
	 */
	@Override
	protected void findView() {
		// TODO Auto-generated method stub
		super.findView();
		if (isExtendsCommonActivity) {
			yidian_img = (ImageView) findViewById(R.id.yidian_img);
			edit_search = (EditText) findViewById(R.id.edit_search);
			owner_logo = (ImageView) findViewById(R.id.owner_logo);
			search_btn = (ImageView) findViewById(R.id.search_btn);
			back_img = (ImageView) findViewById(R.id.back_img);
			text_title = (TextView) findViewById(R.id.text_title);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.example.newsinfo.BaseActivity#initValue()
	 */
	@Override
	protected void initValue() {
		// TODO Auto-generated method stub
		super.initValue();
		if (isExtendsCommonActivity) {
			// back
			if (isCommonActivityLeftCanBack) {
				back_img.setVisibility(View.VISIBLE);
				yidian_img.setVisibility(View.GONE);
			} else {
				back_img.setVisibility(View.GONE);
				yidian_img.setVisibility(View.VISIBLE);
			}

			// title
			if (isCommonActivityCenterEditSearch) {
				edit_search.setVisibility(View.VISIBLE);
				text_title.setVisibility(View.GONE);
			} else {
				edit_search.setVisibility(View.GONE);
				text_title.setVisibility(View.VISIBLE);
			}

			// wo
			if (isCommonActivityRightSearch) {
				search_btn.setVisibility(View.VISIBLE);
				owner_logo.setVisibility(View.GONE);
			} else {
				search_btn.setVisibility(View.GONE);
				owner_logo.setVisibility(View.VISIBLE);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.example.newsinfo.BaseActivity#bindEvent()
	 */
	@Override
	protected void bindEvent() {
		// TODO Auto-generated method stub
		super.bindEvent();
		if (isExtendsCommonActivity) {
			edit_search.setOnClickListener(this);
			owner_logo.setOnClickListener(this);
			search_btn.setOnClickListener(this);
			back_img.setOnClickListener(this);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.example.newsinfo.BaseActivity#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.edit_search:// 搜索框
			Intent intent = new Intent();
			intent.setClass(CommonActivity.this, SearchActivity.class);
			startActivity(intent);
			break;
		case R.id.owner_logo:// 我
			break;
		case R.id.search_btn:// 右边搜索
			break;
		case R.id.back_img:// 左边返回
			finish();
			break;
		default:
			break;
		}
	}

	public boolean isExtendsCommonActivity() {
		return isExtendsCommonActivity;
	}

	public void setExtendsCommonActivity(boolean isExtendsCommonActivity) {
		this.isExtendsCommonActivity = isExtendsCommonActivity;
	}

	public boolean isCommonActivityLeftCanBack() {
		return isCommonActivityLeftCanBack;
	}

	public void setCommonActivityLeftCanBack(boolean isCommonActivityLeftCanBack) {
		this.isCommonActivityLeftCanBack = isCommonActivityLeftCanBack;
	}

	public boolean isCommonActivityCenterEditSearch() {
		return isCommonActivityCenterEditSearch;
	}

	public void setCommonActivityCenterEditSearch(boolean isCommonActivityCenterEditSearch) {
		this.isCommonActivityCenterEditSearch = isCommonActivityCenterEditSearch;
	}

	public boolean isCommonActivityRightSearch() {
		return isCommonActivityRightSearch;
	}

	public void setCommonActivityRightSearch(boolean isCommonActivityRightSearch) {
		this.isCommonActivityRightSearch = isCommonActivityRightSearch;
	}

	public String makeURL(String p_url, Map<String, Object> params) {
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

	/***
	 * 隐藏右边按钮
	 */
	public void setRightNone() {
		if (isExtendsCommonActivity) {
			owner_logo.setVisibility(View.GONE);
			search_btn.setVisibility(View.GONE);
		}
	}
	
	/***
	 * 隐藏左边按钮
	 */
	public void setLeftNone() {
		if (isExtendsCommonActivity) {
			back_img.setVisibility(View.GONE);
			yidian_img.setVisibility(View.GONE);
		}
	}

}
