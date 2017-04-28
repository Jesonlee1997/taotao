package com.taotao.service;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;

/**
 * Created by JesonLee
 * on 2017/4/11.
 */
public interface ItemService {
    TbItem getItemById(long itemId);
    EasyUIDataGridResult getItemList(int page, int rows);
    TaotaoResult addItem(TbItem item, String desc);
    TbItemDesc getItemDescById(long itemId);
}
