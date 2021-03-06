/**
 *****************************************************************************************************************************************************************************
 * 
 * @author :fengguangjing
 * @createTime:2016-10-17下午5:20:10
 * @version:4.2.4
 * @modifyTime:
 * @modifyAuthor:
 * @description:
 *****************************************************************************************************************************************************************************
 */
package com.example.newsinfo.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.example.newsinfo.CommonFragmentActivity;
import com.example.newsinfo.R;
import com.example.newsinfo.UrlUtils;
import com.example.newsinfo.bean.NewsBean;
import com.example.newsinfo.utils.DownLoadAsyncTask;

/**
 ***************************************************************************************************************************************************************************** 
 * 内嵌webview页面
 * @author :fengguangjing
 * @createTime:2016-10-17下午5:20:10
 * @version:4.2.4
 * @modifyTime:
 * @modifyAuthor:
 * @description:
 ***************************************************************************************************************************************************************************** 
 */
public class WebViewActivity extends CommonFragmentActivity {
	private static final String TAG = WebViewActivity.class.getSimpleName();
	private WebView webview;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setCommonActivityLeftCanBack(true);
		setCommonActivityCenterEditSearch(false);
		setCommonActivityRightSearch(false);
		addContentView(R.layout.activity_app_web,UrlUtils.NONE_STATUS_TAB_ACTIVITY_MARGIN_TOP);
		
	}
	
	public static void synCookies(Context context, String url) {  
	    CookieSyncManager.createInstance(context);  
	    CookieManager cookieManager = CookieManager.getInstance();  
	    cookieManager.setAcceptCookie(true);  
	    cookieManager.removeSessionCookie();//移除  
	    cookieManager.setCookie(url, SettingsActivity.getWebCookies());//cookies是在HttpClient中获得的cookie  
	    CookieSyncManager.getInstance().sync();  
	}  
	
	/* (non-Javadoc)
	 * @see com.example.newsinfo.CommonActivity#findView()
	 */
	@Override
	protected void findView() {
		// TODO Auto-generated method stub
		super.findView();
		webview = (WebView) findViewById(R.id.webview);
	}
	
	/* (non-Javadoc)
	 * @see com.example.newsinfo.CommonActivity#initValue()
	 */
	@SuppressLint("NewApi") @Override
	protected void initValue() {
		// TODO Auto-generated method stub
		super.initValue();
		Intent intent = getIntent();
		NewsBean bean = (NewsBean) intent.getSerializableExtra("NEWSBEAN");
		
		
		WebSettings webSettings = webview.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setSupportZoom(true);
		webSettings.setUseWideViewPort(true);
		// 设置出现缩放工具
		webSettings.setBuiltInZoomControls(true);
		// 扩大比例的缩放
		webSettings.setUseWideViewPort(true);
		// 自适应屏幕
		if (Build.VERSION.SDK_INT >= 21) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW );
        }
		webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		webSettings.setLoadWithOverviewMode(true);
		
		webview.setWebViewClient(mWebViewClientBase);
		webview.setWebChromeClient(mWebChromeClientBase);
		webSettings.setUserAgentString(UrlUtils.userAgent);
		
		setRightNone();
		if (bean != null) {
			String title = getIntent().getStringExtra("TITLE");
			((TextView)findViewById(R.id.text_title)).setText(bean.getTitle());
			if("美女".equals(title)){
				//http://www.yidianzixun.com/home?page=article&id=0EizmRvp&up=2515
				if(bean.getUrl()!=null){
					Log.i(TAG, "url===" + bean.getUrl());
					webview.loadUrl(bean.getUrl());
				}else{
					String url = "http://www.yidianzixun.com/home?page=article&id="+bean.getItemid()+"&up="+bean.getUp();
					Log.i(TAG, "url===" + url);
					webview.loadUrl(url);
				}
			}else{
				synCookies(WebViewActivity.this,bean.getUrl());
				Log.i(TAG, "url===" + bean.getUrl());
				webview.loadUrl(bean.getUrl());
			}
		}
		
	}
	
	/* (non-Javadoc)
	 * @see com.example.newsinfo.CommonFragmentActivity#bindEvent()
	 */
	@Override
	protected void bindEvent() {
		// TODO Auto-generated method stub
		super.bindEvent();
		webview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
            	WebView.HitTestResult result = ((WebView) v).getHitTestResult();
            	int type = result.getType();
            	switch (type) {
				case WebView.HitTestResult.IMAGE_TYPE:
					final String imgurl = result.getExtra();
					AlertDialog.Builder builder = new AlertDialog.Builder(WebViewActivity.this);  
		               builder.setItems(new String[]{WebViewActivity.this.getResources().getString(R.string.save_picture)}, new DialogInterface.OnClickListener() {  
		                   @Override  
		                   public void onClick(DialogInterface dialog, int which) {  
		                       new DownLoadAsyncTask(WebViewActivity.this,imgurl).execute();  
		                   }  
		               });  
		               builder.show();  
					break;
				default:
					break;
				}
				return false;
                }
            });
	}

	private WebViewClientBase mWebViewClientBase = new WebViewClientBase();

	private class WebViewClientBase extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			// TODO Auto-generated method stub
			return super.shouldOverrideUrlLoading(view, url);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			// TODO Auto-generated method stub
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			super.onPageFinished(view, url);
		}

		@Override
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			// TODO Auto-generated method stub
			super.onReceivedError(view, errorCode, description, failingUrl);
		}

		@Override
		public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
			// TODO Auto-generated method stub
			super.doUpdateVisitedHistory(view, url, isReload);
		}
	}

	private WebChromeClientBase mWebChromeClientBase = new WebChromeClientBase();

	private class WebChromeClientBase extends WebChromeClient {
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
		}

		@Override
		public void onReceivedTitle(WebView view, String title) {
			// TODO Auto-generated method stub
			super.onReceivedTitle(view, title);
		}

		@Override
		public void onReceivedTouchIconUrl(WebView view, String url, boolean precomposed) {
			// TODO Auto-generated method stub
			super.onReceivedTouchIconUrl(view, url, precomposed);
		}

		@Override
		public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
			// TODO Auto-generated method stub
			return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (webview.canGoBack()) {
			webview.goBack();
		} else {
			super.onBackPressed();
		}
	}
	
	/* (non-Javadoc)
	 * @see com.example.newsinfo.CommonActivity#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back_img:
			onBackPressed();
			break;
		default:
			super.onClick(v);
			break;
		}
	}
	
	public  static void startWebViewActivity(Context context,NewsBean bean){
		Intent intent = new Intent();
		intent.putExtra("NEWSBEAN", bean);
		intent.setClass(context, WebViewActivity.class);
		context.startActivity(intent);
	}

}
