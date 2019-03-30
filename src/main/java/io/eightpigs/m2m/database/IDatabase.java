package io.eightpigs.m2m.database;

import io.eightpigs.m2m.model.config.Database;
import io.eightpigs.m2m.model.db.Column;
import io.eightpigs.m2m.model.db.Table;

import java.util.List;

/**
 * Database operation interface.
 *
 * @author eightpigs <eightpigs@outlook.com>
 * @date 2019-03-30
 */
public interface IDatabase {

    /**
     * Returns the specified tables for the specified database.
     *
     * @param database      database config.
     * @param specifyTables specified tables
     * @return Specify instances of the tables.
     */
    List<Table> getTables(Database database, List<String> specifyTables);

    /**
     * Get the java type and imported package corresponding to the database column type.
     *
     * @param column column info.
     * @return [0]: type  [1]: import
     */
    String[] getTypeAndImport(Column column);
}
