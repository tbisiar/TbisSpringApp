package com.tbis;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.jdbc.core.RowMapper;

import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public static TideData mapRowToTideData(TideData td, Map row) {
        td.setId(Long.parseLong(row.get("id").toString()));
        td.setHeight(Float.parseFloat(row.get("tide_height").toString()));
        td.setLocationId(Integer.valueOf(row.get("location_id").toString()));
        td.setTime(DateTime.parse(row.get("date_time").toString(), fmt));
        return td;
    }

    protected static List<Object[]> parseTideDataStringToTideData(String tideDataString) {

        List<Object[]> dateTimeAttributeList = new ArrayList<>();

        String[] splitString = tideDataString.split(",");

        // Construct date without time, this is overkill for now,
        // but still need to establish best practice for inputting data
        DateTime dateTime = new DateTime(
                Integer.valueOf(splitString[3]),
                Integer.valueOf(splitString[2]),
                Integer.valueOf(splitString[0]),
                0,0,0
        );

        for(int i=4; i<splitString.length; i+=2) {
            // Parse date
            String[] hourMin = splitString[i].split(":");
            DateTime tideDateTime = dateTime
                    .withMinuteOfHour(
                            Integer.parseInt(hourMin[1])
                    ).withHourOfDay(
                            Integer.parseInt(hourMin[0])
                    );

            String[] stringArray = new String[3];
            stringArray[0] = tideDateTime.toString();
            stringArray[1] = splitString[i+1];
            stringArray[2] = "64000";
            dateTimeAttributeList.add(stringArray);
        }
        return dateTimeAttributeList;
    }

}
