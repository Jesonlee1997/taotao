package com.taotao.common.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * 响应EasyUI中要求的数据格式
 * {total:"2", [{"id":"1, "name":"张三}, {"id":"2, "name":"李四"}]}
 * Created by JesonLee
 * on 2017/4/12.
 */
public class EasyUIDataGridResult implements Serializable {
    private long total;
    private List<?> rows;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<?> getRows() {
        return rows;
    }

    public void setRows(List<?> rows) {
        this.rows = rows;
    }
}
