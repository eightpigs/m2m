package io.eightpigs.m2m.util;

import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;

/**
 * get logger.
 *
 * @author eightpigs <eightpigs@outlook.com>
 * @date 2019-03-30
 */
public class LoggerUtils {

    public static Logger get(Class c) {
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new LoggerFormatter());
        Logger logger = Logger.getLogger(c.getName());
        logger.setUseParentHandlers(false);
        logger.addHandler(consoleHandler);
        return logger;
    }
}
