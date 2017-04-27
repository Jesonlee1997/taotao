package com.taotao.search.listener;

import com.taotao.common.pojo.SearchItem;
import com.taotao.search.mapper.SearchItemMapper;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.IOException;

/**
 * 监听商品添加事件
 * Created by JesonLee
 * on 2017/4/26.
 */
public class ItemAddMessageListener implements MessageListener{

    @Autowired
    private SolrServer solrServer;

    @Autowired
    private SearchItemMapper searchItemMapper;

    @Override
    public void onMessage(Message message) {

        try {
            //从消息中取商品ID
            TextMessage textMessage = (TextMessage) message;

            //根据商品ID查询数据，取商品信息
            long itemId = Long.parseLong(textMessage.getText());

            //等待事务提交
            Thread.sleep(1000);
            SearchItem searchItem = searchItemMapper.getItemById(itemId);

            //创建文档对象，向文档对象中添加域
            SolrInputDocument document = new SolrInputDocument();
            document.addField("id", searchItem.getId());
            document.addField("item_title", searchItem.getTitle());
            document.addField("item_sell_point", searchItem.getSellPoint());
            document.addField("item_price", searchItem.getPrice());
            document.addField("item_image", searchItem.getImage());
            document.addField("item_category_name", searchItem.getCategoryName());
            document.addField("item_desc", searchItem.getItemDesc());

            //把文档写入索引库，提交
            solrServer.add(document);
            solrServer.commit();
            System.out.println("import into sucess");

        } catch (JMSException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
