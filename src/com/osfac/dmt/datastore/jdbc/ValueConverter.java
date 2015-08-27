package com.osfac.dmt.datastore.jdbc;

import com.osfac.dmt.feature.AttributeType;
import java.sql.ResultSet;

/**
 * An interface for objects which can transform columns from ResultSets into JUMP data types
 */
public interface ValueConverter {

    AttributeType getType();

    Object getValue(ResultSet rs, int column) throws Exception;
}
