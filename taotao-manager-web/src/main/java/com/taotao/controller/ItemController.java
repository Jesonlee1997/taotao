package com.taotao.controller;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.pojo.TbItem;
import com.taotao.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by JesonLee
 * on 2017/4/11.
 */
@Controller
public class ItemController {

    @Autowired
    private ItemService itemService;

    @RequestMapping("/item/{itemId}")
    @ResponseBody
    public TbItem getItemById(@PathVariable Long itemId) {
        TbItem tbItem = itemService.getItemById(itemId);
        return tbItem;
    }

    @RequestMapping("/item/list")
    @ResponseBody
    public EasyUIDataGridResult getItemList(Integer page, Integer rows) {
        return itemService.getItemList(page, rows);
    }

    public ItemService getItemService() {
        return itemService;
    }

    public void setItemService(ItemService itemService) {
        this.itemService = itemService;
    }
}
