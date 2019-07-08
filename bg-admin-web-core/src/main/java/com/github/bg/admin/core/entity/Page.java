package com.github.bg.admin.core.entity;

import java.util.List;

/**
 * @author linzf
 * @since 2019-04-26
 * 类描述：分页的实体类
 */
public class Page {

    /**
     * 每页显示多少条数据
     */
    private int pageSize;
    /**
     * 当前第几页
     */
    private int current;
    /**
     * 总记录数
     */
    private long total;
    /**
     * 集合数据
     */
    private List rows;

    public Page(){
        super();
    }

    public Page(int pageSize, int current, long total, List rows){
        this.pageSize = pageSize;
        this.current = current;
        this.total = total;
        this.rows = rows;
    }


    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List getRows() {
        return rows;
    }

    public void setRows(List rows) {
        this.rows = rows;
    }
}
