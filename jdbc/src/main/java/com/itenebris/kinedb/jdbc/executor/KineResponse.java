package com.itenebris.kinedb.jdbc.executor;

public class KineResponse {
    private Integer code;
    private String message;
    private KineData data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public KineData getData() {
        return data;
    }

    public void setData(KineData data) {
        this.data = data;
    }
}
