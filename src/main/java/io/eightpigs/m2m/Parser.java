package io.eightpigs.m2m;

import io.eightpigs.m2m.database.IDatabase;
import io.eightpigs.m2m.model.config.Class;
import io.eightpigs.m2m.model.config.Config;
import io.eightpigs.m2m.model.config.Package;
import io.eightpigs.m2m.model.config.Property;
import io.eightpigs.m2m.model.db.Column;
import io.eightpigs.m2m.model.db.Table;
import io.eightpigs.m2m.util.StringUtils;
import org.yaml.snakeyaml.Yaml;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    /**
     * Wildcard symbol in configuration.
     */
    private static final String WILDCARD = "*";

    /**
     * Table name separator.
     */
    private static final String TABLE_NAME_SEPARATOR = "_";

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

    private Config loadConfig() throws Exception {
        byte[] bytes = Files.readAllBytes(configFilePath);
        Yaml yaml = new Yaml();
        return yaml.loadAs(new String(bytes), Config.class);
    }

    public void process() throws Exception {
        Config config = loadConfig();
        IDatabase db = Vars.DATABASE_MAP.get(config.getDatabase().getType());
        List<Table> tables = db.getTables(config.getDatabase(), config.getTables());
        if (tables.size() > 0) {
            List<ClassInfo> classInfos = merge(tables, config, db);
            for (ClassInfo classInfo : classInfos) {
                String content = classInfo.toString();
                // @TODO Write to file.
            }
        }
    }

    /**
     * Merge database information (tables, columns) and configuration information.
     *
     * @param tables All tables that need to be parsed.
     * @param config Configuration information in the configuration file.
     * @param db     database instance.
     * @return Merge completed class information.
     * @throws Exception If there is no global configuration information (WILDCARD config).
     */
    private List<ClassInfo> merge(List<Table> tables, Config config, IDatabase db) throws Exception {
        Map<String, Class> classMap = Arrays.stream(config.getClasses()).collect(Collectors.toMap(Class::getTableName, Function.identity()));
        List<ClassInfo> classInfos = new ArrayList<>();
        for (Table table : tables) {
            Class classConfig = (Class) getConfig(table.getName(), classMap);
            if (classConfig == null) {
                throw new Exception("The class configuration information corresponding to the table was not found.");
            } else {
                Map<String, Property> propertyMap = classConfig.getProperties().stream().collect(Collectors.toMap(Property::getColumnName, Function.identity()));
                ClassInfo classInfo = new ClassInfo(classConfig, table, new ArrayList<>());
                // get table corresponding package name.
                classInfo.setPackage(getPackage(table, config.getPackage()));
                classInfo.setClassName(
                    classConfig.getClassName() == null || classConfig.getClassName().trim().equals(WILDCARD) ? StringUtils.upperCamelCase(table.getName()) : classConfig.getClassName()
                );
                classInfos.add(classInfo);
                for (Column column : table.getColumns()) {
                    Property propertyConfig = (Property) getConfig(column.getName(), propertyMap);
                    String[] typeAndImport = db.getTypeAndImport(column);
                    PropertyInfo propertyInfo = new PropertyInfo(propertyConfig, column, typeAndImport);
                    propertyInfo.setPropertyName(
                        propertyConfig.getPropertyName() == null || propertyConfig.getPropertyName().trim().equals(WILDCARD) ? StringUtils.lowerCamelCase(column.getName()) : propertyConfig.getPropertyName()
                    );
                    classInfo.getProperties().add(propertyInfo);
                }
            }
        }
        return classInfos;
    }


    /**
     * Get a new package name based on the table name and the definition of the package grouping in the configuration file.
     * format: basePackage + "." + packageGroup.get(tableName.split("_")[0]).
     *
     * @param table         table info.
     * @param packageConfig Package configuration information.
     * @return Table corresponding package name.
     */
    private String getPackage(Table table, Package packageConfig) {
        String[] names = table.getName().split(TABLE_NAME_SEPARATOR);
        Map<String, String> group = packageConfig.getGroup();
        if (group != null && group.size() > 0) {
            if (group.containsKey(names[0])) {
                return packageConfig.getBase() + "." + group.get(names[0]);
            }
        }
        return packageConfig.getBase();
    }

    /**
     * Obtain the corresponding configuration information by table / column name.
     *
     * @param name table name / column name.
     * @param map  config map.
     * @return Class / Property configuration information or global configuration (wildcard) corresponding to the table / column name.
     */
    private Object getConfig(String name, Map map) {
        return map.containsKey(name) ? map.get(name) : map.get(WILDCARD);
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

    /**
     * Get code style indented strings.
     *
     * @param config config info.
     * @return Indented string
     */
    private String indent(Config config) {
        if (Vars.INDENT_STYLES.containsKey(config.getStyle().getIndentStyle())) {
            return Vars.INDENT_STYLES.get(config.getStyle().getIndentStyle()).apply(config.getStyle().getIndent());
        }
        return Vars.INDENT_STYLES.get("space").apply(4);
    }
}


class ClassInfo {
    private Class config;
    private Table table;
    private String className;
    private String _package;
    private List<PropertyInfo> properties;

    public ClassInfo(Class config, Table table, List<PropertyInfo> properties) {
        this.config = config;
        this.table = table;
        this.properties = properties;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        // @TODO: class to string -> .java file content
        return "";
    }

    public Class getConfig() {
        return config;
    }

    public Table getTable() {
        return table;
    }

    public List<PropertyInfo> getProperties() {
        return properties;
    }

    public String getPackage() {
        return _package;
    }

    public void setPackage(String _package) {
        this._package = _package;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}

class PropertyInfo {

    private Property config;

    private Column column;

    private String propertyName;

    private String[] javaTypeAndImport;

    public PropertyInfo(Property config, Column column, String[] javaTypeAndImport) {
        this.config = config;
        this.column = column;
        this.javaTypeAndImport = javaTypeAndImport;
    }

    public Property getConfig() {
        return config;
    }

    public Column getColumn() {
        return column;
    }

    public String[] getJavaTypeAndImport() {
        return javaTypeAndImport;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }
}
