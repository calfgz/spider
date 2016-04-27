package com.spider.task;

import org.springframework.beans.factory.annotation.Autowired;

import com.spider.crawler.WechatCrawler;

public class SpiderTask extends AbstractTask {
    
    @Autowired
    WechatCrawler wechatCrawler;

	@Override
	public void runTask() {
		// TODO Auto-generated method stub
        wechatCrawler.crawl(); 
		
	}

}
