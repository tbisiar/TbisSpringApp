package com.tbis;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/*
 * A class to allow organization of tide data by time and height
 */
class TideData {

    private long id;

    private DateTime time;
    private float height;
    private int locationId;

    private static final DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.s");

    TideData(){}

    public TideData(long id, DateTime time, float height, int locationId) {
        this.id = id;
        this.time = time;
        this.height = height;
        this.locationId = locationId;
    }

    public TideData(String id, String dateTime, String height, String locationId) {
        this.id = Long.valueOf(id);
        this.time = new DateTime(dateTime);
        this.height = Float.valueOf(height);
        this.locationId = Integer.valueOf(locationId);
        // TODO: catch exceptions
    }

    public long getId() { return id; }

    public void setId(long id) { this.id = id; }

//    public DateTime getDateTime() { return time; }

    // Returning formatted string because couldn't get JsonFormat annotation to work :(
    public String getTime() { return fmt.print(time); }

    public void setTime(DateTime time) {
        this.time = time;
    }

    public Float getHeight() {
        return height;
    }

    public void setHeight(Float height) {
        this.height = height;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }
}