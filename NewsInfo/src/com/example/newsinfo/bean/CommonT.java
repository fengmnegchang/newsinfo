package com.example.newsinfo.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * T公共类封装
 * 
 * @author think
 * 
 */
public class CommonT implements Serializable {
	/**
	 * newsbean callback封装
	 */
	private ArrayList<NewsBean> newsBeanList;

	public ArrayList<NewsBean> getNewsBeanList() {
		return newsBeanList;
	}

	public void setNewsBeanList(ArrayList<NewsBean> newsBeanList) {
		this.newsBeanList = newsBeanList;
	}

}
