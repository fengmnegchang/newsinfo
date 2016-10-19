/**
 *****************************************************************************************************************************************************************************
 * 
 * @author :fengguangjing
 * @createTime:2016-10-18下午3:46:03
 * @version:4.2.4
 * @modifyTime:
 * @modifyAuthor:
 * @description:
 *****************************************************************************************************************************************************************************
 */
package com.example.newsinfo.bean;

import java.util.ArrayList;

/**
 ***************************************************************************************************************************************************************************** 
 * 
 * @author :fengguangjing
 * @createTime:2016-10-18下午3:46:03
 * @version:4.2.4
 * @modifyTime:
 * @modifyAuthor:
 * @description:
 ***************************************************************************************************************************************************************************** 
 */
public class NewsBean {
	private ArrayList<String> imgurlList = new ArrayList<String>();// 图片地址
	private String nexthref;// 跳转链接
	private String title;
	private String content;
	private String other;

	public ArrayList<String> getImgurlList() {
		return imgurlList;
	}

	public void setImgurlList(ArrayList<String> imgurlList) {
		this.imgurlList = imgurlList;
	}

	public String getNexthref() {
		return nexthref;
	}

	public void setNexthref(String nexthref) {
		this.nexthref = nexthref;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
	}

}
