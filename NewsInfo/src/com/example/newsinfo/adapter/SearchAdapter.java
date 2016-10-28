/**
 *****************************************************************************************************************************************************************************
 * 
 * @author :fengguangjing
 * @createTime:2016-10-25下午3:05:29
 * @version:4.2.4
 * @modifyTime:
 * @modifyAuthor:
 * @description:
 *****************************************************************************************************************************************************************************
 */
package com.example.newsinfo.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.newsinfo.R;
import com.example.newsinfo.bean.NewsBean;

/**
 ***************************************************************************************************************************************************************************** 
 * 搜索  适配器
 * @author :fengguangjing
 * @createTime:2016-10-25下午3:05:29
 * @version:4.2.4
 * @modifyTime:
 * @modifyAuthor:
 * @description:
 ***************************************************************************************************************************************************************************** 
 */
public class SearchAdapter extends BaseAdapter {
	ArrayList<NewsBean> list;
	Context mContext;

	public SearchAdapter(Context mContext, ArrayList<NewsBean> list) {
		this.mContext = mContext;
		this.list = list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public NewsBean getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_search, null);
		TextView textview = (TextView) view.findViewById(R.id.textview);
		textview.setText(getItem(position).getTitle());
		return view;
	}

}
