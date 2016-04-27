package com.spider.test.wechat;

import org.junit.Test;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

public class ArticleProcessor implements PageProcessor {
	
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
		//System.out.println(page.getRawText());
		//微信号文章终端页
		Html html = page.getHtml();
		page.putField("title", html.xpath("//h2[@class='rich_media_title']/text()").toString());
		page.putField("date", html.xpath("//em[@id='post-date']/text()").toString());
		
		page.putField("content", html.xpath("//div[@id='js_content']/html()").toString());
	}
	
	@Test
	public void test() {
		//http://mp.weixin.qq.com/s?timestamp=1461143670&amp;src=3&amp;ver=1&amp;signature=bJLPicLlOusEetmwFejTUF0N834XmDG8rdBLpxrgQbvJNEw70HU-7QEvYmjIpeM4W9ZgoNjR7eixo8qLfsRGhPcLkNIpXYun7fvaqOWBuJzg9MySSc9WH7*OvmkyjFhm8f64eC60QSLZ-MimBjH3hQTL*s4N4VlW7FF5N38poMY=
		String url = "http://mp.weixin.qq.com/s?timestamp=1461143670&amp;src=3&amp;ver=1&amp;signature=bJLPicLlOusEetmwFejTUF0N834XmDG8rdBLpxrgQbvJNEw70HU-7QEvYmjIpeM4W9ZgoNjR7eixo8qLfsRGhPcLkNIpXYun7fvaqOWBuJzg9MySSc9WH7*OvmkyjFhm8f64eC60QSLZ-MimBjH3hQTL*s4N4VlW7FF5N38poMY=";
        Spider.create(new ArticleProcessor()).addUrl(url.replaceAll("&amp;", "&")).run();
    }

}
