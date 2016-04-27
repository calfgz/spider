package com.spider.wechat;

public class WechatConfig {
	//搜狐搜索微信号URL
	public static String WECHAT_SOGOU_URL_TEMPLATE = "http://weixin.sogou.com/weixin?type=1&query=%s&ie=utf8&_sug_=n&_sug_type_=";
	//Request标示处理类型
	public static String WECHAT_TYPE = "__WECHAT_TYPE__";
	
	//搜狗搜索微信号
	public static int WECHAT_SEARCH_SOGOU = 1;
	//微信号文章列表页
	public static int WECHAT_ARTICLE_LIST = 2;
	//微信号文章终端页
	public static int WECHAT_ARTICLE_DETAIL = 3;
}
