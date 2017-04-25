package com.taotao.search.dao;

import com.taotao.common.pojo.SearchItem;
import com.taotao.common.pojo.SearchResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 查询索引库商品dao
 * Created by JesonLee
 * on 2017/4/24.
 */
@Repository
public class SearchDao {
    @Autowired
    private SolrServer solrServer;

    public SearchResult search(SolrQuery query) throws SolrServerException {
        //根据query对象进行查询
        QueryResponse response = solrServer.query(query);

        //取查询结果
        SolrDocumentList results = response.getResults();
        long numFound = results.getNumFound();

        SearchResult searchResult = new SearchResult();
        searchResult.setRecordCount(numFound);

        List<SearchItem> items = new ArrayList<>();
        for (SolrDocument solrDocument : results) {
            SearchItem searchItem = new SearchItem();
            searchItem.setCategoryName((String) solrDocument.get("item_category_name"));
            searchItem.setId((String) solrDocument.get("id"));
            //取一张图片
            String image = (String) solrDocument.get("item_image");
            if (StringUtils.isNotBlank(image)) {
                image = image.split(",")[0];
            }
            searchItem.setImage(image);
            searchItem.setPrice((Long) solrDocument.get("item_price"));
            searchItem.setSellPoint((String) solrDocument.get("item_sell_point"));

            //取高亮显示
            Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
            List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
            String title;
            if (list != null && list.size() > 0) {
                title = list.get(0);
            } else {
                title = (String) solrDocument.get("item_title");
            }
            searchItem.setTitle(title);
            items.add(searchItem);
        }
        searchResult.setItemList(items);
        return searchResult;
    }
}
