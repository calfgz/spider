package com.spider.wechat;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.spider.model.Article;
import com.spider.model.Author;
import com.spider.service.ArticleService;
import com.spider.service.AuthorService;

import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

@Component
public class WechatPipeline implements Pipeline {
	
	@Autowired
	AuthorService authorService;
	
	@Autowired
	ArticleService articleService;
	
	@Override
	public void process(ResultItems resultItems, Task task) {
		// TODO Auto-generated method stub
		Map<String, Object> map = resultItems.getAll();
		Request request = resultItems.getRequest();
		Author author = (Author) request.getExtra("author");
		
		if (author == null) {
			System.err.println("author is null.");
			for (Map.Entry<String, Object> entry : map.entrySet()) {
	            System.out.println(entry.getKey() + ":\t" + entry.getValue());
	        }
		} else {		
			int type = (Integer) request.getExtra(WechatConfig.WECHAT_TYPE);
			if (type == WechatConfig.WECHAT_SEARCH_SOGOU) {
				//搜索微信号
				for (Map.Entry<String, Object> entry : map.entrySet()) {
		            System.out.println(entry.getKey() + ":\t" + entry.getValue());
		        }
			} else if (type == WechatConfig.WECHAT_ARTICLE_LIST) {
				//微信列表页
				for (Map.Entry<String, Object> entry : map.entrySet()) {
		            System.out.println(entry.getKey() + ":\t" + entry.getValue());
		        }
				/*int totalItems = (Integer) map.get("totalItems");
				//更新时间标示
				if (totalItems > 0) {
					Author tmp = new Author();
					tmp.setId(author.getId());
					tmp.setUpdateAt(new Date());
					authorService.updateByNoNull(tmp);
				}*/
			} else if (type == WechatConfig.WECHAT_ARTICLE_DETAIL) {
				try {
					//终端页
					String cover = (String) map.get("cover");
					String title = (String) map.get("title");
					String date = (String) map.get("date");
					String content = (String) map.get("content");
					
					Article article = articleService.findByTitleAndDate(title, date);
					if (article == null) {				
						article = new Article();
						article.setAuthorId(author.getId());
						article.setImg(cover);
						article.setPdate(date);
						article.setUrl(request.getUrl());
						article.setTitle(title);
						article.setContent(content);
						articleService.save(article);				
					} else {
						System.out.println("the article is exists, id:" + article.getId());
					}			
				} catch (Exception e) {
					System.out.println("EXCEPTION.");
					e.printStackTrace();
				}
			} else {
				for (Map.Entry<String, Object> entry : map.entrySet()) {
		            System.out.println(entry.getKey() + ":\t" + entry.getValue());
		        }
			}
		}
	}

}
