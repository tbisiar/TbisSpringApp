package com.tbis;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.jdbc.core.RowMapper;

import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Timestamp;

/**
 * Data mapper for serializing TideData from jdbc queries.
 */
class TideDataRowMapper implements RowMapper{

    private static final DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.s");

    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        TideData tideData = new TideData();
        tideData.setId(rs.getInt("id"));
        tideData.setHeight(rs.getFloat("tide_height"));
        Timestamp ts = rs.getTimestamp("date_time");
        tideData.setTime(DateTime.parse(ts.toString(), fmt));
        tideData.setLocationId(rs.getInt("location_id"));
        return tideData;
    }
}
