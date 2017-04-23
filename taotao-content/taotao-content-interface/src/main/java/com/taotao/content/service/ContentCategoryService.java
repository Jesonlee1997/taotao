package com.taotao.content.service;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;

import java.util.List;

/**
 * Created by JesonLee
 * on 2017/4/17.
 */
public interface ContentCategoryService {
    List<EasyUITreeNode> getContentCategoryList(long parentId);

    TaotaoResult addContentCategory(long parentId, String name);

    TaotaoResult updateContentCategory(long id, String name);

    TaotaoResult deleteContentCategory(long id);
}
