package com.itenebris.kinedb.jdbc;

import com.itenebris.kinedb.jdbc.connection.KineConnection;
import com.itenebris.kinedb.jdbc.executor.ExecutorParams;
import com.itenebris.kinedb.jdbc.executor.Kine;
import com.itenebris.kinedb.jdbc.executor.KineData;
import com.itenebris.kinedb.jdbc.result.ResultData;
import org.junit.Test;

import java.util.Iterator;

public class ConnectionTest extends BaseTest{

    String sql = "select * from tpch1g.customer limit 10000";

    @Test
    public void testExecuteSql() throws Exception {
        KineConnection kineConnection = (KineConnection) con;
        ExecutorParams executorParams = new ExecutorParams();
        ResultData data = kineConnection.getExecutor().executeSql(sql, executorParams);
        System.out.println("testExecuteSql data = " + data);
    }

    @Test
    public void testStreamExecuteSql() throws Exception {
        KineConnection kineConnection = (KineConnection) con;
        ExecutorParams executorParams = new ExecutorParams();
        Iterator<Kine.Results>  resultsIterator = kineConnection.getExecutor().streamExecuteSql(sql, executorParams);
        int  i = 0;
        while (resultsIterator.hasNext()) {
            Kine.Results results = resultsIterator.next();
            System.out.println("results rows : " + results.getRowsCount());
            i += results.getRowsCount();
        }
        System.out.println("------------------------- testStreamExecuteSql total rows = " + i);
    }
}
