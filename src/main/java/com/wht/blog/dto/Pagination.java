package com.wht.blog.dto;

import com.github.pagehelper.Page;
import lombok.Data;

import java.util.List;

/**
 * 分页Bean
 * @author wht
 * @since 2019-09-18 16:14
 */
@Data
public class Pagination<T> {
    private int pageNum;
    private int pageSize;
    private long total;
    private int pages;
    private boolean count;
    private String orderBy;
    private List<T> items;

    public Pagination() {
    }

    @SuppressWarnings("unchecked")
    public Pagination(Page page) {
        pageNum = page.getPageNum();
        pageSize = page.getPageSize();
        total = page.getTotal();
        pages =page.getPages();
        count = page.isCount();
        orderBy = page.getOrderBy();
        items = page.getResult();
    }

    public Pagination(List list) {
        pageNum = 1;
        pageSize = list.size();
        total = list.size();
        pages = 1;
        count = false;
        orderBy = "";
        items = list;
    }
}
