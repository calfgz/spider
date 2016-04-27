package com.spider.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.spider.mapper.ArticleMapper;
import com.spider.model.Article;
import com.spider.model.ArticleExample;
@Service
public class ArticleService {
	@Autowired
	ArticleMapper articleMapper;
	
	public Article findById(int id) {
		return articleMapper.selectByPrimaryKey(id);
	}
	
	public int save(Article article) {
		return articleMapper.insert(article);
	}
	
	public int updateAll(Article article) {
		return articleMapper.updateByPrimaryKey(article);
	}
    
    public int updateByNoNull(Article article) {
    	return articleMapper.updateByPrimaryKeySelective(article);
    }
    
    public Article findByTitleAndDate(String title, String date) {
    	Article article = null;
    	try {
    	ArticleExample example = new ArticleExample();
    	ArticleExample.Criteria criteria = example.createCriteria();
    	criteria.andTitleEqualTo(title);
    	criteria.andPdateEqualTo(date);
    	List<Article> list = articleMapper.selectByExample(example);
    	if (!list.isEmpty()) {
    		article = list.get(0);
    	}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return article;    	
    }
    
    public PageInfo<Article> getPager(ArticleExample example, int pageNum, int pageSize) {
    	PageHelper.startPage(pageNum, pageSize);
    	PageHelper.orderBy("id desc");
    	List<Article> list = articleMapper.selectByExample(example);
    	PageInfo<Article> page = new PageInfo<Article>(list);
    	return page;
    }
}
