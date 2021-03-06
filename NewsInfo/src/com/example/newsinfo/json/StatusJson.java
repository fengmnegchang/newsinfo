/**
 *****************************************************************************************************************************************************************************
 * 
 * @author :fengguangjing
 * @createTime:2016-10-19下午5:33:49
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
 * @createTime:2016-10-19下午5:33:49
 * @version:4.2.4
 * @modifyTime:
 * @modifyAuthor:
 * @description:
 ***************************************************************************************************************************************************************************** 
 */
public class StatusJson {
	/**
	 * { "status":"success", "code":0, "result":[ { "ctype":"news",
	 * "impid":"385806_1476869654777_1774", "pageid":"KW_热点",
	 * "meta":"385806_1476869654777_1774", "dtype":0, "title":"​热点​轮换 创业板整固",
	 * "date":"2016-10-11 17:06:14", "docid":"0Ed43Ay3", "itemid":"0Ed43Ay3",
	 * "summary":
	 * "截至中午收盘，上证综指报3059.55点，上涨11.41点，涨幅为0.37%；深证成指报10767.12点，上涨25.43点，涨幅为0.24%。中小板综指报11843.79点，上涨0.31%；创业指数报2207.34点，下跌0.05%；上证B股指数上涨0.19"
	 * , "b_political":true, "image_urls":[
	 * 
	 * ], "source":"东方财富网", "url":
	 * "http://external3.eastmoney.com/YDNews/ItemView.aspx?C=20161011671546211"
	 * , "mtype":1, "category":"财经", "content_type":"news", "auth":true,
	 * "is_gov":false, "up":313, "comment_count":0 }, { "ctype":"video",
	 * "impid":"385806_1476869654777_1774", "pageid":"KW_热点",
	 * "meta":"385806_1476869654777_1774", "dtype":22,
	 * "title":"财经​热点​：英镑汇价暴跌 英国旅游业成赢家", "date":"2016-10-13 17:06:14",
	 * "docid":"0EeeJLz9", "itemid":"0EeeJLz9",
	 * "summary":"财经热点：英镑汇价暴跌 英国旅游业成赢家 英镑 汇价 旅游业", "image_urls":[ "0EeeJLLkKx"
	 * ], "source":"凤凰视频",
	 * "url":"http://tv.ifeng.com/2793591/ydzx.shtml?ch=sp_ydzx&", "mtype":1,
	 * "image":"0EeeJLLkKx", "content_type":"video", "b_political":false,
	 * "auth":true, "is_gov":false, "up":381, "down":345, "comment_count":0 }, {
	 * "ctype":"video", "impid":"385806_1476869654777_1774", "pageid":"KW_热点",
	 * "meta":"385806_1476869654777_1774", "dtype":22,
	 * "title":"财经​热点​：分析师忧英国央行独立性被削弱", "date":"2016-10-11 16:38:14",
	 * "docid":"0EcQuOp3", "itemid":"0EcQuOp3",
	 * "summary":"财经热点：分析师忧英国央行独立性被削弱 英国 央行 独立性", "b_political":true,
	 * "image_urls":[ "0EcQuO3Cdm" ], "source":"凤凰视频",
	 * "url":"http://tv.ifeng.com/2789264/ydzx.shtml?ch=sp_ydzx&", "mtype":1,
	 * "image":"0EcQuO3Cdm", "content_type":"video", "auth":true,
	 * "is_gov":false, "up":369, "down":335, "comment_count":0 }, {
	 * "ctype":"news", "impid":"385806_1476869654777_1774", "pageid":"KW_热点",
	 * "meta":"385806_1476869654777_1774", "dtype":1,
	 * "title":"​热点​前瞻：PPP寻新​热点 ​关注苹果iPhone7概念股", "date":"2016-09-08 17:06:14",
	 * "docid":"0ENv3Lfe", "itemid":"0ENv3Lfe", "summary":
	 * "在经历周二的大幅反弹之后，沪深两市股指周三再现冲高回落走势，近期最为热门的概念PPP板块延续强势，而苹果IPHONE7发布，A股相关产业链个股也有可能火爆。"
	 * , "b_political":true, "image_urls":[
	 * "http://si1.go2yd.com/get-image/06j8j60ih84" ], "source":"东方财富网",
	 * "url":"http://stock.eastmoney.com/news/1405,20160908662212656.html",
	 * "category":"科技,财经,科技数码", "like":3, "content_type":"news", "auth":true,
	 * "is_gov":false, "up":166, "comment_count":0,
	 * "image":"http://si1.go2yd.com/get-image/06j8j60ih84" }, {
	 * "ctype":"video", "impid":"385806_1476869654777_1774", "pageid":"KW_热点",
	 * "meta":"385806_1476869654777_1774", "dtype":22,
	 * "title":"财经​热点​：墨西哥汇率紧随美国大选辩论波动", "date":"2016-10-12 17:06:14",
	 * "docid":"0EdHCBqi", "itemid":"0EdHCBqi",
	 * "summary":"财经热点：墨西哥汇率紧随美国大选辩论波动 美国大选 墨西哥 比索 汇价", "image_urls":[
	 * "0EdHCBDy6d" ], "source":"凤凰视频",
	 * "url":"http://tv.ifeng.com/2790745/ydzx.shtml?ch=sp_ydzx&", "mtype":1,
	 * "image":"0EdHCBDy6d", "content_type":"video", "b_political":false,
	 * "auth":true, "is_gov":false, "up":392, "down":355, "comment_count":0 }, {
	 * "ctype":"news", "impid":"385806_1476869654777_1774", "pageid":"KW_热点",
	 * "meta":"385806_1476869654777_1774", "dtype":1,
	 * "title":"壹周财经​热点​10.10-10.16最新资讯一手掌握！| TIC原创",
	 * "date":"2016-10-17 17:06:14", "docid":"0EgAY5Q7", "itemid":"0EgAY5Q7",
	 * "summary":
	 * "① 2016年10月10日，短距交通项目“小鸣单车”今日宣布，已于近日完成1亿元A轮系列融资，由国内上市运动自行车企业凯路仕董事长邓永豪领投，部分上市公司股东跟投。"
	 * , "image_urls":[ "0EgAY5HkyA" ], "source":"孟杨访谈录",
	 * "url":"http://www.yidianzixun.com/mp/content?id=9494728",
	 * "category":"科技,财经,互联网", "image":"0EgAY5HkyA", "content_type":"news",
	 * "b_political":false, "auth":true, "is_gov":false }, { "ctype":"news",
	 * "impid":"385806_1476869654777_1774", "pageid":"KW_热点",
	 * "meta":"385806_1476869654777_1774", "dtype":3,
	 * "title":"【社会​热点​】好消息！昆明要申报世界美食之都啦", "date":"2016-10-19 15:25:31",
	 * "docid":"0EhsTXPU", "itemid":"0EhsTXPU", "summary":
	 * "“世界美食之都”来头不小，是联合国世界教科文组织三大旗舰品牌之一。2004年英国在联合国教科文组织执行局第170届大会上，提出建议建立富有开创精神的创意城市网络，在该项目之下又分设“文学之都”“电影之都”“音乐之都”“手工艺与民间艺术之都”“设计之都”“媒体之"
	 * , "b_political":true, "image_urls":[ "0EhsTXuhqh", "0EhsTXYcGb",
	 * "0EhsTXjfaK" ], "source":"云南政协报",
	 * "url":"http://www.yidianzixun.com/mp/content?id=9592583",
	 * "category":"社会", "image":"0EhsTXuhqh", "content_type":"news",
	 * "auth":true, "is_gov":false }, { "ctype":"news",
	 * "impid":"385806_1476869654777_1774", "pageid":"KW_热点",
	 * "meta":"385806_1476869654777_1774", "dtype":0,
	 * "title":"事业单位招聘面试中的文化​热点​剖析", "date":"2016-10-12 16:38:14",
	 * "docid":"0EdoRKOH", "itemid":"0EdoRKOH", "summary":
	 * "中公事业单位招聘考试网为大家带来2016事业单位面试文章《面试中的文化热点剖析》，希望各位考生可以通过本篇文章对事业单位面试有所了解和掌握，在以后的面试中灵活应用，一举成功!"
	 * , "image_urls":[
	 * 
	 * ], "source":"中公网",
	 * "url":"http://www.offcn.com/sydw/2016/1012/242847.html",
	 * "category":"教育,职场", "content_type":"news", "b_political":false,
	 * "auth":true, "is_gov":false, "up":414, "comment_count":0 }, {
	 * "ctype":"news", "impid":"385806_1476869654777_1774", "pageid":"KW_热点",
	 * "meta":"385806_1476869654777_1774", "dtype":0,
	 * "title":"2016年招警考试时政​热点​：10月19日国内外时政​热点​", "date":"2016-10-19 15:41:00",
	 * "docid":"0EhwTKBL", "itemid":"0EhwTKBL", "summary":
	 * "本文是2016年招警考试时政热点的相关内容。华图招警考试频道特为大家整理了2016年每天的国内外时政热点新闻，帮助大家实时掌握社会动态。更多招警考试时政热点，请关注华图教育招警考试频道，保存页面。"
	 * , "b_political":true, "image_urls":[
	 * 
	 * ], "source":"华图教育网",
	 * "url":"http://zhaojing.huatu.com/2016/1019/1523406.html",
	 * "category":"教育", "content_type":"news", "auth":true, "is_gov":false,
	 * "up":234, "comment_count":0 }, { "ctype":"news",
	 * "impid":"385806_1476869654777_1774", "pageid":"KW_热点",
	 * "meta":"385806_1476869654777_1774", "dtype":0, "title":"​热点​缺失，空仓成无耐之举",
	 * "date":"2016-09-22 17:06:14", "docid":"0EUSMMcV", "itemid":"0EUSMMcV",
	 * "summary":
	 * "昨日A股市场延续缩量震荡格局。乍看之下，主要指数均收红，但其实涨幅均不高，热点表现十分温吞，而且与前几日相比又换了面孔，散乱的热点显然很难支撑指数的反弹。长假将至和消息敏感窗口，共同导致市场资金谨慎加重，存量资金流失，人气甚至比节前还要低迷，市场在静静等待“风"
	 * , "image_urls":[
	 * 
	 * ], "source":"全景网", "url":"http://blog.p5w.net/blog-3103950-1178515.html",
	 * "category":"财经", "content_type":"news", "b_political":false, "auth":true,
	 * "is_gov":false, "up":154, "comment_count":0 } ], "fresh_count":0 }
	 */

	private String status;
	private int code;
	private int fresh_count;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
 

	public int getFresh_count() {
		return fresh_count;
	}

	public void setFresh_count(int fresh_count) {
		this.fresh_count = fresh_count;
	}

}
