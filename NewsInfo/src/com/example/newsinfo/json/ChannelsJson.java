/**
 *****************************************************************************************************************************************************************************
 * 
 * @author :fengguangjing
 * @createTime:2016-11-1上午10:55:00
 * @version:4.2.4
 * @modifyTime:
 * @modifyAuthor:
 * @description:
 *****************************************************************************************************************************************************************************
 */
package com.example.newsinfo.json;

import java.util.List;

import com.example.newsinfo.bean.ChannelsBean;

/**
 ***************************************************************************************************************************************************************************** 
 * 
 * @author :fengguangjing
 * @createTime:2016-11-1上午10:55:00
 * @version:4.2.4
 * @modifyTime:
 * @modifyAuthor:
 * @description:
 ***************************************************************************************************************************************************************************** 
 */
public class ChannelsJson extends StatusJson {
	private List<ChannelsBean> created_channels;
	private List<ChannelsBean> failed_channels;
	private List<ChannelsBean> deleted_channels;

	public List<ChannelsBean> getCreated_channels() {
		return created_channels;
	}

	public void setCreated_channels(List<ChannelsBean> created_channels) {
		this.created_channels = created_channels;
	}

	public List<ChannelsBean> getFailed_channels() {
		return failed_channels;
	}

	public void setFailed_channels(List<ChannelsBean> failed_channels) {
		this.failed_channels = failed_channels;
	}

	public List<ChannelsBean> getDeleted_channels() {
		return deleted_channels;
	}

	public void setDeleted_channels(List<ChannelsBean> deleted_channels) {
		this.deleted_channels = deleted_channels;
	}

}
