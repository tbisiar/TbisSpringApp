package com.tbis;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller to access tide data
 */
@RestController
@RequestMapping(value = "/tideData", method = RequestMethod.GET)
public class TideDataController {

    @Autowired
    TbisSpringAppApplication app;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index() {
        return "Greetings from Spring Boot!";
    }

    @ResponseBody
    @RequestMapping(value = "/mostRecent", method = RequestMethod.GET)
    public TideData loadMostRecentTideData() {
        TideData tideData = app.findMostRecentTideData(DateTime.now());
        return tideData;
    }

    //    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/next24Hours", method = RequestMethod.GET)
    public List<TideData> loadNext24HourTideData() {
        DateTime now = new DateTime();
        DateTime twentyFourHoursFromNow = now.plusDays(1);
        List<TideData> tideDataList = app.findTideDataWithinTimeSpan(now, twentyFourHoursFromNow);
        return tideDataList;
    }
}