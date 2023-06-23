package com.itenebris.kinedb.jdbc.result;

import com.itenebris.kinedb.jdbc.KineType;
import com.itenebris.kinedb.jdbc.exceptions.SqlError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

public class ResultData {

    Logger log = LoggerFactory.getLogger(ResultData.class);
    public String[] rowNames;
    public String[] types;
    public KineType[] kineTypes;
    public Object[][] rowValues;

    public Field[] fields;

    private Map<String, Integer> columnNameToIndex = null;
    private boolean builtIndexMapping = false;

    public String[] getColumnNames() {
        return this.rowNames;
    }

    public void buildIndexMapping() {
        if (this.rowNames == null) {
            this.builtIndexMapping = true;
            return;
        }
        int numColumns = this.rowNames.length;
        this.columnNameToIndex = new TreeMap(String.CASE_INSENSITIVE_ORDER);
        this.kineTypes = new KineType[numColumns];
        this.fields = new Field[numColumns];

        for(int i = numColumns - 1; i >= 0; --i) {
            Integer index = i;
            String columnName = this.rowNames[i];
            this.columnNameToIndex.put(columnName, index);

            String columnType = this.types[i];
            KineType columnKineType = KineType.getByName(columnType);
            this.kineTypes[i] = columnKineType;

            Field field = new Field(columnName, columnType, 0, 0, columnKineType.getJdbcType(), columnKineType);
            this.fields[i] = field;
        }
        this.builtIndexMapping = true;
        log.debug("ResultData buildIndexMapping done :{}", this);
    }

    public Map<String, Integer> getColumnNameToIndex() {
        return this.columnNameToIndex;
    }

    public int findColumn(String columnName, int indexBase) {
        if (!this.builtIndexMapping) {
            this.buildIndexMapping();
        }

        Integer index = this.columnNameToIndex.get(columnName);
        if (index != null) {
            return index + indexBase;
        } else {

            for (int i = 0; i < this.rowNames.length; ++i) {
                if (this.rowNames[i].equalsIgnoreCase(columnName)) {
                    return i + indexBase;
                }
            }

            return -1;
        }
    }

    public boolean isEmpty() {
        return this.rowValues.length == 0;
    }

    public int size() {
        return this.rowValues.length;
    }

    public int getAffectLine() {
        if (this.rowValues != null && this.rowValues.length > 0) {
            Object[] affectLineOb = this.rowValues[0];
            int affectLine = Integer.valueOf(affectLineOb[0].toString());
            return affectLine;

        }
        return 0;
    }

    public void setCurrentRow(int rowNumber) throws SQLException {
        throw SqlError.newSQLException("Operation not allowed for a result set of type ResultSet.TYPE_FORWARD_ONLY.");
    }

    public Object[] get(int row) {
        return this.rowValues[row];

    }


    @Override
    public String toString() {
        return "ResultData{" +
                "rowNames=" + Arrays.toString(rowNames) +
                ", types=" + Arrays.toString(types) +
                ", kineTypes=" + Arrays.toString(kineTypes) +
                ", rowValues=" + Arrays.toString(rowValues) +
                ", fields=" + Arrays.toString(fields) +
                ", columnNameToIndex=" + columnNameToIndex +
                ", builtIndexMapping=" + builtIndexMapping +
                '}';
    }

    public String[] getRowNames() {
        return rowNames;
    }

    public void setRowNames(String[] rowNames) {
        this.rowNames = rowNames;
    }

    public String[] getTypes() {
        return types;
    }

    public void setTypes(String[] types) {
        this.types = types;
    }

    public KineType[] getKineTypes() {
        return kineTypes;
    }

    public void setKineTypes(KineType[] kineTypes) {
        this.kineTypes = kineTypes;
    }

    public Object[][] getRowValues() {
        return rowValues;
    }

    public void setRowValues(Object[][] rowValues) {
        this.rowValues = rowValues;
    }

    public Field[] getFields() {
        return fields;
    }

    public void setFields(Field[] fields) {
        this.fields = fields;
    }
}
