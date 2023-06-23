package com.itenebris.kinedb.jdbc;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class StatementTest2 extends BaseTest2{

    Logger log = LoggerFactory.getLogger(this.getClass());

//    @Override
//    public void setUp() throws Exception {
//        con = TestUtil.openDBWithDatabase();
//        //TestUtil.createTable(con, "jdbc_demo1", "id bigint primary key, name varchar(20) not null, age integer");
//        System.out.println("StatementTest2 setUp done");
//
//    }
//
//    @Override
//    public void tearDown() throws SQLException {
//
//        //TestUtil.dropTable(con, "jdbc_demo1");
//
//        con.close();
//        System.out.println("StatementTest2 tearDown done");
//    }
    @Test
    public void createTable() throws SQLException {
        TestUtil.createTable(con, "jdbc_demo1", "id bigint primary key not null, name varchar(20) not null, age integer");
        System.out.println("createTable done");
    }

    @Test
    public void dropTable() throws SQLException {
        TestUtil.dropTable(con, "jdbc_demo2");
        System.out.println("dropTable done");
    }

    @Test
    public void testInsert() throws SQLException{
        Statement stmt = con.createStatement();
        int result1 = stmt.executeUpdate(TestUtil.insertSQL("jdbc_demo1", "id,name,age", "10000, 'name1', 10"));
        int result2 = stmt.executeUpdate(TestUtil.insertSQL("jdbc_demo1", "id,name,age", "20000, 'name2', 12"));

        System.out.println("testInsert result1 " + result1);
        System.out.println("testInsert result2 " + result2);
        stmt.close();
    }

    @Test
    public void testQuery() throws SQLException{
        Statement stmt = con.createStatement();
        ResultSet result1 = stmt.executeQuery(TestUtil.selectSQL("t_user", "id,name,age", null));
        //ResultSet result2 = stmt.executeQuery(TestUtil.selectSQL("jdbc_demo1", "id,name,age", "id > 1"));
        //ResultSet result3 = stmt.executeQuery(TestUtil.selectSQL("jdbc_demo1", "count(*)", null));

        System.out.println("testQuery result1 " + result1);
        //System.out.println("testQuery result2 " + result2);
        //System.out.println("testQuery result3 " + result3);

        while (result1.next()) {
            long id = result1.getLong(1);
            String name = result1.getString("name");
            int age = result1.getInt(3);
            System.out.println("testQuery result1 id: " + id + " name: " + name + " age: " + age);
        }
        stmt.close();
    }

    @Test
    public void testQuery2() throws SQLException{
        Statement stmt = con.createStatement();
        ResultSet result1 = stmt.executeQuery(TestUtil.selectSQL("jdbc_demo1", "id,name,age", null));

        System.out.println("testQuery2 result1 " + result1);

        while (result1.next()) {
            long id = result1.getLong(1);
            String name = result1.getString("name");
            int age = result1.getInt(3);
            System.out.println("testQuery2 result1 id: " + id + " name: " + name + " age: " + age);
        }
        stmt.close();
    }

    @Test
    public void testUpdate() throws SQLException{
        Statement stmt = con.createStatement();

        String update = "update jdbc_demo1 set name = 'name1111',age = 20 where id = 10000";
        int result1 = stmt.executeUpdate(update);
        System.out.println("testUpdate result1 " + result1);
        stmt.close();
    }
    @Test
    public void testDelete() throws SQLException{
        Statement stmt = con.createStatement();

        String update = "delete from jdbc_demo1 where id = 10000";
        int result1 = stmt.executeUpdate(update);
        String update2 = "delete from jdbc_demo1 where id = 20000";
        int result2 = stmt.executeUpdate(update2);
        System.out.println("testDelete result1 " + result1);
        System.out.println("testDelete result2 " + result2);
        stmt.close();
    }


}
