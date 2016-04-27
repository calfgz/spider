package com.spider.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.spider.mapper.AuthorMapper;
import com.spider.model.Author;
import com.spider.model.AuthorExample;
import com.spider.util.T;
import com.spider.wechat.WechatConfig;

import us.codecraft.webmagic.Request;

@Service
public class AuthorService {
	@Autowired
	AuthorMapper authorMapper;
	
	public Author findById(int id) {
		return authorMapper.selectByPrimaryKey(id);
	}
	
	public int save(Author author) {
		return authorMapper.insert(author);
	}
	
	public int updateAll(Author author) {
		return authorMapper.updateByPrimaryKey(author);
	}
    
    public int updateByNoNull(Author agent) {
    	return authorMapper.updateByPrimaryKeySelective(agent);
    }
    
    public Request getSogouRequestByLastUpdate(Author prev) {
    	Request request = null;
    	AuthorExample example = new AuthorExample();
    	AuthorExample.Criteria criteria = example.createCriteria();
    	if(prev != null) {    		
        	criteria.andIdNotEqualTo(prev.getId());
        	if(prev.getUpdateAt() != null) {
        		criteria.andUpdateAtGreaterThanOrEqualTo(prev.getUpdateAt());
        	}

    		Author tmp = new Author();
    		tmp.setId(prev.getId());
    		tmp.setUpdateAt(T.getNow());
    		updateByNoNull(tmp);
    	}
    	PageHelper.startPage(1, 1);
    	PageHelper.orderBy("update_at asc");   
    	List<Author> list = authorMapper.selectByExample(example);
    	if(!list.isEmpty()) {
    		Author author = list.get(0);
    		if(author != null) {
	    		request = new Request(String.format(WechatConfig.WECHAT_SOGOU_URL_TEMPLATE, author.getWechat().trim()));
	    		request.putExtra("authorId", author.getId());
	    		request.putExtra(WechatConfig.WECHAT_TYPE, WechatConfig.WECHAT_SEARCH_SOGOU);
	    		request.putExtra("author", author);
    		}
    	}
    	return request;
    }
    
    public List<Author> getList(int top) {
    	AuthorExample example = new AuthorExample();
    	if(top > 0) {
        	PageHelper.startPage(1, top);
        	PageHelper.orderBy("update_at desc");    		
    	}
    	List<Author> list = authorMapper.selectByExample(example);
    	return list;
    }
    
    public PageInfo<Author> getPager(AuthorExample example, int pageNum, int pageSize) {
    	PageHelper.startPage(pageNum, pageSize);
    	PageHelper.orderBy("id desc");
    	List<Author> list = authorMapper.selectByExample(example);
    	PageInfo<Author> page = new PageInfo<Author>(list);
    	return page;
    }
}
