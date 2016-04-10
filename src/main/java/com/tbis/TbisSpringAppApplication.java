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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootApplication
class TbisSpringAppApplication implements CommandLineRunner{

    private static final Logger logger = LoggerFactory.getLogger(TbisSpringAppApplication.class);
    private static final DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    protected JdbcTemplate jdbcTemplate;


    public static void main(String[] args) {
        setLoggingLevel(Level.TRACE);

        ApplicationContext ctx = SpringApplication.run(TbisSpringAppApplication.class, args);

        logger.debug("Listing beanDefinitionNames: ");
        for(String beanName: ctx.getBeanDefinitionNames()) {
            logger.debug("   " + beanName);
        }
    }

    @Override
    public void run(String... strings) throws Exception {

        createTideDataTableInDatabase();
        List<Object[]> parsedTideData = parseTideDataFromFile("Auckland 2016.csv");
        populateTideDataTableInDatabase(parsedTideData);

        // Startup check - verify that we can read data from the db
        TideData mostRecentTideData = findMostRecentTideData(DateTime.now());
        logger.debug("mostRecentTideData = " + mostRecentTideData);
    }

    TideData findMostRecentTideData(DateTime dateTime) {
        return (TideData) jdbcTemplate.queryForObject(
                "SELECT * FROM tide_data WHERE date_time < ? ORDER BY date_time DESC LIMIT 1",
                new Object[]{fmt.print(dateTime)},
                new TideDataRowMapper()
        );
    }

    TideData findNextUpcomingTideData(DateTime dateTime) {
        return (TideData) jdbcTemplate.queryForObject(
                "SELECT * FROM tide_data WHERE date_time > ? ORDER BY date_time ASC LIMIT 1",
                new Object[]{fmt.print(dateTime)},
                new TideDataRowMapper()
        );
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

    TideData findByTideDataById(int tideDataId) {
        String sql = "SELECT * FROM tide_data WHERE tide_data.id = ?";
        TideData tideData = (TideData) jdbcTemplate.queryForObject(sql, new Object[] {tideDataId}, new TideDataRowMapper());
        return tideData;
    }

    public static void setLoggingLevel(ch.qos.logback.classic.Level level) {
        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
        root.setLevel(level);
    }

    private void closeQuietly(Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (IOException ignored) {}
        }
    }

    private void createTideDataTableInDatabase() {
        jdbcTemplate.execute("DROP TABLE tide_data IF EXISTS");
        jdbcTemplate.execute("CREATE TABLE tide_data(" +
                "id SERIAL, " +
                "date_time timestamp, " +
                "tide_height double precision, " +
                "location_id integer)");
    }

    private List<Object[]> parseTideDataFromFile(String fileName) {
        List<Object[]> parsedTideData = new ArrayList<>();
//        String exampleTideDataRow = "8,Fr,1,2016,0:28,0.8,6:59,2.9,12:52,0.9,19:06,2.9";
        String filePath = new File("").getAbsolutePath();
        logger.debug("Default filePath = " + filePath);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(filePath + "/src/main/resources/" + fileName));
            String line = null;
            // skip first three lines for header
            reader.readLine();
            reader.readLine();
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                List<Object[]> parsedLine = TideDataRowMapper.parseTideDataStringToTideData(line);
                parsedTideData.addAll(parsedLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeQuietly(reader);
        }
        return parsedTideData;
    }

    private void populateTideDataTableInDatabase(List<Object[]> parsedTideData) {
        jdbcTemplate.batchUpdate("INSERT INTO tide_data(date_time, tide_height, location_id)" +
                "VALUES (?,?,?)", parsedTideData);
    }
}
