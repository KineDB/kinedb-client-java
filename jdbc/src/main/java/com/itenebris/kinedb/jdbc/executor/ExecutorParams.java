package com.itenebris.kinedb.jdbc.executor;

public class ExecutorParams {


    public ExecutorParams() {
    }

    public ExecutorParams(int fetchSize, String defaultDatabase, String engine) {
        this.fetchSize = fetchSize;
        this.defaultDatabase = defaultDatabase;
        this.engine = engine;
    }

    private int fetchSize;
    private String defaultDatabase;

    private String engine;

    public int getFetchSize() {
        return fetchSize;
    }

    public void setFetchSize(int fetchSize) {
        this.fetchSize = fetchSize;
    }

    public String getDefaultDatabase() {
        return defaultDatabase;
    }

    public void setDefaultDatabase(String defaultDatabase) {
        this.defaultDatabase = defaultDatabase;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    @Override
    public String toString() {
        return "ExecutorParams{" +
                "fetchSize=" + fetchSize +
                ", defaultDatabase='" + defaultDatabase + '\'' +
                ", engine='" + engine + '\'' +
                '}';
    }
}
