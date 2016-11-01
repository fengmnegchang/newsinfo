package com.example.newsinfo.activity;


import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.example.newsinfo.R;
import com.example.newsinfo.TabDb;

/**
 * 
 *****************************************************************************************************************************************************************************
 * 主tab页面
 * @author :fengguangjing
 * @createTime:2016-10-28上午10:35:02
 * @version:4.2.4
 * @modifyTime:
 * @modifyAuthor:
 * @description:
 *****************************************************************************************************************************************************************************
 */
public class MainTabActivity extends  TabActivity implements  OnCheckedChangeListener {
	private  TabHost tabHost;
	RadioGroup mRadioGroup;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
 
		setContentView(R.layout.activity_tab_main);
		mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
		mRadioGroup.setOnCheckedChangeListener(this);
		tabHost =  getTabHost();
		tabHost.setup();
		initTab();

	}

	private void initTab() {
		String tabs[] = TabDb.getTabsTxt();
		for (int i = 0; i < tabs.length; i++) {
			TabSpec tabSpec = tabHost.newTabSpec(tabs[i]).setIndicator(getTabView(i));
			Intent intent = new Intent(this,  TabDb.getActivitys()[i]);
			intent.putExtra("TITLE_TAB", tabs[i]);
			tabSpec.setContent(intent).setIndicator(getTabView(i));
			tabHost.addTab(tabSpec);
			tabHost.setTag(i);
		}
	}

	private View getTabView(int idx) {
		View view = LayoutInflater.from(this).inflate(R.layout.footer_tabs, null);
		((TextView) view.findViewById(R.id.tvTab)).setText(TabDb.getTabsTxt()[idx]);
		if (idx == 0) {
			((TextView) view.findViewById(R.id.tvTab)).setTextColor(Color.RED);
			((ImageView) view.findViewById(R.id.ivImg)).setImageResource(TabDb.getTabsImgLight()[idx]);
		} else {
			((ImageView) view.findViewById(R.id.ivImg)).setImageResource(TabDb.getTabsImg()[idx]);
		}
		return view;
	}

	 

	/* (non-Javadoc)
	 * @see android.widget.RadioGroup.OnCheckedChangeListener#onCheckedChanged(android.widget.RadioGroup, int)
	 */
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.radio1:
			tabHost.setCurrentTab(0);
			break;
		case R.id.radio2:
			tabHost.setCurrentTab(1);
			break;
		case R.id.radio3:
			tabHost.setCurrentTab(2);
			break;
		case R.id.radio4:
			tabHost.setCurrentTab(3);
			break;
		case R.id.radio5:
			tabHost.setCurrentTab(4);
			break;
		default:
			break;
		}
		
	}

}