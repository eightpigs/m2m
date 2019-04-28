package io.eightpigs.m2m.database.impl;

import io.eightpigs.m2m.database.IDatabase;
import io.eightpigs.m2m.model.config.Database;
import io.eightpigs.m2m.model.db.Column;
import io.eightpigs.m2m.model.db.Table;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * MySQL database operation implementation.
 *
 * @author eightpigs <eightpigs@outlook.com>
 * @date 2019-03-30
 */
public class MySQL implements IDatabase {

    private static Connection conn;

    private static final String connectStrTmpl = "jdbc:mysql://{host}:{port}/{name}?{params}";

    private static Map<String, Function<Integer, String[]>> typeMap;


    /**
     * mysql type -> java type.
     * <p>
     * {@see https://dev.mysql.com/doc/connector-j/8.0/en/connector-j-reference-type-conversions.html}
     *
     * @TODO UNSIGNED
     */
    public MySQL() {
        Function<Integer, String[]> bit = (len) -> len == 1 ? new String[]{"Boolean"} : new String[]{"byte[]"};
        Function<Integer, String[]> tinyint = (len) -> new String[]{"Integer"};
        Function<Integer, String[]> _int = (len) -> new String[]{"Integer"};
        Function<Integer, String[]> timestamp = (len) -> new String[]{"Timestamp", "java.sql.Timestamp"};
        Function<Integer, String[]> string = (len) -> new String[]{"String"};
        Function<Integer, String[]> byteArray = (len) -> new String[]{"byte[]"};
        Function<Integer, String[]> date = (len) -> new String[]{"Date", "java.sql.Date"};

        typeMap = new HashMap<String, Function<Integer, String[]>>() {{
            put("BIT", bit);

            put("TINYINT", tinyint);
            put("BOOL", tinyint);
            put("BOOLEAN", tinyint);

            put("SMALLINT", _int);
            put("MEDIUMINT", _int);
            put("INT", _int);
            put("INTEGER", _int);

            put("BIGINT", (len) -> new String[]{"LONG"});
            put("FLOAT", (len) -> new String[]{"Float"});
            put("DOUBLE", (len) -> new String[]{"Double"});
            put("DECIMAL", (len) -> new String[]{"BigDecimal", "java.math.BigDecimal"});

            put("DATE", date);

            put("DATETIME", timestamp);
            put("TIMESTAMP", timestamp);

            put("TIME", (len) -> new String[]{"Time", "java.sql.Time"});

            // Use the "Short" directly.
            // Official Manuals: If yearIsDateType configuration property is set to false,
            // then the returned object type is java.sql.Short. If set to true (the default),
            // then the returned object is of type java.sql.Date with the date set to January 1st, at midnight.
            put("YEAR", (len) -> new String[]{"Short"});

            put("CHAR", string);
            put("VARCHAR", string);
            put("TINYTEXT", string);
            put("TEXT", string);
            put("MEDIUMTEXT", string);
            put("LONGTEXT", string);
            put("ENUM", string);
            put("SET", string);

            put("BINARY", byteArray);
            put("VARBINARY", byteArray);
            put("TINYBLOB", byteArray);
            put("BLOB", byteArray);
            put("MEDIUMBLOB", byteArray);
            put("LONGBLOB", byteArray);
        }};

    }

    private String getConnectStr(Database database) {
        return connectStrTmpl
            .replace("{host}", database.getHost() == null ? "127.0.0.1" : database.getHost())
            .replace("{port}", database.getPort() == null ? "3306" : String.valueOf(database.getPort()))
            .replace("{name}", database.getName())
            .replace("{params}", database.getParams() == null ? "" : database.getParams());
    }

    private void connect(Database database) throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection(getConnectStr(database), database.getUser(), database.getPassword());
    }

    @Override
    public List<Table> getTables(Database database, List<String> specifyTables) {
        List<Table> tables = new ArrayList<>();
        try {
            connect(database);
            Map<String, String> tableMap = getSpecifyTableMap(specifyTables);
            DatabaseMetaData databaseMetaData = conn.getMetaData();
            ResultSet resultSet = databaseMetaData.getTables(database.getName(), null, "%", null);
            while (resultSet.next()) {
                String name = resultSet.getString("TABLE_NAME");
                // Check if the table name is specified.
                if (tableMap == null || tableMap.containsKey(name)) {
                    Table table = new Table(name, resultSet.getString("REMARKS"), new ArrayList<>());

                    // Get the columns of the table.
                    ResultSet columnResultSet = databaseMetaData.getColumns(database.getName(), "%", table.getName(), "%");
                    while (columnResultSet.next()) {
                        Column column = new Column(
                            columnResultSet.getString("COLUMN_NAME"),
                            columnResultSet.getString("TYPE_NAME"),
                            columnResultSet.getInt("COLUMN_SIZE"),
                            columnResultSet.getString("REMARKS")
                        );
                        table.getColumns().add(column);
                    }

                    tables.add(table);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tables;
    }

    @Override
    public String[] getTypeAndImport(Column column) {
        if (typeMap.containsKey(column.getType())) {
            return typeMap.get(column.getType()).apply(column.getLength());
        }
        return new String[0];
    }

    private Map<String, String> getSpecifyTableMap(List<String> specifyTables) {
        Map<String, String> tableMap = null;
        if (specifyTables != null && specifyTables.size() > 0) {
            tableMap = new HashMap<>(specifyTables.size());
            for (String i : specifyTables) {
                tableMap.put(i, null);
            }
        }
        return tableMap;
    }
}
