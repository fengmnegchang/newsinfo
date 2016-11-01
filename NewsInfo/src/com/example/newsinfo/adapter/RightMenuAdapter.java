/**
 *****************************************************************************************************************************************************************************
 * 
 * @author :fengguangjing
 * @createTime:2016-10-18下午3:45:48
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
 * 右侧菜单列表 适配器
 * 
 * @author :fengguangjing
 * @createTime:2016-10-18下午3:45:48
 * @version:4.2.4
 * @modifyTime:
 * @modifyAuthor:
 * @description:
 ***************************************************************************************************************************************************************************** 
 */
public class RightMenuAdapter extends BaseAdapter {
	ArrayList<NewsBean> list;
	Context mContext;
	ImageLoader mImageLoader;
	String mContent;

	public RightMenuAdapter(Context mContext, ArrayList<NewsBean> list,
			String mContent) {
		this.mContext = mContext;
		this.list = list;
		this.mContent = mContent;
		mImageLoader = new ImageLoader(mContext);
		mImageLoader.setRequiredSize(5 * (int) mContext.getResources()
				.getDimension(R.dimen.litpic_width));
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
	public Object getItem(int position) {
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
		NewsBean bean = (NewsBean) getItem(position);
		View view = null;
		if (bean.getType().equals("image")) {
			view = LayoutInflater.from(mContext).inflate(
					R.layout.adapter_item_right_menu_image, null);
			ImageView img_icon = (ImageView) view.findViewById(R.id.img_icon);
			if(bean.getImage()==null ){
				mImageLoader.DisplayImage("http://staticimg.yidianzixun.com/modules/images/home/media_new.png?t=2016092311", img_icon);
			}else{
				mImageLoader.DisplayImage(bean.getImage(), img_icon);
			}
		} else if (bean.getType().equals("hot")) {
			view = LayoutInflater.from(mContext).inflate(
					R.layout.adapter_item_right_menu_hot, null);
			TextView txt_left = (TextView) view.findViewById(R.id.txt_left);
			TextView txt_right = (TextView) view.findViewById(R.id.txt_right);
			if(position%2==0){
				txt_left.setText(bean.getTitle());
				txt_right.setVisibility(View.GONE);
				txt_left.setVisibility(View.VISIBLE);
			}else{
				txt_right.setText(bean.getTitle());
				txt_right.setVisibility(View.VISIBLE);
				txt_left.setVisibility(View.GONE);
			}
		} else if (bean.getType().equals("pindao")) {
			view = LayoutInflater.from(mContext).inflate(
					R.layout.adapter_item_right_menu_pindao, null);
			ImageView img_icon = (ImageView) view.findViewById(R.id.img_icon);
			TextView txt_title = (TextView) view.findViewById(R.id.txt_title);
			TextView txt_other = (TextView) view.findViewById(R.id.txt_other);
			TextView txt_summary = (TextView) view.findViewById(R.id.txt_summary);
			txt_title.setText(bean.getTitle());
			txt_summary.setText(bean.getSummary());
			mImageLoader.DisplayImage(bean.getImage(), img_icon);
		}
		return view;
	}

}
