package com.taotao.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.IDUtils;
import com.taotao.common.utils.JsonUtils;
import com.taotao.jedis.JedisClient;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemExample;
import com.taotao.service.ItemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.Destination;
import java.util.Date;
import java.util.List;

/**
 * Created by JesonLee
 * on 2017/4/11.
 */
@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private TbItemMapper itemMapper;

    @Autowired
    private TbItemDescMapper itemDescMapper;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Resource(name = "itemAddTopic")
    private Destination destination;

    @Autowired
    private JedisClient jedisClient;

    @Value("${ITEM_INFO}")
    private String ITEM_INFO;

    @Value("${ITEM_EXPIRE}")
    private Integer ITEM_EXPIRE;

    @Override
    public TbItem getItemById(long itemId) {
        //查询数据库之前先查询缓存
        String key = ITEM_INFO+ ":"+itemId+":BASE";
        try {
            String json = jedisClient.get(key);
            if (StringUtils.isNotBlank(json)) {
                return JsonUtils.jsonToPojo(json, TbItem.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //缓存中没有查询数据库
        TbItem item = itemMapper.selectByPrimaryKey(itemId);

        //把查询结果添加到缓存
        try {

            jedisClient.set(key, JsonUtils.objectToJson(item));

            //设置过期时间提高缓存利用率
            jedisClient.expire(key, ITEM_EXPIRE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return item;
    }

    @Override
    public EasyUIDataGridResult getItemList(int page, int rows) {
        //设置分页信息
        PageHelper.startPage(page, rows);

        //执行查询
        TbItemExample example = new TbItemExample();
        List<TbItem> list = itemMapper.selectByExample(example);

        //取查询结果
        PageInfo<TbItem> pageInfo = new PageInfo<>(list);
        EasyUIDataGridResult result = new EasyUIDataGridResult();
        result.setTotal(pageInfo.getTotal());
        result.setRows(list);
        return result;
    }

    @Override
    public TaotaoResult addItem(TbItem item, String desc) {
        //生成商品id
        long itemId = IDUtils.genItemId();

        //补全item的属性
        item.setId(itemId);
        //商品状态，1-正常，2-下架，3-删除
        item.setStatus((byte)1);
        item.setCreated(new Date());
        item.setUpdated(new Date());

        //向商品表插入数据
        itemMapper.insert(item);

        //创建一个商品描述表对应的pojo
        TbItemDesc tbItemDesc = new TbItemDesc();
        tbItemDesc.setItemId(itemId);
        tbItemDesc.setItemDesc(desc);
        tbItemDesc.setCreated(new Date());
        tbItemDesc.setUpdated(new Date());

        //向商品描述表插入数据
        itemDescMapper.insert(tbItemDesc);

        //向ActiveMQ发送商品添加消息
        jmsTemplate.send(destination, session -> session.createTextMessage(String.valueOf(itemId)));

        return TaotaoResult.ok();
    }

    @Override
    public TbItemDesc getItemDescById(long itemId) {
        //查询数据库之前先查询缓存
        String key = ITEM_INFO+ ":"+itemId+":DESC";
        try {
            String json = jedisClient.get(key);
            if (StringUtils.isNotBlank(json)) {
                return JsonUtils.jsonToPojo(json, TbItemDesc.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //缓存中没有查询数据库
        TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(itemId);

        //把查询结果添加到缓存
        try {

            jedisClient.set(key, JsonUtils.objectToJson(itemDesc));

            //设置过期时间提高缓存利用率
            jedisClient.expire(key, ITEM_EXPIRE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return itemDesc;
    }
}
