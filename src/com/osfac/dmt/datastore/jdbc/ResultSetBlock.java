package com.osfac.dmt.datastore.jdbc;

import java.sql.*;

public interface ResultSetBlock {

    void yield(ResultSet resultSet) throws Exception;
}
