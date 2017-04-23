package com.taotao.content.service;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbContent;

import java.util.List;

/**
 * Created by JesonLee
 * on 2017/4/19.
 */
public interface ContentService {
    EasyUIDataGridResult getContentList(long categoryId, int page, int rows);
    TaotaoResult addContent(TbContent content);
    List<TbContent> getContentByCid(long categoryId);
}
