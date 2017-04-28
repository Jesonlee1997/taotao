package com.taotao.freemaker;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

/**
 * Created by JesonLee
 * on 2017/4/27.
 */
public class TestFreeMarker {

    @Test
    public void testFreeMarker() throws IOException, TemplateException {
        //1.创建一个模板文件

        //2.创建一个Configuration对象
        Configuration configuration = new Configuration(Configuration.getVersion());

        //3.设置模板所在的路径
        configuration.setDirectoryForTemplateLoading(
                new File("J:\\Java\\projects\\taotao\\taotao-item-web\\src\\main\\webapp\\WEB-INF\\ftl"));

        //4.设置模板的字符集
        configuration.setDefaultEncoding("utf-8");

        //5.使用Configuration对象加载一个模板文件，需要指定模板文件的文件名
        Template template = configuration.getTemplate("student.ftl");

        //6.创建一个数据集，可以是pojo也可以是map
        Map data = new HashMap();
        data.put("hello", "hello freemarker");

        Student student = new Student(1, "jeson", 11, "北京");
        data.put("student", student);

        List<Student> students = new ArrayList<Student>(){{
            add(new Student(1, "jeson", 11, "北京"));
            add(new Student(2, "jeson", 11, "北京"));
            add(new Student(3, "jeson", 11, "北京"));
            add(new Student(4, "jeson", 11, "北京"));
            add(new Student(5, "jeson", 11, "北京"));
            add(new Student(6, "jeson", 11, "北京"));
        }};

        data.put("students", students);

        //日期类型的处理
        data.put("date", new Date());

        //7.创建一个Writer对象，指定输出文件的路径及文件名
        Writer out = new FileWriter(
                new File("J:\\Java\\projects\\taotao\\taotao-item-web\\src\\main\\webapp\\WEB-INF\\out\\student.html"));

        //8.使用模板对象的process方法输出文件
        template.process(data, out);

        //9.关闭流
        out.close();
    }
}
