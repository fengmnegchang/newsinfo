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
 * 
 * @author :fengguangjing
 * @createTime:2016-10-18下午3:45:48
 * @version:4.2.4
 * @modifyTime:
 * @modifyAuthor:
 * @description:
 ***************************************************************************************************************************************************************************** 
 */
public class NewsAdapter extends BaseAdapter {
	ArrayList<NewsBean> list;
	Context mContext;
	ImageLoader mImageLoader;
	String mContent;

	public NewsAdapter(Context mContext, ArrayList<NewsBean> list, String mContent) {
		this.mContext = mContext;
		this.list = list;
		this.mContent = mContent;
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
		View view;
		if (mContent.equals("美女")) {
			view = LayoutInflater.from(mContext).inflate(R.layout.item_meinv_news, null);
			ImageView img_icon = (ImageView) view.findViewById(R.id.img_icon);
			TextView txt_other = (TextView) view.findViewById(R.id.txt_other);
			txt_other.setText(bean.getOther());
			if (bean.getImage_urls().size() > 0) {
				if (bean.getImage_urls().get(0).contains("http:") || bean.getImage_urls().get(0).contains("https:")) {
					mImageLoader.DisplayImage(bean.getImage_urls().get(0), img_icon);
				} 
			} else {
				mImageLoader.DisplayImage(bean.getImage(), img_icon);
			}
		} else {
			if (bean.getImage_urls().size() > 1) {
				view = LayoutInflater.from(mContext).inflate(R.layout.item_imgs_news, null);
				ImageView img_icon = (ImageView) view.findViewById(R.id.img_icon);
				ImageView img_icon1 = (ImageView) view.findViewById(R.id.img_icon1);
				ImageView img_icon2 = (ImageView) view.findViewById(R.id.img_icon2);
				TextView txt_title = (TextView) view.findViewById(R.id.txt_title);
				TextView txt_content = (TextView) view.findViewById(R.id.txt_content);
				TextView txt_other = (TextView) view.findViewById(R.id.txt_other);

				txt_title.setText(bean.getTitle());
				txt_content.setText(bean.getSummary());
				txt_other.setText(bean.getOther());
				try {
					// http://i1.go2yd.com/image.php?url=0EguvefACT&type=thumbnail_200x140
					if (bean.getImage_urls().get(0).contains("http:") || bean.getImage_urls().get(0).contains("https:")) {
						mImageLoader.DisplayImage(bean.getImage_urls().get(0), img_icon);
					} else {
						mImageLoader.DisplayImage("http://i1.go2yd.com/image.php?url=" + bean.getImage_urls().get(0) + "&type=thumbnail_200x140", img_icon);
					}

					if (bean.getImage_urls().size() >= 1) {
						if (bean.getImage_urls().get(1).contains("http:") || bean.getImage_urls().get(1).contains("https:")) {
							mImageLoader.DisplayImage(bean.getImage_urls().get(1), img_icon1);
						} else {
							mImageLoader.DisplayImage("http://i1.go2yd.com/image.php?url=" + bean.getImage_urls().get(1) + "&type=thumbnail_200x140", img_icon1);
						}
						// mImageLoader.DisplayImage(bean.getImage_urls().get(1),
						// img_icon1);
					} else {
						img_icon1.setVisibility(View.INVISIBLE);
					}
					if (bean.getImage_urls().size() >= 2) {
						if (bean.getImage_urls().get(2).contains("http:") || bean.getImage_urls().get(2).contains("https:")) {
							mImageLoader.DisplayImage(bean.getImage_urls().get(2), img_icon2);
						} else {
							mImageLoader.DisplayImage("http://i1.go2yd.com/image.php?url=" + bean.getImage_urls().get(2) + "&type=thumbnail_200x140", img_icon2);
						}
						// mImageLoader.DisplayImage(bean.getImage_urls().get(2),
						// img_icon2);
					} else {
						img_icon2.setVisibility(View.INVISIBLE);
						img_icon1.setVisibility(View.INVISIBLE);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			} else {
				view = LayoutInflater.from(mContext).inflate(R.layout.item_news, null);
				ImageView img_icon = (ImageView) view.findViewById(R.id.img_icon);
				TextView txt_title = (TextView) view.findViewById(R.id.txt_title);
				TextView txt_content = (TextView) view.findViewById(R.id.txt_content);
				TextView txt_other = (TextView) view.findViewById(R.id.txt_other);

				txt_title.setText(bean.getTitle());
				txt_content.setText(bean.getSummary());
				txt_other.setText(bean.getOther());

				if (bean.getImage_urls().size() > 0) {
					if (bean.getImage_urls().get(0).contains("http:") || bean.getImage_urls().get(0).contains("https:")) {
						mImageLoader.DisplayImage(bean.getImage_urls().get(0), img_icon);
					} else {
						mImageLoader.DisplayImage("http://i1.go2yd.com/image.php?url=" + bean.getImage_urls().get(0) + "&type=thumbnail_200x140", img_icon);
					}
					// mImageLoader.DisplayImage(bean.getImage_urls().get(0),
					// img_icon);
				} else {
					img_icon.setVisibility(View.GONE);
				}
			}
		}
		return view;
	}

}
