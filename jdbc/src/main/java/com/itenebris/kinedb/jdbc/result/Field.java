package com.itenebris.kinedb.jdbc.result;

import com.itenebris.kinedb.jdbc.KineType;

public class Field {
    private String columnName;
    private String kineTypeStr;
    private long length;

    // for decimal .. types
    private int scale;
    private int kineTypeId = -1;
    private KineType kineType;

    public Field(String columnName, String kineTypeStr, long length, int scale, int kineTypeId, KineType kineType) {
        this.columnName = columnName;
        this.kineType = kineType;
        this.kineTypeId = kineTypeId;
        this.kineTypeStr = kineTypeStr;
        this.length = length;
        this.scale = scale;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getKineTypeStr() {
        return kineTypeStr;
    }

    public void setKineTypeStr(String kineTypeStr) {
        this.kineTypeStr = kineTypeStr;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public int getKineTypeId() {
        return kineTypeId;
    }

    public void setKineTypeId(int kineTypeId) {
        this.kineTypeId = kineTypeId;
    }

    public KineType getKineType() {
        return kineType;
    }

    public void setKineType(KineType kineType) {
        this.kineType = kineType;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }
}
