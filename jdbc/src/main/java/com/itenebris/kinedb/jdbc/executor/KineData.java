package com.itenebris.kinedb.jdbc.executor;

import com.itenebris.kinedb.jdbc.KineType;
import com.itenebris.kinedb.jdbc.result.ResultData;
import com.itenebris.kinedb.jdbc.util.KineTypeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class KineData extends ResultData {

    static Logger log = LoggerFactory.getLogger(KineData.class);
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
                ", data = " + super.toString() +
                '}';
    }

    public static KineData convertExecuteSqlResult(Kine.Results result) throws SQLException {
        log.debug(" result.Rows length " + result.getRowsCount());
        int rowCount = result.getRowsCount();

        // 1. use the first row to generate the result rowNames
        List<String> rowNames = null;
        List<String> rowTypes = null;
        if (rowCount > 0) {
            Kine.RowRef firstRow = result.getRows(0);
            rowNames = new ArrayList<>(firstRow.getColumnsCount());
            rowTypes = new ArrayList<>(firstRow.getColumnsCount());

            for (int i=0; i< firstRow.getColumnsCount(); i++) {
                Kine.ColumnValueRef column = firstRow.getColumns(i);
                rowNames.add(column.getName());
                rowTypes.add(column.getType());
            }
        }
        //System.out.println("convertExecuteSqlResultRow rowNames: " + rowNames);

        // 2. generate the result rowValues
        List<Object[]> rowValues  = new ArrayList<>(rowCount);
        for (int i=0; i<rowCount; i++) {
            Object[] rowValue = convertExecuteSqlResultRow(result.getRows(i));
            rowValues.add(rowValue);
        }


        KineData resultData = new KineData();
        if (rowNames != null) {
            resultData.setRowNames(rowNames.toArray((new String[0])));
            resultData.setTypes(rowTypes.toArray((new String[0])));
            resultData.setRowValues(rowValues.toArray(new Object[0][0]));
        }

        //System.out.println("ExecuteSQL result :\n " + resultData);
        return resultData;
    }

    private static Object[] convertExecuteSqlResultRow(Kine.RowRef row ) throws SQLException {

        List<Object> values = new ArrayList<>();

        for (int i=0; i<row.getColumnsCount(); i++) {
            Kine.ColumnValueRef columnValue = row.getColumns(i);
            Object colValue = convertColumnValue(columnValue);
            values.add(colValue);
        }
        //System.out.println("convertExecuteSqlResultRow rowValue: " + values);
        return values.toArray();
    }

    private static Object convertColumnValue(Kine.ColumnValueRef columnValue) throws SQLException {
        KineType kineType = KineType.getByName(columnValue.getType());
        Object value = KineTypeUtils.kineTypeBytesToJavaTypes(kineType, columnValue.getValue().toByteArray());
        return value;
    }
}
