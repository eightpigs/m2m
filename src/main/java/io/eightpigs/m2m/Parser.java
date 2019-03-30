package io.eightpigs.m2m;

import io.eightpigs.m2m.database.IDatabase;
import io.eightpigs.m2m.model.config.Config;
import io.eightpigs.m2m.model.db.Column;
import io.eightpigs.m2m.model.db.Table;
import org.yaml.snakeyaml.Yaml;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * table to java object parser.
 * All the processing logic.
 * 1. connect to db
 * 2. get tables
 * 3. get fields
 * 4. assembly objects
 * 5. write to files
 *
 * @author eightpigs <eightpigs@outlook.com>
 * @date 2019-03-30
 */

public class Parser {

    /**
     * config file name.
     */
    private static final String configFileName = ".m2m.yaml";

    /**
     * the project base path.
     */
    private String basePath;

    /**
     * Full path to the configuration file.
     */
    private Path configFilePath;

    private Parser() {
    }

    /**
     * init parser.
     *
     * @param basePath the project path.
     * @return parser.
     */
    public static Parser init(String basePath) {
        if (basePath != null) {
            Parser parser = new Parser();
            parser.basePath = basePath;
            if (parser.configExists()) {
                return parser;
            }
        }
        return null;
    }

    private Config getConfig() throws Exception {
        byte[] bytes = Files.readAllBytes(configFilePath);
        Yaml yaml = new Yaml();
        return yaml.loadAs(new String(bytes), Config.class);
    }

    public void process() throws Exception {
        Config config = getConfig();
        IDatabase db = Vars.DATABASE_MAP.get(config.getDatabase().getType());
        List<Table> tables = db.getTables(config.getDatabase(), config.getTables());
        for (Table table : tables) {
            System.out.println(table.getName());
            for (Column column : table.getColumns()) {
                System.out.println(column.getName() + " - " + column.getType());
            }
        }

        // @TODO 1. get class
        // @TODO 2. merge config to class
        // @TODO 3. get java file content
    }

    /**
     * check {@value configFileName} exists.
     *
     * @return does it exist.
     */
    private boolean configExists() {
        configFilePath = Paths.get(basePath + "/" + configFileName);
        return Files.exists(configFilePath);
    }

}
