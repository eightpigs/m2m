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

    /**
     * Get the name using a Lower Camel Case.
     *
     * @param name original name.
     * @return Lower Camel Case.
     */
    public static String lowerCamelCase(String name) {
        String[] names = name.split("_");
        if (names.length > 1) {
            StringBuilder builder = new StringBuilder();
            builder.append(names[0].toLowerCase());
            for (int i = 1; i < names.length; i++) {
                builder.append(names[i].substring(0, 1).toUpperCase());
                builder.append(names[i].substring(1));
            }
            return builder.toString();
        }
        return name;
    }

    /**
     * Get the name using a Upper Camel Case.
     *
     * @param name original name.
     * @return Upper Camel Case.
     */
    public static String upperCamelCase(String name) {
        String lowerCamelCase = lowerCamelCase(name);
        return lowerCamelCase.substring(0, 1).toUpperCase() + lowerCamelCase.substring(1);
    }

}
