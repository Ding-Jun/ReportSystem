package com.funtest.core.bean.page;

import java.io.Serializable;
import java.util.List;

import com.funtest.core.bean.constant.Constants;

/**
 * 类Page.java的实现描述：公共的分页
 * @param <T> 
 */
public class Page<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<T> rowData;
    private long totalRows;

    private int pageSize = Constants.PAGE_SIZE_DEFAULT;
    private int curPage = Constants.PAGE_CUR_PAGE_DEFAULT;
    private long totalPage;

    private int startPageIndex;
    private int endPageIndex;

    public List<T> getRowData() {
        return rowData;
    }

    public void setRowData(List<T> rowData) {
        this.rowData = rowData;
    }

    public long getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(long totalRows) {
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

    public long getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(long totalPage) {
        this.totalPage = totalPage;
    }

    public int getStartPageIndex() {
        return startPageIndex;
    }

    public void setStartPageIndex(int startPageIndex) {
        this.startPageIndex = startPageIndex;
    }

    public int getEndPageIndex() {
        return endPageIndex;
    }

    public void setEndPageIndex(int endPageIndex) {
        this.endPageIndex = endPageIndex;
    }

}
