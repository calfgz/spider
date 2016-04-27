package com.spider.crawler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.spider.service.AuthorService;
import com.spider.wechat.WechatPipeline;
import com.spider.wechat.WechatProcessor;

import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;

@Component
public class WechatCrawler {
	
	@Autowired
	AuthorService authorService;
	
	@Autowired
	WechatPipeline wechatPipeline;
	
	@Autowired
	WechatProcessor wechatProcessor;
	
	private static boolean running = false;
	
	//private String urlTemplate = "http://weixin.sogou.com/weixin?type=1&query=%s&ie=utf8&_sug_=n&_sug_type_=";
	
	public final static Log log = LogFactory.getLog(WechatCrawler.class);

    public void crawl() {
        if(running) {
            log.debug("Scawler is running~ please try again for a moment~");
            return;
        }
        running = true;
        log.fatal("start to run...");
        
    	/*Site site = wechatProcessor.getSite();
    	site.setDomain(".sogou.com");
    	site.addCookie(".sogou.com", "ppinf", "5|1460344040|1461553640|dHJ1c3Q6MToxfGNsaWVudGlkOjQ6MjAxN3x1bmlxbmFtZTo0OkNhbGZ8Y3J0OjEwOjE0NjAzNDQwNDB8cmVmbmljazo0OkNhbGZ8dXNlcmlkOjQ0OjMwRjYwNDUwMEU0MjY1QTlFNkQxMUU1NTQ3MDJCRjczQHFxLnNvaHUuY29tfA")
		.addCookie("weixin.sogou.com", "ppmdig", "14603440400000009ae08fc87931f2629d9fc275784939f9")
		.addCookie(".sogou.com", "pprdig", "ffaf9Zf9yir2wf_8yOPiqwSnAt-icU8OUcnXuEn0a7m3Ri_hoyCT-bTD8Fp4wKEPbBU7lx3Gh3BVYU7DZpAsFMK_M6a7nZF2PgOriQ4GJuhw5qHw_5v7FEk7-0QMNDTCw2Tm0mLTwLkfWxA9lJhXxfikg7sv5HLtf51mwDcA2e4");*/
    	
    	Request request = authorService.getSogouRequestByLastUpdate(null);
    	Spider spider = Spider.create(wechatProcessor).addPipeline(wechatPipeline).addRequest(request);
	    try {
	    	spider.run();   
    	} catch (Exception e) {
            e.printStackTrace();
            spider.close();
        } finally {
            running = false;  
        } 
    }

    /*public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:/applicationContext.xml");
        WechatCrawler wechatCrawler = applicationContext.getBean(WechatCrawler.class);
        wechatCrawler.crawl();
    }*/

}
