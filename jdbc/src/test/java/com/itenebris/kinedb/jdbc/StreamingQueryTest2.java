package com.itenebris.kinedb.jdbc;

import org.junit.Test;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StreamingQueryTest2 extends BaseTest2{

    @Test
    public void testStreamingQuery() throws SQLException {
        PreparedStatement pstmt = con.prepareStatement("select * from tpch1g.lineitem limit 10000");
        pstmt.setFetchSize(1000);
        pstmt.setFetchDirection(ResultSet.FETCH_FORWARD);
        ResultSet rs = pstmt.executeQuery();
        int total = 0;
        while(rs.next()) {
            Object key = rs.getObject(1);
            System.out.println("testStreamingQuery key is = "+ key);
            total++;
        }
        System.out.println("testStreamingQuery total rows is = "+ total);
        rs.close();
        pstmt.close();
    }

    @Test
    public void testStreamingQueryJoin() throws SQLException {
        PreparedStatement pstmt = con.prepareStatement("select a.*,b.* from mysql100.user1 a join mysql100.address b on a.id = b.user_id");
        pstmt.setFetchSize(1000);
        pstmt.setFetchDirection(ResultSet.FETCH_FORWARD);
        ResultSet rs = pstmt.executeQuery();
        int total = 0;
        while(rs.next()) {
            Object key = rs.getObject(1);
            System.out.println("testStreamingQueryJoin key is = "+ key);
            total++;
        }
        System.out.println("testStreamingQueryJoin total rows is = "+ total);
        rs.close();
        pstmt.close();
    }

    @Test
    public void testStreamingQueryUnion() throws SQLException {
        PreparedStatement pstmt = con.prepareStatement("select a.id,a.name,a.age from mysql100.user1 a union select b.id,b.name,b.age from es_test.user1 b ");
        pstmt.setFetchSize(1000);
        pstmt.setFetchDirection(ResultSet.FETCH_FORWARD);
        ResultSet rs = pstmt.executeQuery();
        int total = 0;
        while(rs.next()) {
            Object key1 = rs.getObject(1);
            Object key2 = rs.getObject(2);
            Object key3 = rs.getObject(3);
            System.out.println("testStreamingQueryUnion key1 is = "+ key1);
            System.out.println("testStreamingQueryUnion key2 is = "+ key2);
            System.out.println("testStreamingQueryUnion key3 is = "+ key3);
            total++;
        }
        System.out.println("testStreamingQueryJoin total rows is = "+ total);
        rs.close();
        pstmt.close();
    }
}
