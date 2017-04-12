package com.taotao.service;

import com.taotao.common.pojo.EasyUITreeNode;

import java.util.List;

/**
 * Created by JesonLee
 * on 2017/4/12.
 */
public interface ItemCatService {
    List<EasyUITreeNode> getItemCatList(long parentId);
}
