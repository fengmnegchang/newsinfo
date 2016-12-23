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

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.newsinfo.CommonFragmentActivity;
import com.example.newsinfo.R;
import com.example.newsinfo.UrlUtils;

/**
 ***************************************************************************************************************************************************************************** 
 * 设置页面
 * 
 * @author :fengguangjing
 * @createTime:2016-10-25下午2:47:49
 * @version:4.2.4
 * @modifyTime:
 * @modifyAuthor:
 * @description:
 ***************************************************************************************************************************************************************************** 
 */
public class SettingsActivity extends CommonFragmentActivity {
	private static final String TAG = SettingsActivity.class.getSimpleName();
	private EditText key1, key2, key3, key4, key5, key6, key7, key8;
	private EditText value1, value2, value3, value4, value5, value6, value7, value8;
	private Button btn_settings;
	private ImageView image_settings;
	String channel;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		channel = getIntent().getStringExtra("CHANNEL");
		if (channel == null) {
			setExtendsCommonActivity(false);
			setContentView(R.layout.activity_settings);
			init();
		} else {
			setCommonActivityLeftCanBack(true);
			setCommonActivityCenterEditSearch(false);
			setCommonActivityRightSearch(false);
			addContentView(R.layout.activity_settings, UrlUtils.NONE_STATUS_TAB_ACTIVITY_MARGIN_TOP);
		}
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

		key1 = (EditText) findViewById(R.id.key1);
		key2 = (EditText) findViewById(R.id.key2);
		key3 = (EditText) findViewById(R.id.key3);
		key4 = (EditText) findViewById(R.id.key4);
		key5 = (EditText) findViewById(R.id.key5);
		key6 = (EditText) findViewById(R.id.key6);
		key7 = (EditText) findViewById(R.id.key7);
		key8 = (EditText) findViewById(R.id.key8);

		value1 = (EditText) findViewById(R.id.value1);
		value2 = (EditText) findViewById(R.id.value2);
		value3 = (EditText) findViewById(R.id.value3);
		value4 = (EditText) findViewById(R.id.value4);
		value5 = (EditText) findViewById(R.id.value5);
		value6 = (EditText) findViewById(R.id.value6);
		value7 = (EditText) findViewById(R.id.value7);
		value8 = (EditText) findViewById(R.id.value8);

		btn_settings = (Button) findViewById(R.id.btn_settings);
		image_settings = (ImageView) findViewById(R.id.image_settings);
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
		if (channel == null) {
			// LAUNCHER
			image_settings.setVisibility(View.VISIBLE);
			if (mSharedPreferences.getBoolean(IS_FIRST_IN, true)) {
				initCookies(true);
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						initApp(true);
						mSharedPreferences.edit().putBoolean(IS_FIRST_IN, false).commit();
						startMainTabActivity();
					}
				}, 2000);
			} else {
				initCookies(false);
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						initApp(false);
						startMainTabActivity();
					}
				}, 500);
			}

		} else {
			// 设置
			initCookies(false);
			setRightNone();
			text_title.setText("设置");
			image_settings.setVisibility(View.GONE);
		}
	}

	/**
	 * 
	 */
	protected void initCookies(boolean isfirst) {
		if (isfirst) {
			key1.setText("JSESSIONID");
			value1.setText("dc4dcbc5ecf258792d24c12faf0b8bb9d050763e38b8b0d2b43bf2d8cdd1640a");

			key2.setText("CNZZDATA1255169715");
			value2.setText("1908767344-1456467599-http%253A%252F%252Fwww.yidianzixun.com%252F%7C1482456730");

			key3.setText("captcha");
			value3.setText("s%3A0a44d766e59195253e0b6b4ac351d6da.9A1F81srhUeIc7suROl6bi%2BbQSpym0HtxxpXgQPLgcg");

			key4.setText("Hm_lvt_15fafbae2b9b11d280c79eff3b840e45");
			value4.setText("1482299930,1482370340,1482456144,1482460971");

			key5.setText("Hm_lpvt_15fafbae2b9b11d280c79eff3b840e45");
			value5.setText("1482460987");

			key6.setText("cn_9a154edda337ag57c050_dplus");
			value6.setText("%7B%22distinct_id%22%3A%20%221531c6fb95869-0a7506d4a-304a4d7d-1aeaa0-1531c6fb959c4%22%2C%22%E6%9D%A5%E6%BA%90%E6%B8%A0%E9%81%93%22%3A%20%22%22%2C%22%24session_id%22%3A%201456470997%2C%22%24initial_time%22%3A%20%221456467599%22%2C%22%24initial_referrer%22%3A%20%22http%3A%2F%2Fwww.yidianzixun.com%2Fhome%3Fpage%3Dchannel%26keyword%3D%25E4%25BA%2592%25E8%2581%2594%25E7%25BD%2591%22%2C%22%24initial_referring_domain%22%3A%20%22www.yidianzixun.com%22%2C%22%24_sessionid%22%3A%200%2C%22%24_sessionTime%22%3A%201482461129%2C%22%24dp%22%3A%200%2C%22%24_sessionPVTime%22%3A%201482461129%2C%22initial_view_time%22%3A%20%221468565764%22%2C%22initial_referrer%22%3A%20%22http%3A%2F%2Fwww.yidianzixun.com%2Fhome%22%2C%22initial_referrer_domain%22%3A%20%22www.yidianzixun.com%22%2C%22%24recent_outside_referrer%22%3A%20%22%24direct%22%7D");
			
			key7.setText("userAgent");
			value7.setText("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31");

			key8.setText("Cookie");
			value8.setText("JSESSIONID=dc4dcbc5ecf258792d24c12faf0b8bb9d050763e38b8b0d2b43bf2d8cdd1640a; captcha=s%3A0a44d766e59195253e0b6b4ac351d6da.9A1F81srhUeIc7suROl6bi%2BbQSpym0HtxxpXgQPLgcg; Hm_lvt_15fafbae2b9b11d280c79eff3b840e45=1482299930,1482370340,1482456144,1482460971; Hm_lpvt_15fafbae2b9b11d280c79eff3b840e45=1482460987; CNZZDATA1255169715=1908767344-1456467599-http%253A%252F%252Fwww.yidianzixun.com%252F%7C1482456730; cn_9a154edda337ag57c050_dplus=%7B%22distinct_id%22%3A%20%221531c6fb95869-0a7506d4a-304a4d7d-1aeaa0-1531c6fb959c4%22%2C%22%E6%9D%A5%E6%BA%90%E6%B8%A0%E9%81%93%22%3A%20%22%22%2C%22%24session_id%22%3A%201456470997%2C%22%24initial_time%22%3A%20%221456467599%22%2C%22%24initial_referrer%22%3A%20%22http%3A%2F%2Fwww.yidianzixun.com%2Fhome%3Fpage%3Dchannel%26keyword%3D%25E4%25BA%2592%25E8%2581%2594%25E7%25BD%2591%22%2C%22%24initial_referring_domain%22%3A%20%22www.yidianzixun.com%22%2C%22%24_sessionid%22%3A%200%2C%22%24_sessionTime%22%3A%201482461129%2C%22%24dp%22%3A%200%2C%22%24_sessionPVTime%22%3A%201482461129%2C%22initial_view_time%22%3A%20%221468565764%22%2C%22initial_referrer%22%3A%20%22http%3A%2F%2Fwww.yidianzixun.com%2Fhome%22%2C%22initial_referrer_domain%22%3A%20%22www.yidianzixun.com%22%2C%22%24recent_outside_referrer%22%3A%20%22%24direct%22%7D");
		}else{
			key1.setText(mSharedPreferences.getString("key1", ""));
			value1.setText(mSharedPreferences.getString("value1", ""));

			key2.setText(mSharedPreferences.getString("key2", ""));
			value2.setText(mSharedPreferences.getString("value2", ""));
			
			key3.setText(mSharedPreferences.getString("key3", ""));
			value3.setText(mSharedPreferences.getString("value3", ""));
			
			key4.setText(mSharedPreferences.getString("key4", ""));
			value4.setText(mSharedPreferences.getString("value4", ""));
			
			key5.setText(mSharedPreferences.getString("key5", ""));
			value5.setText(mSharedPreferences.getString("value5", ""));
			
			key6.setText(mSharedPreferences.getString("key6", ""));
			value6.setText(mSharedPreferences.getString("value6", ""));
			
			key7.setText("userAgent");
			value7.setText(mSharedPreferences.getString("userAgent", ""));
			
			key8.setText("Cookie");
			value8.setText(mSharedPreferences.getString("Cookie", ""));
			 
		}
	}

	public void startMainTabActivity() {
		Intent intent = new Intent();
		intent.setClass(SettingsActivity.this, MainTabActivity.class);
		startActivity(intent);
		finish();
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
		btn_settings.setOnClickListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.example.newsinfo.CommonActivity#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_settings:
			initApp(false);
			break;
		default:
			break;
		}
	}

	/**
	 * 
	 */
	public void initApp(boolean isfirst) {
		if (isfirst) {
			// headers
			headers = new HashMap<String, String>();
			headers.put("Cookie", value8.getText().toString());

			mSharedPreferences.edit().putString("Cookie", value8.getText().toString()).commit();
			// Cookies
			cookies = new HashMap<String, String>();
			cookies.put(key1.getText().toString(), value1.getText().toString());
			cookies.put(key2.getText().toString(), value2.getText().toString());
			cookies.put(key3.getText().toString(), value3.getText().toString());
			cookies.put(key4.getText().toString(), value4.getText().toString());
			cookies.put(key5.getText().toString(), value5.getText().toString());
			cookies.put(key6.getText().toString(), value6.getText().toString());

			mSharedPreferences.edit().putString("key1", key1.getText().toString()).commit();
			mSharedPreferences.edit().putString("value1", value1.getText().toString()).commit();

			mSharedPreferences.edit().putString("key2", key2.getText().toString()).commit();
			mSharedPreferences.edit().putString("value2", value2.getText().toString()).commit();

			mSharedPreferences.edit().putString("key3", key3.getText().toString()).commit();
			mSharedPreferences.edit().putString("value3", value3.getText().toString()).commit();

			mSharedPreferences.edit().putString("key4", key4.getText().toString()).commit();
			mSharedPreferences.edit().putString("value4", value4.getText().toString()).commit();

			mSharedPreferences.edit().putString("key5", key5.getText().toString()).commit();
			mSharedPreferences.edit().putString("value5", value5.getText().toString()).commit();

			mSharedPreferences.edit().putString("key6", key6.getText().toString()).commit();
			mSharedPreferences.edit().putString("value6", value6.getText().toString()).commit();

			// cookie
			cookie = value8.getText().toString();
			mSharedPreferences.edit().putString("cookie", cookie).commit();

			// userAgent
			userAgent = value7.getText().toString();
			mSharedPreferences.edit().putString("userAgent", userAgent).commit();

		} else {
			// headers
			headers = new HashMap<String, String>();
			headers.put("Cookie", mSharedPreferences.getString("Cookie", value8.getText().toString()));

			// cookie
			cookie = mSharedPreferences.getString("cookie", value8.getText().toString());

			// userAgent
			userAgent = mSharedPreferences.getString("userAgent", value7.getText().toString());

			// Cookies
			cookies = new HashMap<String, String>();
			cookies.put(mSharedPreferences.getString("key1", key1.getText().toString()), mSharedPreferences.getString("value1", value1.getText().toString()));
			cookies.put(mSharedPreferences.getString("key2", key2.getText().toString()), mSharedPreferences.getString("value2", value2.getText().toString()));
			cookies.put(mSharedPreferences.getString("key3", key3.getText().toString()), mSharedPreferences.getString("value3", value3.getText().toString()));
			cookies.put(mSharedPreferences.getString("key4", key4.getText().toString()), mSharedPreferences.getString("value4", value4.getText().toString()));
			cookies.put(mSharedPreferences.getString("key5", key5.getText().toString()), mSharedPreferences.getString("value5", value5.getText().toString()));
			cookies.put(mSharedPreferences.getString("key6", key6.getText().toString()), mSharedPreferences.getString("value6", value6.getText().toString()));
		}
	}

	// cookies
	// public static final String JSESSIONID =
	// "dc4dcbc5ecf258792d24c12faf0b8bb9d050763e38b8b0d2b43bf2d8cdd1640a";
	// public static final String CNZZDATA1255169715 =
	// "1908767344-1456467599-http%253A%252F%252Fwww.yidianzixun.com%252F%7C1477536295";
	// public static final String captcha =
	// "s%3Af075e21f410e9e2739aebd21d4817b45.QQ%2Bq05pDslaDGkdJFV0uL3ZZQBXtbqf36wYrGsvuQx8";
	// public static final String Hm_lvt_15fafbae2b9b11d280c79eff3b840e45 =
	// "1477272275,1477358473,1477445179,1477531397";
	// public static final String Hm_lpvt_15fafbae2b9b11d280c79eff3b840e45 =
	// "1477538812";
	// public static final String cn_9a154edda337ag57c050_dplus =
	// "%7B%22distinct_id%22%3A%20%221531c6fb95869-0a7506d4a-304a4d7d-1aeaa0-1531c6fb959c4%22%2C%22%E6%9D%A5%E6%BA%90%E6%B8%A0%E9%81%93%22%3A%20%22%22%2C%22%24session_id%22%3A%201456470997%2C%22%24initial_time%22%3A%20%221456467599%22%2C%22%24initial_referrer%22%3A%20%22http%3A%2F%2Fwww.yidianzixun.com%2Fhome%3Fpage%3Dchannel%26keyword%3D%25E4%25BA%2592%25E8%2581%2594%25E7%25BD%2591%22%2C%22%24initial_referring_domain%22%3A%20%22www.yidianzixun.com%22%2C%22%24_sessionid%22%3A%200%2C%22%24_sessionTime%22%3A%201477538873%2C%22%24dp%22%3A%200%2C%22%24_sessionPVTime%22%3A%201477538873%2C%22initial_view_time%22%3A%20%221468565764%22%2C%22initial_referrer%22%3A%20%22http%3A%2F%2Fwww.yidianzixun.com%2Fhome%22%2C%22initial_referrer_domain%22%3A%20%22www.yidianzixun.com%22%2C%22%24recent_outside_referrer%22%3A%20%22%24direct%22%7D";
	// public static final String userAgent =
	// "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31";
	//
	public static String cookie;
	public static String userAgent;
	public static Map<String, String> cookies;
	public static Map<String, String> headers;

	// /**
	// * 设置 登陆Cookies
	// */
	// public static Map<String, String> getCookies(){
	// Map<String, String> cookies = new HashMap<String, String>();
	// cookies.put("JSESSIONID", JSESSIONID);
	// cookies.put("CNZZDATA1255169715", CNZZDATA1255169715);
	// cookies.put("captcha", captcha);
	// cookies.put("Hm_lvt_15fafbae2b9b11d280c79eff3b840e45",
	// Hm_lvt_15fafbae2b9b11d280c79eff3b840e45);
	// cookies.put("Hm_lpvt_15fafbae2b9b11d280c79eff3b840e45",
	// Hm_lpvt_15fafbae2b9b11d280c79eff3b840e45);
	// cookies.put("cn_9a154edda337ag57c050_dplus",cn_9a154edda337ag57c050_dplus);
	// return cookies;
	// }

	/**
	 * 设置 登陆Cookies
	 */
	public static Map<String, String> getCookies() {
		return cookies;
	}

	// /**
	// * JsonObjectRequest 请求设置Headers
	// */
	// public static Map<String,String> getHeaders(){
	// //Cookie:JSESSIONID=dc4dcbc5ecf258792d24c12faf0b8bb9d050763e38b8b0d2b43bf2d8cdd1640a;
	// captcha=s%3Af0091195a659a28b3017dfb7a73fc3b4.7R3%2BfLspP4nKRtUD1k3ApkQ9vdjUK%2FxaFlh22d0Q8V4;
	// Hm_lvt_15fafbae2b9b11d280c79eff3b840e45=1477272275,1477358473,1477445179,1477531397;
	// Hm_lpvt_15fafbae2b9b11d280c79eff3b840e45=1477552058;
	// CNZZDATA1255169715=1908767344-1456467599-http%253A%252F%252Fwww.yidianzixun.com%252F%7C1477547095;
	// cn_9a154edda337ag57c050_dplus=%7B%22distinct_id%22%3A%20%221531c6fb95869-0a7506d4a-304a4d7d-1aeaa0-1531c6fb959c4%22%2C%22%E6%9D%A5%E6%BA%90%E6%B8%A0%E9%81%93%22%3A%20%22%22%2C%22%24session_id%22%3A%201456470997%2C%22%24initial_time%22%3A%20%221456467599%22%2C%22%24initial_referrer%22%3A%20%22http%3A%2F%2Fwww.yidianzixun.com%2Fhome%3Fpage%3Dchannel%26keyword%3D%25E4%25BA%2592%25E8%2581%2594%25E7%25BD%2591%22%2C%22%24initial_referring_domain%22%3A%20%22www.yidianzixun.com%22%2C%22%24_sessionid%22%3A%200%2C%22%24_sessionTime%22%3A%201477553367%2C%22%24dp%22%3A%200%2C%22%24_sessionPVTime%22%3A%201477553367%2C%22initial_view_time%22%3A%20%221468565764%22%2C%22initial_referrer%22%3A%20%22http%3A%2F%2Fwww.yidianzixun.com%2Fhome%22%2C%22initial_referrer_domain%22%3A%20%22www.yidianzixun.com%22%2C%22%24recent_outside_referrer%22%3A%20%22%24direct%22%7D
	// Map<String, String> headers = new HashMap<String, String>();
	// headers.put("Cookie",
	// "JSESSIONID=dc4dcbc5ecf258792d24c12faf0b8bb9d050763e38b8b0d2b43bf2d8cdd1640a; captcha=s%3Af0091195a659a28b3017dfb7a73fc3b4.7R3%2BfLspP4nKRtUD1k3ApkQ9vdjUK%2FxaFlh22d0Q8V4; Hm_lvt_15fafbae2b9b11d280c79eff3b840e45=1477272275,1477358473,1477445179,1477531397; Hm_lpvt_15fafbae2b9b11d280c79eff3b840e45=1477552058; CNZZDATA1255169715=1908767344-1456467599-http%253A%252F%252Fwww.yidianzixun.com%252F%7C1477547095; cn_9a154edda337ag57c050_dplus=%7B%22distinct_id%22%3A%20%221531c6fb95869-0a7506d4a-304a4d7d-1aeaa0-1531c6fb959c4%22%2C%22%E6%9D%A5%E6%BA%90%E6%B8%A0%E9%81%93%22%3A%20%22%22%2C%22%24session_id%22%3A%201456470997%2C%22%24initial_time%22%3A%20%221456467599%22%2C%22%24initial_referrer%22%3A%20%22http%3A%2F%2Fwww.yidianzixun.com%2Fhome%3Fpage%3Dchannel%26keyword%3D%25E4%25BA%2592%25E8%2581%2594%25E7%25BD%2591%22%2C%22%24initial_referring_domain%22%3A%20%22www.yidianzixun.com%22%2C%22%24_sessionid%22%3A%200%2C%22%24_sessionTime%22%3A%201477553367%2C%22%24dp%22%3A%200%2C%22%24_sessionPVTime%22%3A%201477553367%2C%22initial_view_time%22%3A%20%221468565764%22%2C%22initial_referrer%22%3A%20%22http%3A%2F%2Fwww.yidianzixun.com%2Fhome%22%2C%22initial_referrer_domain%22%3A%20%22www.yidianzixun.com%22%2C%22%24recent_outside_referrer%22%3A%20%22%24direct%22%7D");
	// return headers;
	// }

	/**
	 * JsonObjectRequest 请求设置Headers
	 */
	public static Map<String, String> getHeaders() {
		return headers;
	}

	// /**
	// * webview 请求设置Cookies
	// */
	// public static String getWebCookies(){
	// //Cookie:JSESSIONID=dc4dcbc5ecf258792d24c12faf0b8bb9d050763e38b8b0d2b43bf2d8cdd1640a;
	// captcha=s%3Af0091195a659a28b3017dfb7a73fc3b4.7R3%2BfLspP4nKRtUD1k3ApkQ9vdjUK%2FxaFlh22d0Q8V4;
	// Hm_lvt_15fafbae2b9b11d280c79eff3b840e45=1477272275,1477358473,1477445179,1477531397;
	// Hm_lpvt_15fafbae2b9b11d280c79eff3b840e45=1477552058;
	// CNZZDATA1255169715=1908767344-1456467599-http%253A%252F%252Fwww.yidianzixun.com%252F%7C1477547095;
	// cn_9a154edda337ag57c050_dplus=%7B%22distinct_id%22%3A%20%221531c6fb95869-0a7506d4a-304a4d7d-1aeaa0-1531c6fb959c4%22%2C%22%E6%9D%A5%E6%BA%90%E6%B8%A0%E9%81%93%22%3A%20%22%22%2C%22%24session_id%22%3A%201456470997%2C%22%24initial_time%22%3A%20%221456467599%22%2C%22%24initial_referrer%22%3A%20%22http%3A%2F%2Fwww.yidianzixun.com%2Fhome%3Fpage%3Dchannel%26keyword%3D%25E4%25BA%2592%25E8%2581%2594%25E7%25BD%2591%22%2C%22%24initial_referring_domain%22%3A%20%22www.yidianzixun.com%22%2C%22%24_sessionid%22%3A%200%2C%22%24_sessionTime%22%3A%201477553367%2C%22%24dp%22%3A%200%2C%22%24_sessionPVTime%22%3A%201477553367%2C%22initial_view_time%22%3A%20%221468565764%22%2C%22initial_referrer%22%3A%20%22http%3A%2F%2Fwww.yidianzixun.com%2Fhome%22%2C%22initial_referrer_domain%22%3A%20%22www.yidianzixun.com%22%2C%22%24recent_outside_referrer%22%3A%20%22%24direct%22%7D
	// String
	// cookie="JSESSIONID=dc4dcbc5ecf258792d24c12faf0b8bb9d050763e38b8b0d2b43bf2d8cdd1640a; captcha=s%3Af0091195a659a28b3017dfb7a73fc3b4.7R3%2BfLspP4nKRtUD1k3ApkQ9vdjUK%2FxaFlh22d0Q8V4; Hm_lvt_15fafbae2b9b11d280c79eff3b840e45=1477272275,1477358473,1477445179,1477531397; Hm_lpvt_15fafbae2b9b11d280c79eff3b840e45=1477552058; CNZZDATA1255169715=1908767344-1456467599-http%253A%252F%252Fwww.yidianzixun.com%252F%7C1477547095; cn_9a154edda337ag57c050_dplus=%7B%22distinct_id%22%3A%20%221531c6fb95869-0a7506d4a-304a4d7d-1aeaa0-1531c6fb959c4%22%2C%22%E6%9D%A5%E6%BA%90%E6%B8%A0%E9%81%93%22%3A%20%22%22%2C%22%24session_id%22%3A%201456470997%2C%22%24initial_time%22%3A%20%221456467599%22%2C%22%24initial_referrer%22%3A%20%22http%3A%2F%2Fwww.yidianzixun.com%2Fhome%3Fpage%3Dchannel%26keyword%3D%25E4%25BA%2592%25E8%2581%2594%25E7%25BD%2591%22%2C%22%24initial_referring_domain%22%3A%20%22www.yidianzixun.com%22%2C%22%24_sessionid%22%3A%200%2C%22%24_sessionTime%22%3A%201477553367%2C%22%24dp%22%3A%200%2C%22%24_sessionPVTime%22%3A%201477553367%2C%22initial_view_time%22%3A%20%221468565764%22%2C%22initial_referrer%22%3A%20%22http%3A%2F%2Fwww.yidianzixun.com%2Fhome%22%2C%22initial_referrer_domain%22%3A%20%22www.yidianzixun.com%22%2C%22%24recent_outside_referrer%22%3A%20%22%24direct%22%7D";
	// return cookie;
	// }

	/**
	 * webview 请求设置Cookies
	 */
	public static String getWebCookies() {
		return cookie;
	}

}
