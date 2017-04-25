package com.taotao.search.service.impl;

import com.taotao.common.pojo.SearchResult;
import com.taotao.search.dao.SearchDao;
import com.taotao.search.service.SearchService;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 搜索服务功能实现
 * Created by JesonLee
 * on 2017/4/24.
 */
@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private SearchDao searchDao;

    @Override
    public SearchResult search(String queryString, int page, int rows){
        //根据查询条件拼装查询对象
        SolrQuery query = new SolrQuery();
        query.setQuery(queryString);

        //设置分页条件
        if (page < 1) page = 1;
        query.setStart((page - 1) * rows);
        if (rows < 1) rows = 10;
        query.setRows(rows);

        //设置默认搜索域
        query.set("df", "item_title");

        //设置高亮显示
        query.setHighlight(true);
        query.addHighlightField("item_title");
        query.setHighlightSimplePre("<font color='red'>");
        query.setHighlightSimplePost("</font>");

        //调用Dao进行查询
        SearchResult searchResult = null;
        try {
            searchResult = searchDao.search(query);
        } catch (SolrServerException e) {
            e.printStackTrace();
        }
        long recordCount = searchResult.getRecordCount();
        long pages = recordCount / rows;
        if (recordCount % rows > 0) {
            pages++;
        }
        searchResult.setTotalPages(pages);
        return searchResult;
    }


}