package com.itenebris.kinedb.jdbc.executor;

import com.itenebris.kinedb.jdbc.result.ResultData;

public class KineData extends ResultData {
    public String sqlType;
    public Integer pageNum;
    public Integer pageSize;
    public Integer total;

    public String getSqlType() {
        return sqlType;
    }

    public void setSqlType(String sqlType) {
        this.sqlType = sqlType;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "KineData{" +
                "sqlType='" + sqlType + '\'' +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", total=" + total +
                '}';
    }
}
