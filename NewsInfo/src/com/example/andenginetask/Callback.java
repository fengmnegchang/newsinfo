/**
 *****************************************************************************************************************************************************************************
 * 
 * @author :fengguangjing
 * @createTime:2016-10-28下午5:26:11
 * @version:4.2.4
 * @modifyTime:
 * @modifyAuthor:
 * @description:
 *****************************************************************************************************************************************************************************
 */
package com.example.andenginetask;

/**
 ***************************************************************************************************************************************************************************** 
 * 回调接口,回调方法运行于主线程
 * 
 * @author :fengguangjing
 * @createTime:2016-10-28下午5:26:11
 * @version:4.2.4
 * @modifyTime:
 * @modifyAuthor:
 * @description:
 ***************************************************************************************************************************************************************************** 
 */
public interface Callback<T> {
	/**
	 * 请求返回 同于AsyncTask的onPostExecute
	 * @param pCallbackValue
	 */
	public void onCallback(final T result);
}
