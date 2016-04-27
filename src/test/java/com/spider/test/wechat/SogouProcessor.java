package com.spider.test.wechat;

import org.junit.Test;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

public class SogouProcessor implements PageProcessor {
	
	private Site site = Site.me().setSleepTime(1000);
			/*.addCookie(".sogou.com", "ppinf", "5|1460344040|1461553640|dHJ1c3Q6MToxfGNsaWVudGlkOjQ6MjAxN3x1bmlxbmFtZTo0OkNhbGZ8Y3J0OjEwOjE0NjAzNDQwNDB8cmVmbmljazo0OkNhbGZ8dXNlcmlkOjQ0OjMwRjYwNDUwMEU0MjY1QTlFNkQxMUU1NTQ3MDJCRjczQHFxLnNvaHUuY29tfA")
			.addCookie("weixin.sogou.com", "ppmdig", "14603440400000009ae08fc87931f2629d9fc275784939f9")
			.addCookie(".sogou.com", "pprdig", "ffaf9Zf9yir2wf_8yOPiqwSnAt-icU8OUcnXuEn0a7m3Ri_hoyCT-bTD8Fp4wKEPbBU7lx3Gh3BVYU7DZpAsFMK_M6a7nZF2PgOriQ4GJuhw5qHw_5v7FEk7-0QMNDTCw2Tm0mLTwLkfWxA9lJhXxfikg7sv5HLtf51mwDcA2e4");*/

	@Override
	public Site getSite() {
		// TODO Auto-generated method stub
		return site;
	}

	@Override
	public void process(Page page) {

		String url = page.getUrl().toString();
		System.out.println(url);
			//搜索微信号
		String listUrl = page.getHtml().$("div.wx-rb_v1", "href").toString();
		String name = page.getHtml().xpath("//h3/text()").toString();
		String weixin = page.getHtml().xpath("//label[@name='em_weixinhao']/text()").toString();
		page.putField("listUrl", listUrl);
		page.putField("name", name);
		page.putField("weixin", weixin);
	}
	
	@Test
	public void test() {
		String url = "http://weixin.sogou.com/weixin?type=1&query=gzpclady&ie=utf8&_sug_=n&_sug_type_=";
        Spider.create(new SogouProcessor()).addUrl(url).run();
	}

}
