package com.itenebris.kinedb.jdbc.statement;

public interface Query {

    int getResultFetchSize();

    void setResultFetchSize(int var1);

    Resultset.Type getResultType();

    void setResultType(Resultset.Type resultType);

    String getCurrentDatabase();

    void setCurrentDatabase(String currentDatabase);
}
