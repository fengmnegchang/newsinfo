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

import com.example.andenginetask.AsyncTaskUtils;
import com.example.andenginetask.CallEarliest;
import com.example.andenginetask.Callable;
import com.example.andenginetask.Callback;
import com.example.andenginetask.ProgressCallable;

import android.content.Context;
import android.content.SharedPreferences;
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
	public static final String SHARE_NAME = "NEWS_INFO_PROJECT";
	public static final String IS_FIRST_IN = "is_first_in";
	public SharedPreferences mSharedPreferences;
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(@Nullable Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		mSharedPreferences =  getSharedPreferences(SHARE_NAME, Context.MODE_PRIVATE);
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
	
	
	 /** 
     * 封装的asynctask方法，此方法没有进度框. 
     *  
     * @param pCallEarliest 运行于主线程，最先执行此方法. 
     * @param mCallable 运行于异步线程,第二执行此方法. 
     * @param mCallback 运行于主线程,最后执行此方法. 
     */  
    public <T> void doAsync(final CallEarliest<T> pCallEarliest,  
            final Callable<T> mCallable, final Callback<T> mCallback) {  
        AsyncTaskUtils.doAsync(pCallEarliest, mCallable, mCallback);  
    }  
  
    /** 
     * 封装的asynctask方法，此方法拥有进度对话框，并支持定义样式. 
     * @param pContext  上下文 
     * @param styleID   对话框样式 ProgressDialog.STYLE_HORIZONTAL|ProgressDialog.STYLE_SPINNER 
     * @param pTitle    标题 
     * @param pMessage  内容 
     * @param pCallEarliest  运行于主线程，最先执行此方法. 
     * @param progressCallable 运行于异步线程,用于传递对话框进度. 
     * @param pCallback  运行于主线程,最后执行此方法. 
     */  
    public <T> void doProgressAsync(final Context pContext, final int styleID,  
            final String pTitleResID, final String pMessageResID,  
            final CallEarliest<T> pCallEarliest, final ProgressCallable<T> pCallable,  
            final Callback<T> pCallback) {  
  
        AsyncTaskUtils.doProgressAsync(pContext, styleID, pTitleResID,  
                pMessageResID, pCallEarliest, pCallable, pCallback);  
    }  
      
      
    /** 
     * 封装的asynctask方法，此方法拥有进度对话框，并支持定义样式. 
     * @param pContext  上下文 
     * @param styleID   对话框样式 ProgressDialog.STYLE_HORIZONTAL|ProgressDialog.STYLE_SPINNER 
     * @param pTitle    标题,资源id 
     * @param pMessage  内容,资源id 
     * @param pCallEarliest  运行于主线程，最先执行此方法. 
     * @param progressCallable 运行于异步线程,用于传递对话框进度. 
     * @param pCallback  运行于主线程,最后执行此方法. 
     */  
    public <T> void doProgressAsync(final Context pContext, final int styleID,  
            final int pTitleResID, final int pMessageResID,  
            final CallEarliest<T> pCallEarliest, final ProgressCallable<T> pCallable,  
            final Callback<T> pCallback) {  
  
        AsyncTaskUtils.doProgressAsync(pContext, styleID, pTitleResID,  
                pMessageResID, pCallEarliest, pCallable, pCallback);  
    }  
}
