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

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;

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
public class BaseFragmentActivity extends FragmentActivity implements OnClickListener {

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(@Nullable Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		
	}

	protected void init() {
		try {
			findView();
			initValue();
			bindEvent();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/***
	 * 取出控件
	 */
	protected void findView() {

	}

	/**
	 * 实例化控件
	 */
	protected void initValue() {

	}

	/**
	 * 绑定事件
	 */
	protected void bindEvent() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

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
}
