package io.eightpigs.m2m;

import io.eightpigs.m2m.database.IDatabase;
import io.eightpigs.m2m.database.impl.MySQL;
import io.eightpigs.m2m.model.db.Column;
import io.eightpigs.m2m.model.db.Table;
import io.eightpigs.m2m.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * All variables available in the configuration file and internally defined.
 *
 * @author eightpigs <eightpigs@outlook.com>
 * @date 2019-03-30
 */
public class Vars {

    /**
     * MySQL database.
     */
    public static final String DATABASE_TYPE_MYSQL = "mysql";

    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    private static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";

    public static final Map<String, Function<Object, String>> VARIABLES = new HashMap<>() {
        {
            put("date", (format) -> new SimpleDateFormat(StringUtils.choose(format.toString(), DEFAULT_DATE_FORMAT))
                .format(new Date()));

            put("datetime", (format) -> new SimpleDateFormat(StringUtils.choose(format.toString(), DEFAULT_DATETIME_FORMAT))
                .format(new Date()));

            put("time", (format) -> new SimpleDateFormat(StringUtils.choose(format.toString(), DEFAULT_TIME_FORMAT))
                .format(new Date()));

            put("tableName", (table) -> {
                if (table != null) {
                    return ((Table) table).getName();
                }
                return "";
            });

            put("fieldName", (field) -> {
                if (field != null) {
                    return ((Column) field).getName();
                }
                return "";
            });
        }
    };

    /**
     * indent style.
     */
    public static final Map<String, Function<Integer, String>> INDENT_STYLES = new HashMap<>() {{
        put("tab", "\t"::repeat);
        put("space", " "::repeat);
    }};

    /**
     * Implementation of different database operations.
     */
    public static final Map<String, IDatabase> DATABASE_MAP = new HashMap<>() {{
        put(DATABASE_TYPE_MYSQL, new MySQL());
    }};
}
