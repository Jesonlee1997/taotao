package com.taotao.content.service.impl;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentCategoryService;
import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.pojo.TbContentCategory;
import com.taotao.pojo.TbContentCategoryExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 内容管理Service
 * Created by JesonLee
 * on 2017/4/17.
 */
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {
    @Autowired
    private TbContentCategoryMapper categoryMapper;

    @Override
    public List<EasyUITreeNode> getContentCategoryList(long parentId) {
        //根据parentId查询子节点列表
        TbContentCategoryExample example = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        //执行查询
        List<TbContentCategory> categoryList = categoryMapper.selectByExample(example);
        List<EasyUITreeNode> nodes = new ArrayList<>();
        for (TbContentCategory category : categoryList) {
            EasyUITreeNode node = new EasyUITreeNode();
            node.setId(category.getId());
            node.setText(category.getName());
            node.setState(category.getIsParent()?"closed":"open");
            nodes.add(node);
        }
        return nodes;
    }

    @Override
    public TaotaoResult addContentCategory(long parentId, String name) {
        //创建一个pojo对象
        TbContentCategory contentCategory = new TbContentCategory();
        //补全对象的的属性
        contentCategory.setParentId(parentId);
        contentCategory.setName(name);
        //状态。可选值：1（正常），2（删除）
        contentCategory.setStatus(1);
        //排序，默认为1
        contentCategory.setSortOrder(1);
        //新加的节点一定为叶子节点
        contentCategory.setIsParent(false);
        contentCategory.setCreated(new Date());
        contentCategory.setUpdated(new Date());
        //插入到数据中
        categoryMapper.insert(contentCategory);
        //判断父节点的状态
        TbContentCategory parent = categoryMapper.selectByPrimaryKey(parentId);
        if (!parent.getIsParent()) {
            //如果父节点为叶子节点应该改为父节点
            parent.setIsParent(true);
            categoryMapper.updateByPrimaryKey(parent);
        }
        //返回结果
        return TaotaoResult.ok(contentCategory);
    }

    @Override
    public TaotaoResult updateContentCategory(long id, String name) {
        TbContentCategory category = categoryMapper.selectByPrimaryKey(id);
        category.setName(name);
        categoryMapper.updateByPrimaryKey(category);
        return TaotaoResult.ok();
    }

    @Override
    public TaotaoResult deleteContentCategory(long id) {
        //删除该节点
        TbContentCategory contentCategory = categoryMapper.selectByPrimaryKey(id);
        deleteContentCat(id);

        //设置查询条件，查询该节点父节点所有的子节点，判断该节点的父节点是否没有子节点
        TbContentCategoryExample example = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(contentCategory.getParentId());
        List<TbContentCategory> children = categoryMapper.selectByExample(example);
        TbContentCategory father = categoryMapper.selectByPrimaryKey(contentCategory.getParentId());
        if (children.size() == 0){
            father.setIsParent(false);
        }
        categoryMapper.updateByPrimaryKey(father);
        return TaotaoResult.ok();
    }

    private void deleteContentCat(long id) {
        TbContentCategory category = categoryMapper.selectByPrimaryKey(id);
        //如果是是父节点的话就级联删除
        if (category.getIsParent()) {
            TbContentCategoryExample example = new TbContentCategoryExample();
            TbContentCategoryExample.Criteria criteria = example.createCriteria();
            //设置查询条件，查询所有父节点为该节点的节点
            criteria.andParentIdEqualTo(id);
            List<TbContentCategory> categories = categoryMapper.selectByExample(example);
            for (TbContentCategory contentCategory : categories) {
                deleteContentCat(contentCategory.getId());
            }
        }
        categoryMapper.deleteByPrimaryKey(id);
    }


}
