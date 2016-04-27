package com.spider.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.github.pagehelper.PageInfo;
import com.spider.crawler.WechatCrawler;
import com.spider.model.Article;
import com.spider.model.ArticleExample;
import com.spider.model.Author;
import com.spider.model.AuthorExample;
import com.spider.service.ArticleService;
import com.spider.service.AuthorService;
import com.spider.util.T;

@Controller
public class AdminController {
	
	@Autowired
	AuthorService authorService;
	
	@Autowired
	ArticleService articleService;
	
	@Autowired
	WechatCrawler wechatCrawler;

    @RequestMapping(value="/admin/index.do", method=RequestMethod.GET)
    public String showIndex(HttpServletRequest req, HttpServletResponse resp) throws Exception{
        return "admin/index";
    }
    
    @RequestMapping(value="/admin/author.do")
    public String author(HttpServletRequest req, HttpServletResponse resp) throws Exception{
    	int pageNum = T.intValue(req.getParameter("pageNum"), 1);
    	int pageSize = T.intValue(req.getParameter("pageSize"), 25);
    	AuthorExample example = new AuthorExample();
    	PageInfo<Author> pager = authorService.getPager(example, pageNum, pageSize);
        req.setAttribute("pager", pager);
        return "admin/author";
    }
    
    @RequestMapping(value="/admin/article.do")
    public String article(HttpServletRequest req, HttpServletResponse resp) throws Exception{
    	int pageNum = T.intValue(req.getParameter("pageNum"), 1);
    	int pageSize = T.intValue(req.getParameter("pageSize"), 25);
    	ArticleExample example = new ArticleExample();
    	PageInfo<Article> pager = articleService.getPager(example, pageNum, pageSize);
        req.setAttribute("pager", pager);
        return "admin/article";
    }
    
    @RequestMapping(value="/admin/crawl.do", method=RequestMethod.GET)
    public String crawl(HttpServletRequest req, HttpServletResponse resp) throws Exception{
    	wechatCrawler.crawl();
        return "{ok}";
    }
}
