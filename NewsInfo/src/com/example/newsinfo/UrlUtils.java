/**
 *****************************************************************************************************************************************************************************
 * 
 * @author :fengguangjing
 * @createTime:2016-10-19上午11:49:18
 * @version:4.2.4
 * @modifyTime:
 * @modifyAuthor:
 * @description:
 *****************************************************************************************************************************************************************************
 */
package com.example.newsinfo;

import java.util.HashMap;
import java.util.Map;

/**
 *****************************************************************************************************************************************************************************
 * 
 * @author :fengguangjing
 * @createTime:2016-10-19上午11:49:18
 * @version:4.2.4
 * @modifyTime:
 * @modifyAuthor:
 * @description:
 *****************************************************************************************************************************************************************************
 */
public class UrlUtils {
	//地址
	public static final String  YI_DIAN_ZI_XUN= "http://www.yidianzixun.com";
	public static final String  MAN = "http://enrz.com/";
	public static final String  CHANNEL_LIST= "http://www.yidianzixun.com/home?page=channellist";
	public static final String  PROFILE= "http://www.yidianzixun.com/home?page=profile";

	//沉浸式状态栏
	public static final int NONE_STATUS_TAB_ACTIVITY_MARGIN_TOP = 70;
	public static final int STATUS_TAB_ACTIVITY_MARGIN_TOP = 50;
	
	//cookies
	public static final String JSESSIONID = "dc4dcbc5ecf258792d24c12faf0b8bb9d050763e38b8b0d2b43bf2d8cdd1640a";
	public static final String  CNZZDATA1255169715 = "1908767344-1456467599-http%253A%252F%252Fwww.yidianzixun.com%252F%7C1477536295";
	public static final String  captcha = "s%3Af075e21f410e9e2739aebd21d4817b45.QQ%2Bq05pDslaDGkdJFV0uL3ZZQBXtbqf36wYrGsvuQx8";
	public static final String  Hm_lvt_15fafbae2b9b11d280c79eff3b840e45 = "1477272275,1477358473,1477445179,1477531397";
	public static final String  Hm_lpvt_15fafbae2b9b11d280c79eff3b840e45 = "1477538812";
	public static final String  cn_9a154edda337ag57c050_dplus = "%7B%22distinct_id%22%3A%20%221531c6fb95869-0a7506d4a-304a4d7d-1aeaa0-1531c6fb959c4%22%2C%22%E6%9D%A5%E6%BA%90%E6%B8%A0%E9%81%93%22%3A%20%22%22%2C%22%24session_id%22%3A%201456470997%2C%22%24initial_time%22%3A%20%221456467599%22%2C%22%24initial_referrer%22%3A%20%22http%3A%2F%2Fwww.yidianzixun.com%2Fhome%3Fpage%3Dchannel%26keyword%3D%25E4%25BA%2592%25E8%2581%2594%25E7%25BD%2591%22%2C%22%24initial_referring_domain%22%3A%20%22www.yidianzixun.com%22%2C%22%24_sessionid%22%3A%200%2C%22%24_sessionTime%22%3A%201477538873%2C%22%24dp%22%3A%200%2C%22%24_sessionPVTime%22%3A%201477538873%2C%22initial_view_time%22%3A%20%221468565764%22%2C%22initial_referrer%22%3A%20%22http%3A%2F%2Fwww.yidianzixun.com%2Fhome%22%2C%22initial_referrer_domain%22%3A%20%22www.yidianzixun.com%22%2C%22%24recent_outside_referrer%22%3A%20%22%24direct%22%7D";
	public static final String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31";
	
	
	
	/**
	 * 设置 登陆Cookies
	 */
	public static Map<String, String> getCookies(){
		Map<String, String> cookies = new HashMap<String, String>();
		cookies.put("JSESSIONID", JSESSIONID);
		cookies.put("CNZZDATA1255169715", CNZZDATA1255169715);
		cookies.put("captcha", captcha);
		cookies.put("Hm_lvt_15fafbae2b9b11d280c79eff3b840e45", Hm_lvt_15fafbae2b9b11d280c79eff3b840e45);
		cookies.put("Hm_lpvt_15fafbae2b9b11d280c79eff3b840e45", Hm_lpvt_15fafbae2b9b11d280c79eff3b840e45);
		cookies.put("cn_9a154edda337ag57c050_dplus",cn_9a154edda337ag57c050_dplus);
		return cookies;
	}
	
}
