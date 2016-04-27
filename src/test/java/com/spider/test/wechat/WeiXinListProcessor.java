package com.spider.test.wechat;

import java.util.Date;
import java.util.regex.Pattern;

import org.junit.Test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.spider.util.T;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

public class WeiXinListProcessor implements PageProcessor {
	
	private Site site = Site.me().setSleepTime(1000);
			/*.addCookie(".sogou.com", "ppinf", "5|1460344040|1461553640|dHJ1c3Q6MToxfGNsaWVudGlkOjQ6MjAxN3x1bmlxbmFtZTo0OkNhbGZ8Y3J0OjEwOjE0NjAzNDQwNDB8cmVmbmljazo0OkNhbGZ8dXNlcmlkOjQ0OjMwRjYwNDUwMEU0MjY1QTlFNkQxMUU1NTQ3MDJCRjczQHFxLnNvaHUuY29tfA")
			.addCookie("weixin.sogou.com", "ppmdig", "14603440400000009ae08fc87931f2629d9fc275784939f9")
			.addCookie(".sogou.com", "pprdig", "ffaf9Zf9yir2wf_8yOPiqwSnAt-icU8OUcnXuEn0a7m3Ri_hoyCT-bTD8Fp4wKEPbBU7lx3Gh3BVYU7DZpAsFMK_M6a7nZF2PgOriQ4GJuhw5qHw_5v7FEk7-0QMNDTCw2Tm0mLTwLkfWxA9lJhXxfikg7sv5HLtf51mwDcA2e4");*/

	@Override
	public Site getSite() {
		// TODO Auto-generated method stub
		return site;
	}
	
	static final Pattern pattern=Pattern.compile("\\\\");  

	@Override
	public void process(Page page) {

		String url = page.getUrl().toString();
		System.out.println(url);
		
		//System.out.println(page.getRawText());
		//微信号文章列表页
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
			int idx = 1;
			for(int i = 0; i < list.size(); i++) {
				//以天为单位，每天一个json
				JSONObject dayJson = list.getJSONObject(i);
				
				//基本信息
				JSONObject comm_msg_info = dayJson.getJSONObject("comm_msg_info");
				long datetime = comm_msg_info.getLong("datetime");
				String date = T.format(new Date(datetime * 1000), "yyyy-MM-dd");
				page.putField("date" + i, date);
				
				//第一篇文章信息
				JSONObject app_msg_ext_info = dayJson.getJSONObject("app_msg_ext_info");
				String title = app_msg_ext_info.getString("title");
				String content_url = app_msg_ext_info.getString("content_url");
				String cover = app_msg_ext_info.getString("cover");
				page.putField("title" + idx, title);
				page.putField("content_url" + idx, pattern.matcher(content_url).replaceAll(""));
				page.putField("cover" + idx, pattern.matcher(cover).replaceAll(""));
				idx++;
				
				//其它文章信息
				JSONArray multi_app_msg_item_list = app_msg_ext_info.getJSONArray("multi_app_msg_item_list");
				for(int j = 0; j < multi_app_msg_item_list.size(); j++) {
					JSONObject multiJson = multi_app_msg_item_list.getJSONObject(j);
					page.putField("title" + idx, multiJson.getString("title"));
					page.putField("content_url" + idx, pattern.matcher(multiJson.getString("content_url")).replaceAll(""));
					page.putField("cover" + idx, pattern.matcher(multiJson.getString("cover")).replaceAll(""));
					idx++;
				}
			}
			
			//System.out.println(content);
			page.putField("content", content);
		} else {
			page.setSkip(true);
		}
	}
	
	@Test
	public void test() {
		//http://mp.weixin.qq.com/profile?src=3&timestamp=1461139948&ver=1&signature=OxXvavi0Xl2vYAx8Q4yhFljM5MO8RS4jbyEsCRGNrT7zEzOGKRdN0xa*MZJ8-e9OAxFckG7-PrhqlJtgAPhHtQ==
		String url = "http://mp.weixin.qq.com/profile?src=3&timestamp=1461205154&ver=1&signature=OxXvavi0Xl2vYAx8Q4yhFljM5MO8RS4jbyEsCRGNrT7zEzOGKRdN0xa*MZJ8-e9OabmmTeScysYOnoy5M3WNCA==";
        Spider.create(new WeiXinListProcessor()).addUrl(url).run();
	}

}
