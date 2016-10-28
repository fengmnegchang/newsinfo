/**
 *****************************************************************************************************************************************************************************
 * 
 * @author :fengguangjing
 * @createTime:2016-10-19下午5:33:32
 * @version:4.2.4
 * @modifyTime:
 * @modifyAuthor:
 * @description:
 *****************************************************************************************************************************************************************************
 */
package com.example.newsinfo.json;

import java.util.ArrayList;

import com.example.newsinfo.bean.NewsBean;

/**
 ***************************************************************************************************************************************************************************** 
 * 
 * @author :fengguangjing
 * @createTime:2016-10-19下午5:33:32
 * @version:4.2.4
 * @modifyTime:
 * @modifyAuthor:
 * @description:
 ***************************************************************************************************************************************************************************** 
 */
public class NewsBeanJson extends StatusJson {
	private ArrayList<NewsBean> result;
	private ArrayList<NewsBean> channels;

	public ArrayList<NewsBean> getResult() {
		return result;
	}

	public void setResult(ArrayList<NewsBean> result) {
		this.result = result;
	}

	public ArrayList<NewsBean> getChannels() {
		return channels;
	}

	public void setChannels(ArrayList<NewsBean> channels) {
		this.channels = channels;
	}

}
