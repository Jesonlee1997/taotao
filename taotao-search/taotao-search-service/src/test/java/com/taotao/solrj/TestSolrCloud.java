package com.taotao.solrj;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by JesonLee
 * on 2017/4/25.
 */
public class TestSolrCloud {

    @Test
    public void testSolrCloudAdDocument() throws IOException, SolrServerException {
        //创建一个CloudSolrServer对象
        CloudSolrServer cloudSolrServer = new CloudSolrServer(
                "192.168.56.153:2181,192.168.56.153:2182,192.168.56.153:2183");

        //设置默认collection
        cloudSolrServer.setDefaultCollection("collection");

        //创建一个文档对象
        SolrInputDocument document = new SolrInputDocument();
        document.addField("id", "test001");
        document.addField("item_title", "测试商品名称");
        document.addField("item_price", 100);

        //将文档写入索引库，提交
        cloudSolrServer.add(document);
        cloudSolrServer.commit();
    }
}
