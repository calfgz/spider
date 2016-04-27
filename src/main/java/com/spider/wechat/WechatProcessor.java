package com.spider.wechat;

import java.util.Date;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.spider.model.Article;
import com.spider.model.Author;
import com.spider.service.ArticleService;
import com.spider.service.AuthorService;
import com.spider.util.T;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

@Component
public class WechatProcessor implements PageProcessor {
	
	@Autowired
	ArticleService articleService;
	
	@Autowired
	AuthorService authorService;	

	static final Pattern pattern = Pattern.compile("\\\\");  
	
	private Site site = Site.me().setSleepTime(6000).addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:45.0) Gecko/20100101 Firefox/45.0");

	@Override
	public void process(Page page) {
		// TODO Auto-generated method stub
		Request request = page.getRequest();
		int type = (Integer) request.getExtra(WechatConfig.WECHAT_TYPE);
		Author author = (Author) request.getExtra("author");
		
		if( type == WechatConfig.WECHAT_SEARCH_SOGOU) {
			//搜狗搜索微信号
			try{
				String listUrl = page.getHtml().$("div.wx-rb_v1", "href").toString();
				if (T.isBlank(listUrl)) {
					String noresult = page.getHtml().xpath("//div[@id='noresult_part1_container']/strong/text()").toString();
					if(!T.isBlank(noresult)) {
						System.out.println("no result for search author : " + author.getWechat());
						//下个公众号
						Request req2 = authorService.getSogouRequestByLastUpdate(author);
						page.addTargetRequest(req2);
					} else {
						System.out.println(page.getRawText());
						page.setSkip(true);
					}
					//page.setSkip(true);
				}else if (listUrl.contains("mp.weixin.qq.com/profile")) {
					String name = page.getHtml().xpath("//h3/text()").toString();
					String weixin = page.getHtml().xpath("//label[@name='em_weixinhao']/text()").toString();
					page.putField("listUrl", listUrl);
					page.putField("name", name);
					page.putField("weixin", weixin);
	
					Request req = new Request(listUrl);
					req.putExtra(WechatConfig.WECHAT_TYPE, WechatConfig.WECHAT_ARTICLE_LIST);
					req.putExtra("author", author);
					page.addTargetRequest(req);
				} else {
					page.setSkip(true);
				}
			}catch (Exception e) {
				page.setSkip(true);
				System.err.println("HTML : " + page.getRawText());
				e.printStackTrace();
			}
		} else if(type == WechatConfig.WECHAT_ARTICLE_LIST) {
			//微信号文章列表页
			try{
				String content = page.getHtml().regex("var msgList = \\'.*\\';").toString();
				if (!T.isBlank(content)) {
					//转义字符转换
					//"&#39;", "'", "&quot;", '"', "&nbsp;", " ", "&gt;", ">", "&lt;", "<", "&amp;", "&", "&yen;", "¥"
					content = content.replaceAll("&quot;", "\"").replaceAll("&#39;", "\'").replaceAll("&nbsp;", " ")
							.replaceAll("&gt;", ">").replaceAll("&lt;", "<").replaceAll("&amp;", "&").replaceAll("&yen;", "¥").trim();
					//提取json数据
					int length = content.length();
					content = content.substring(15, length -2);
					
					//解释json数据
					JSONObject json = JSONObject.parseObject(content);
					JSONArray list = json.getJSONArray("list");
					if (!list.isEmpty()) {
						for(int i = 0; i < list.size(); i++) {
							//以天为单位，每天一个json
							JSONObject dayJson = list.getJSONObject(i);
							
							//基本信息
							JSONObject comm_msg_info = dayJson.getJSONObject("comm_msg_info");
							long datetime = comm_msg_info.getLong("datetime");
							String date = T.format(new Date(datetime * 1000), "yyyy-MM-dd");
							//page.putField("date" + i, date);
							
							//第一篇文章信息
							JSONObject app_msg_ext_info = dayJson.getJSONObject("app_msg_ext_info");
							String title = app_msg_ext_info.getString("title").trim();
							String content_url = "http://mp.weixin.qq.com" + pattern.matcher(app_msg_ext_info.getString("content_url").replaceAll("&amp;", "&")).replaceAll("");
							String cover = pattern.matcher(app_msg_ext_info.getString("cover").replaceAll("&amp;", "&")).replaceAll("");
							

							Article article = articleService.findByTitleAndDate(title, date);
							if (article == null) {				
								Request req = new Request(content_url);
								req.putExtra(WechatConfig.WECHAT_TYPE, WechatConfig.WECHAT_ARTICLE_DETAIL);
								req.putExtra("author", author);
								req.putExtra("title", title);
								req.putExtra("cover", cover);
								req.putExtra("date", date);
								page.addTargetRequest(req);
							}
							
							//其它文章信息
							JSONArray multi_app_msg_item_list = app_msg_ext_info.getJSONArray("multi_app_msg_item_list");
							if (!multi_app_msg_item_list.isEmpty()) {
								for(int j = 0; j < multi_app_msg_item_list.size(); j++) {
									JSONObject multiJson = multi_app_msg_item_list.getJSONObject(j);
									String title1 = multiJson.getString("title").trim();
									String content_url1 = "http://mp.weixin.qq.com" + pattern.matcher(multiJson.getString("content_url").replaceAll("&amp;", "&")).replaceAll("");
									String cover1 = pattern.matcher(multiJson.getString("cover").replaceAll("&amp;", "&")).replaceAll("");

									Article article1 = articleService.findByTitleAndDate(title1, date);
									if (article1 == null) {
										Request req1 = new Request(content_url1);
										req1.putExtra(WechatConfig.WECHAT_TYPE, WechatConfig.WECHAT_ARTICLE_DETAIL);
										req1.putExtra("author", author);
										req1.putExtra("title", title1);
										req1.putExtra("cover", cover1);
										req1.putExtra("date", date);
										page.addTargetRequest(req1);
									}
								}
							}
						}
					} else {
						page.setSkip(true);
					}
					
					//下个公众号
					Request req2 = authorService.getSogouRequestByLastUpdate(author);
					page.addTargetRequest(req2);
					
					//System.out.println(content);
					//page.putField("content", content);
				} else {
					//下个公众号
					Request req2 = authorService.getSogouRequestByLastUpdate(author);
					page.addTargetRequest(req2);
					page.setSkip(true);
				}
			} catch (Exception e) {
				page.setSkip(true);
				//System.err.println("HTML : " + page.getRawText());
				e.printStackTrace();
			}
		} else if (type == WechatConfig.WECHAT_ARTICLE_DETAIL) {
			try{
				Html html = page.getHtml();
				//String title = (String) request.getExtra("title");
				String cover = (String) request.getExtra("cover");
				page.putField("author", author);
				page.putField("cover", cover);
				page.putField("title", html.xpath("//h2[@class='rich_media_title']/text()").toString());
				page.putField("date", html.xpath("//em[@id='post-date']/text()").toString());
				page.putField("content", html.xpath("//div[@id='js_content']/html()").toString());
			} catch (Exception e) {
				page.setSkip(true);
				//System.err.println("HTML : " + page.getRawText());
				e.printStackTrace();
			}
		}		
	}

	@Override
	public Site getSite() {
		// TODO Auto-generated method stub
		return site;
	}

}
