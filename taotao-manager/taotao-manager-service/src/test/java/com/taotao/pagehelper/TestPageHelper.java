package com.taotao.pagehelper;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemExample;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by JesonLee
 * on 2017/4/12.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/spring/applicationContext-dao.xml"})
public class TestPageHelper {
    @Autowired
    private TbItemMapper itemMapper;

    @Test
    public void testPageHelper() {
        //1.在mybatis的配置文件中配置分页插件
        //2.在执行查询之前配置分页条件，使用PageHelper的静态方法
        PageHelper.startPage(1, 10);
        //3.执行查询
        //创建Example对象
        TbItemExample example = new TbItemExample();
        List<TbItem> list = itemMapper.selectByExample(example);

        //4.获取分页信息，使用PageInfo对象取
        PageInfo<TbItem> pageInfo = new PageInfo<TbItem>(list);
        System.out.println("总记录数：" + pageInfo.getTotal());
        System.out.println("总页数：" + pageInfo.getPages());
        System.out.println("返回的记录数：" + list.size());
    }

}
