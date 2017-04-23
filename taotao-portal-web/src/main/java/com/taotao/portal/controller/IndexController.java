package com.taotao.portal.controller;

import com.taota.portal.pojo.AD1Node;
import com.taotao.common.utils.JsonUtils;
import com.taotao.content.service.ContentService;
import com.taotao.pojo.TbContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页展示Controller
 * Created by JesonLee
 * on 2017/4/17.
 */
@Controller
public class IndexController {
    @Value("${AD1_CATEGORY_ID}")
    private Long AD1_CATEGORY_ID;

    @Value("${AD1_WIDTH}")
    private Integer AD1_WIDTH;

    @Value("${AD1_WIDTH_B}")
    private Integer AD1_WIDTH_B;

    @Value("${AD1_HEIGHT}")
    private Integer AD1_HEIGHT;

    @Value("${AD1_HEIGHT_B}")
    private Integer AD1_HEIGHT_B;

    @Autowired
    private ContentService contentService;

    @RequestMapping("/index")
    public String showIndex(Model model) {
        //根据cid查询轮播图内容列表
        List<TbContent> list = contentService.getContentByCid(AD1_CATEGORY_ID);
        //把列表转换为AD1Node
        List<AD1Node> ad1Nodes= new ArrayList<>();
        for (TbContent content : list) {
            AD1Node node = new AD1Node();
            node.setAlt(content.getTitle());
            node.setHeight(AD1_HEIGHT);
            node.setHeightB(AD1_HEIGHT_B);
            node.setWidth(AD1_WIDTH);
            node.setWidthB(AD1_WIDTH_B);
            node.setSrc(content.getPic());
            node.setSrcB(content.getPic2());
            node.setHref(content.getUrl());
            ad1Nodes.add(node);
        }

        String ad1Json = JsonUtils.objectToJson(ad1Nodes);
        model.addAttribute("ad1", ad1Json);

        return "index";
    }
}
