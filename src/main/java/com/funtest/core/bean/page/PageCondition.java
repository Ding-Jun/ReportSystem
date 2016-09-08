package com.funtest.core.bean.page;

import java.io.Serializable;

import com.funtest.core.bean.constant.Constants;

/**
 * 
 * @ClassName: PageCondition
 * @Description:分页查询
 */
public class PageCondition implements Serializable {

    private static final long serialVersionUID = -6435905288498512596L;

    private int totalRows;
    private int pageSize = Constants.PAGE_SIZE_DEFAULT;
    private int curPage = Constants.PAGE_CUR_PAGE_DEFAULT;
    private int totalPages;

    private String sort="";    // 排序字段
//    private String order = Constants.PAGE_ORDER_DEFAULT; // asc/desc
    private String order="";

    public int getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCurPage() {
        return curPage;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

}
