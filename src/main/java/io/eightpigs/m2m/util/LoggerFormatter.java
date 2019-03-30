package io.eightpigs.m2m.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Custom log formatter.
 *
 * @author eightpigs <eightpigs@outlook.com>
 * @date 2019-03-30
 */
public class LoggerFormatter extends Formatter {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public String format(LogRecord record) {
        String message = record.getMessage();
        System.out.println(record.getMessage());
        return "[" + record.getLevel() + "] " +
            DATE_FORMAT.format(new Date()) +
            " " +
            record.getSourceClassName() + "#" + record.getSourceMethodName() + " " +
            message;
    }

    @Override
    public String getHead(Handler h) {
        return "";
    }

    @Override
    public String getTail(Handler h) {
        return "";
    }
}
