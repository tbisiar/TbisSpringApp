package com.tbis;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller to access tide data
 */
@RestController
public class TideDataController {

    @Autowired
    TbisSpringAppApplication app;

    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

    @ResponseBody
    @RequestMapping("/mostRecent")
    public TideData loadMostRecentTideData() {
        TideData tideData = app.findMostRecentTideData(DateTime.now());
        return tideData;
    }

}