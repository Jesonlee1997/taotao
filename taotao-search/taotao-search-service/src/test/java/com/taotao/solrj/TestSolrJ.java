package com.taotao.solrj;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import java.io.IOException;

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

}
