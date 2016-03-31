package com.tbis;

import ch.qos.logback.classic.Level;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootApplication
class TbisSpringAppApplication implements CommandLineRunner{

    private static final Logger logger = LoggerFactory.getLogger(TbisSpringAppApplication.class);
    private static final DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.s");

    @Autowired
    public JdbcTemplate jdbcTemplate;

    public static void main(String[] args) {
        setLoggingLevel(Level.TRACE);

        ApplicationContext ctx = SpringApplication.run(TbisSpringAppApplication.class, args);

        logger.debug("Listing beanDefinitionNames: ");
        for(String beanName: ctx.getBeanDefinitionNames()) {
            logger.debug("   " + beanName);
        }
    }

    private static void setLoggingLevel(ch.qos.logback.classic.Level level) {
        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
        root.setLevel(level);
    }

    @Override
    public void run(String... strings) throws Exception {

        logger.debug("Creating table in DB");

        jdbcTemplate.execute("DROP TABLE tide_data IF EXISTS");
        jdbcTemplate.execute("CREATE TABLE tide_data(" +
                "id SERIAL, " +
                "date_time timestamp, " +
                "tide_height double precision, " +
                "location_id integer)");

        logger.debug("Populating table in DB");

//        String exampleTideDataRow = "8,Fr,1,2016,0:28,0.8,6:59,2.9,12:52,0.9,19:06,2.9";
        String filePath = new File("").getAbsolutePath();
        logger.debug("Default filePath = " + filePath);
        List<Object[]> parsedTideData = new ArrayList<>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(filePath + "/src/main/resources/Auckland 2016.csv"));
            String line;
            // skip first three lines for header
            reader.readLine();
            reader.readLine();
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                List<Object[]> parsedLine = TideDataRowMapper.parseTideDataStringToListArray(line);
                parsedTideData.addAll(parsedLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeQuietly(reader);
        }

        jdbcTemplate.batchUpdate("INSERT INTO tide_data(date_time, tide_height, location_id)" +
                "VALUES (?,?,?)", parsedTideData);

        TideData mostRecentTideData = findMostRecentTideData(DateTime.now());

        logger.debug("mostRecentTideData = " + mostRecentTideData);
    }

    private void closeQuietly(Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (IOException ignored) {}
        }
    }
    TideData findMostRecentTideData(DateTime dateTime) {
        TideData returnedData = null;

        logger.debug("Querying for tide_data records where date_time is before ?", new Object[]{new Timestamp(dateTime.getMillis())});

        Object returnString = jdbcTemplate.queryForObject("SELECT * FROM tide_data WHERE date_time < ? ORDER BY date_time DESC LIMIT 1", new Object[]{fmt.print(dateTime)}, new TideDataRowMapper());
        logger.info("returnString = " + returnString);
        if(returnString instanceof TideData) {
             returnedData = (TideData) returnString;
        } else {
            logger.error("returnedString is not instance of TideData");
        }
        return returnedData;
    }

    List<TideData> findTideDataWithinTimeSpan(DateTime startDateTime, DateTime endDateTime) {

        List<Map<String, Object>> returnedObjectMap = jdbcTemplate.queryForList(
                "SELECT * FROM tide_data WHERE date_time BETWEEN ? and ? ORDER BY date_time ASC",
                new Object[]{fmt.print(startDateTime), fmt.print(endDateTime)}
        );

        List<TideData> returnedData = new ArrayList<>();
        for(Map row:returnedObjectMap) {
            TideData td = TideDataRowMapper.mapRowToTideData(new TideData(), row);
            returnedData.add(td);
        }

        return returnedData;
    }

    private TideData findByTideDataId(int tideDataId) {
        String sql = "SELECT * FROM tide_data WHERE tide_data.id = ?";
        return (TideData) jdbcTemplate.queryForObject(sql, new Object[] {tideDataId}, new TideDataRowMapper());
    }

}
