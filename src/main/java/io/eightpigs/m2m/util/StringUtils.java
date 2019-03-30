package io.eightpigs.m2m.util;

/**
 * The string utility class used in this project.
 *
 * @author eightpigs <eightpigs@outlook.com>
 * @date 2019-03-28
 */
public class StringUtils {

    /**
     * Gets a value that is not blank.
     *
     * @param value        Prepare the check value.
     * @param defaultValue default value.
     * @return Returns {@code value} if it is not empty, otherwise the {@code defaultValue} is returned.
     */
    public static String choose(String value, String defaultValue) {
        return value != null && value.trim().length() > 0 ? value : defaultValue;
    }

}
