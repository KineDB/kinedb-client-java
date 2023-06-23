package com.itenebris.kinedb.jdbc;

import org.junit.After;
import org.junit.Before;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class BaseTest2 {

    protected Connection con;

    @Before
    public void setUp() throws Exception {
        Properties props = new Properties();
        props.put("engine", "presto");
        con = TestUtil.openDBWithDatabase(props);

    }

    @After
    public void tearDown() throws SQLException {
        TestUtil.closeDB(con);
    }
}
