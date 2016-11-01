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

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.newsinfo.R;
import com.example.newsinfo.activity.SettingsActivity;
import com.example.newsinfo.bean.NewsBean;
import com.example.newsinfo.imageloader.ImageLoader;
import com.example.newsinfo.json.ChannelsJson;
import com.google.gson.Gson;

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

	public RightMenuAdapter(Context mContext, ArrayList<NewsBean> list, String mContent) {
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
		final NewsBean bean = (NewsBean) getItem(position);
		View view = null;
		if (bean.getType().equals("image")) {
			view = LayoutInflater.from(mContext).inflate(R.layout.adapter_item_right_menu_image, null);
			ImageView img_icon = (ImageView) view.findViewById(R.id.img_icon);
			if (bean.getImage() == null) {
				mImageLoader.DisplayImage("http://staticimg.yidianzixun.com/modules/images/home/media_new.png?t=2016092311", img_icon);
			} else {
				mImageLoader.DisplayImage(bean.getImage(), img_icon);
			}
		} else if (bean.getType().equals("hot")) {
			view = LayoutInflater.from(mContext).inflate(R.layout.adapter_item_right_menu_hot, null);
			TextView txt_left = (TextView) view.findViewById(R.id.txt_left);
			TextView txt_right = (TextView) view.findViewById(R.id.txt_right);
			if (position % 2 == 0) {
				txt_left.setText(bean.getTitle());
				txt_right.setVisibility(View.GONE);
				txt_left.setVisibility(View.VISIBLE);
			} else {
				txt_right.setText(bean.getTitle());
				txt_right.setVisibility(View.VISIBLE);
				txt_left.setVisibility(View.GONE);
			}
		} else if (bean.getType().equals("pindao")) {
			view = LayoutInflater.from(mContext).inflate(R.layout.adapter_item_right_menu_pindao, null);
			ImageView img_icon = (ImageView) view.findViewById(R.id.img_icon);
			TextView txt_title = (TextView) view.findViewById(R.id.txt_title);
			TextView txt_other = (TextView) view.findViewById(R.id.txt_other);
			TextView txt_summary = (TextView) view.findViewById(R.id.txt_summary);
			txt_title.setText(bean.getTitle());
			txt_summary.setText(bean.getSummary());
			txt_other.setText(bean.getOther());
			txt_other.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if ("订阅".equals(bean.getOther())) {
						// 订阅
						RequestQueue requestQueue = Volley.newRequestQueue(mContext);
						JSONObject jsonRequest = new JSONObject();
						try {
							jsonRequest.put("path", "channel|create");
							jsonRequest.put("data", "{\"created_channels\": [{\"name\": \""+bean.getTitle()+"\"}]}");
						} catch (JSONException e) {
							e.printStackTrace();
						}
						
						JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "http://www.yidianzixun.com/api/q/", SettingsActivity.getHeaders(), jsonRequest,
								new Response.Listener<JSONObject>() {
									@Override
									public void onResponse(JSONObject response) {
										/**
										 * {
									    "status":"success",
									    "code":0,
									    "created_channels":[
									        {
									            "name":"星座",
									            "channel_id":5127728789,
									            "type":"category",
									            "fromId":"t77",
									            "category":null,
									            "image":"http://s.go2yd.com/b/idbbafdm_7i00d1d1.jpg",
									            "share_id":"t77"
									        }
									    ],
									    "failed_channels":[
									
									    ]
									}
									 */
										Gson gson = new Gson();
										ChannelsJson mChannelsJson = gson.fromJson(response.toString(), ChannelsJson.class);
										if(mChannelsJson.getCreated_channels()!=null && mChannelsJson.getCreated_channels().size()>0){
											bean.setId(mChannelsJson.getCreated_channels().get(0).getChannel_id());
											bean.setImage(mChannelsJson.getCreated_channels().get(0).getImage());
										}
										System.out.println(response);
									}
								}, new Response.ErrorListener() {
									@Override
									public void onErrorResponse(VolleyError error) {
										System.out.println(error);
									}
								});
						requestQueue.add(jsonObjectRequest);
					} else if ("已订".equals(bean.getOther())) {
						// 取消订阅
						RequestQueue requestQueue = Volley.newRequestQueue(mContext);
						JSONObject jsonRequest = new JSONObject();
						try {
							jsonRequest.put("path", "channel|delete");
							jsonRequest.put("data", "{\"deleted_channels\": [\""+bean.getId()+"\"]}");
						} catch (JSONException e) {
							e.printStackTrace();
						}
						
						JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "http://www.yidianzixun.com/api/q/", SettingsActivity.getHeaders(), jsonRequest,
								new Response.Listener<JSONObject>() {
									@Override
									public void onResponse(JSONObject response) {
										Gson gson = new Gson();
										ChannelsJson mChannelsJson = gson.fromJson(response.toString(), ChannelsJson.class);
										if(mChannelsJson.getDeleted_channels()!=null && mChannelsJson.getDeleted_channels().size()>0){
											bean.setId(mChannelsJson.getDeleted_channels().get(0).getChannel_id());
											bean.setImage(mChannelsJson.getDeleted_channels().get(0).getImage());
										}
										System.out.println(response);
									}
								}, new Response.ErrorListener() {
									@Override
									public void onErrorResponse(VolleyError error) {
										System.out.println(error);
									}
								});
						requestQueue.add(jsonObjectRequest);
					}
				}
			});
			mImageLoader.DisplayImage(bean.getImage(), img_icon);
		}
		return view;
	}

}
