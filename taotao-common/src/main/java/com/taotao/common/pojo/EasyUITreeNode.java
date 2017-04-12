package com.taotao.common.pojo;

import java.io.Serializable;

/**
 * Created by JesonLee
 * on 2017/4/12.
 */
public class EasyUITreeNode implements Serializable {
    private long id;
    private String text;

    //是否是父节点
    private String state;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}
