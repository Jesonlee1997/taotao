package com.taotao.solrj;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by JesonLee
 * on 2017/4/23.
 */
public class TestSolrJ {

    @Test
    public void testAddDocument() throws IOException, SolrServerException {
        //创建一个SolrServer对象。创建一个HttpSolrServer对象
        //需要指定solr服务的url
        SolrServer solrServer = new HttpSolrServer("http://192.168.56.153:8080/solr/collection1");
        //创建一个文档对象SolrInputDocument
        SolrInputDocument document = new SolrInputDocument();
        //向文档中添加域，必须有id域，域的名称必须在schema.xml中定义
        document.addField("id", "2");
        document.addField("item_title", "测试商品2");
        document.addField("item_price", 1000);
        //把文档对象写入索引库
        solrServer.add(document);
        //提交
        solrServer.commit();
    }

    @Test
    public void deleteDocumentById() throws IOException, SolrServerException {
        SolrServer solrServer = new HttpSolrServer("http://192.168.56.153:8080/solr/collection1");
        solrServer.deleteById("test001");
        //提交
        solrServer.commit();
    }

    @Test
    public void deleteDocumentByQuery() throws IOException, SolrServerException {
        SolrServer solrServer = new HttpSolrServer("http://192.168.56.153:8080/solr/collection1");
        solrServer.deleteByQuery("item_title:测试商品3");
        solrServer.commit();
    }

    @Test
    public void searchDocument() throws SolrServerException {
        //创建一个SolrServer对象
        SolrServer solrServer = new HttpSolrServer("http://192.168.56.153:8080/solr/collection1");
        SolrQuery solrQuery = new SolrQuery();
        //创建一个Solr对象
        //设置查询条件、过滤条件...
        solrQuery.setQuery("手机");
        solrQuery.setStart(0);
        solrQuery.setRows(10);
        //设置默认搜索域
        solrQuery.set("df", "item_keywords");
        //设置高亮
        solrQuery.setHighlight(true);
        solrQuery.addHighlightField("item_title");
        solrQuery.setHighlightSimplePre("<em>");
        solrQuery.setHighlightSimplePost("</em>");

        //执行查询，得到一个Response对象
        QueryResponse response = solrServer.query(solrQuery);

        //取查询结果
        SolrDocumentList solrDocuments = response.getResults();
        System.out.println("查询结果总记录数：" + solrDocuments.getNumFound());

        //取高亮显示
        for (SolrDocument solrDocument : solrDocuments) {
            System.out.println(solrDocument.get("id"));

            Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
            List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
            String itemTitle = "";
            if (list != null && list.size() > 0) {
                itemTitle = list.get(0);
            } else {
                itemTitle = (String) solrDocument.get("item_title");
            }
            System.out.println(itemTitle);
            System.out.println(solrDocument.get("item_sell_point"));
            System.out.println(solrDocument.get("item_price"));
            System.out.println(solrDocument.get("item_image"));
            System.out.println(solrDocument.get("item_category_name"));
            System.out.println("----------------------------------");
        }

    }
}
