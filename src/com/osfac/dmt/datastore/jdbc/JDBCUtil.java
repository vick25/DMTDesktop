package com.osfac.dmt.datastore.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Utilities for JDBC.
 *
 * @author Martin Davis
 * @version 1.0
 */
public class JDBCUtil {

    public static void execute(Connection conn, String sql, ResultSetBlock block) {
        try {
            try (Statement statement = conn.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {
                block.yield(resultSet);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
