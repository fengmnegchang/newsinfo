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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.newsinfo.R;
import com.example.newsinfo.bean.NewsBean;
import com.example.newsinfo.imageloader.ImageLoader;

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
public class PinDaoAdapter extends BaseAdapter {
	ArrayList<NewsBean> list;
	Context mContext;
	ImageLoader mImageLoader;
	
	public PinDaoAdapter(Context mContext, ArrayList<NewsBean> list) {
		this.mContext = mContext;
		this.list = list;
		mImageLoader = new ImageLoader(mContext);
		mImageLoader.setRequiredSize(5 * (int) mContext.getResources().getDimension(R.dimen.litpic_width));
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
		View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_pindao, null);
		TextView textview = (TextView) view.findViewById(R.id.textview);
		ImageView image_pindao = (ImageView) view.findViewById(R.id.image_pindao);
		textview.setText(getItem(position).getTitle());
		mImageLoader.DisplayImage(getItem(position).getImage(), image_pindao);
		return view;
	}

}
