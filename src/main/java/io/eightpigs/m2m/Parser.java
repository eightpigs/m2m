package io.eightpigs.m2m;

import com.intellij.openapi.util.io.StreamUtil;
import io.eightpigs.m2m.database.IDatabase;
import io.eightpigs.m2m.model.config.Class;
import io.eightpigs.m2m.model.config.Package;
import io.eightpigs.m2m.model.config.*;
import io.eightpigs.m2m.model.db.Column;
import io.eightpigs.m2m.model.db.Table;
import io.eightpigs.m2m.model.parse.ClassInfo;
import io.eightpigs.m2m.model.parse.PropertyInfo;
import io.eightpigs.m2m.util.StringUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
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

    /**
     * source code dir.
     */
    private static final String sourceDir = "src/main/java";

    /**
     * default author in java doc.
     */
    private static final String DEFAULT_AUTHOR = "m2m generator";
    /**
     * default create date in java doc.
     */
    private static final String DEFAULT_DATE = "${datetime}";

    /**
     * The name of the template, defined in the order in which the content is populated.
     */
    private static final String[] TEMPLATE_NAMES = new String[]{"class.vm", "property.vm", "constructor.vm", "xetter.vm"};

    private static final String fullConstructor = "full";
    private static final String emptyConstructor = "empty";

    private static Map<String, Boolean> DEFAULT_CONSTRUCTORS = new HashMap<String, Boolean>() {
        {
            put(fullConstructor, true);
            put(emptyConstructor, true);
        }
    };

    /**
     * default property config.
     */
    private Property DEFAULT_PROPERTY_CONFIG = new Property(WILDCARD, null, true, true, true, Collections.emptyList(), new ArrayList<>(1));

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
        if (config.getDatabase() == null) {
            throw new Exception("The configuration of the database cannot be empty.");
        }

        checkConfig(config);
        IDatabase db = Vars.DATABASE_MAP.get(config.getDatabase().getType());
        List<Table> tables = db.getTables(config.getDatabase(), config.getTables());

        if (tables != null && tables.size() > 0) {
            List<ClassInfo> classInfos = merge(tables, config, db);
            for (ClassInfo classInfo : classInfos) {
                Path path = Paths.get(getClassFilePath(classInfo));
                Files.write(path, parse(classInfo).getBytes());
            }
        }
    }

    private void checkConfig(Config config) {
        if (config.getInfo() == null) {
            config.setInfo(new Info(DEFAULT_AUTHOR, Vars.exec(DEFAULT_DATE, Vars.DEFAULT_DATE_FORMAT)));
        } else {
            if (config.getInfo().getAuthor() == null || config.getInfo().getAuthor().trim().length() == 0) {
                config.getInfo().setAuthor(DEFAULT_AUTHOR);
            }

            if (config.getInfo().getDate() == null || config.getInfo().getDate().trim().length() == 0) {
                config.getInfo().setDate(DEFAULT_DATE);
            }
            config.getInfo().setDate(Vars.exec(config.getInfo().getDate(), "yyyy-MM-dd HH:mm:ss"));
        }

        if (config.getClasses() == null) {
            config.setClasses(new Class[]{
                new Class(WILDCARD, null, true, DEFAULT_CONSTRUCTORS, Collections.emptyList(), Collections.emptyList(), Collections.emptyList())
            });
        }

        if (config.getClasses().length > 1) {
            Arrays.stream(config.getClasses()).parallel().forEach(c -> {
                c.setAnnotations(c.getAnnotations() == null ? Collections.emptyList() : c.getAnnotations());
                c.setImports(c.getImports() == null ? new ArrayList<>(5) : c.getImports());
                c.setProperties(c.getProperties() == null ? new ArrayList<>(10) : c.getProperties());
                c.setComment(c.getComment() == null ? true : c.getComment());
                c.setConstructors(c.getConstructors() == null ? DEFAULT_CONSTRUCTORS : c.getConstructors());
            });
        }
    }

    /**
     * Gets the file path of the class.
     * If the folder does not exist, create it.
     *
     * @param classInfo class parse info.
     * @return file path of the class.
     */
    private String getClassFilePath(ClassInfo classInfo) {
        String fileName = StringUtils.upperCamelCase(classInfo.getClassName()) + ".java";
        String dir = basePath + "/" + sourceDir + "/" + classInfo.getPackage().replace(".", "/") + "/";
        Path path = Paths.get(dir);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return dir + fileName;
    }

    /**
     * Parses the template and returns a string.
     *
     * @param classInfo class parse info.
     * @return parsed result.
     */
    private String parse(ClassInfo classInfo) {
        VelocityContext ctx = new VelocityContext();
        ctx.put("info", classInfo);
        ctx.put("indent", indent(classInfo.getConfig()));
        StringWriter writer = new StringWriter();
        for (String name : TEMPLATE_NAMES) {
            Velocity.evaluate(ctx, writer, "parse", loadTemplate(name));
        }
        writer.append("}");
        return writer.toString();
    }

    private String loadTemplate(String name) {
        InputStream stream = this.getClass().getResourceAsStream("/templates/" + name);
        try {
            return StreamUtil.readText(stream, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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

                ClassInfo classInfo = new ClassInfo();
                classInfo.setImports(new ArrayList<>(classConfig.getImports()));
                classInfo.setProperties(new ArrayList<>());
                // get table corresponding package name.
                classInfo.setPackage(getPackage(table, config.getPackage()));
                classInfo.setConfig(config);
                classInfo.setClassConfig(classConfig);
                classInfo.setFullConstructor(false);
                classInfo.setTable(table);
                classInfo.setEmptyConstructor(false);
                if (classConfig.getConstructors().containsKey(fullConstructor)) {
                    classInfo.setFullConstructor(classConfig.getConstructors().get(fullConstructor));
                }
                if (classConfig.getConstructors().containsKey(emptyConstructor)) {
                    classInfo.setEmptyConstructor(classConfig.getConstructors().get(emptyConstructor));
                }
                classInfo.setClassName(
                    classConfig.getClassName() == null || classConfig.getClassName().trim().equals(WILDCARD) ? StringUtils.upperCamelCase(table.getName()) : classConfig.getClassName()
                );
                if (classConfig.getAnnotations() != null && classConfig.getAnnotations().size() > 0) {
                    classInfo.setAnnotations(new ArrayList<>());
                    for (String anno : classConfig.getAnnotations()) {
                        classInfo.getAnnotations().add(Vars.exec(anno, table));
                    }
                }
                classInfos.add(classInfo);

                for (Column column : table.getColumns()) {
                    Property propertyConfig = (Property) getConfig(column.getName(), propertyMap);
                    if (propertyConfig == null) {
                        propertyConfig = DEFAULT_PROPERTY_CONFIG;
                    }
                    String[] typeAndImport = db.getTypeAndImport(column);
                    PropertyInfo propertyInfo = new PropertyInfo(propertyConfig, column, typeAndImport);
                    propertyInfo.setPropertyName(
                        propertyConfig.getPropertyName() == null || propertyConfig.getPropertyName().trim().equals(WILDCARD) ? StringUtils.lowerCamelCase(column.getName()) : propertyConfig.getPropertyName()
                    );
                    propertyInfo.setUpperCaseName(StringUtils.upperCamelCase(propertyInfo.getPropertyName()));
                    if (typeAndImport.length > 1) {
                        classInfo.getImports().add(typeAndImport[1]);
                    }
                    if (propertyConfig.getImports() != null && propertyConfig.getImports().size() > 0) {
                        classInfo.getImports().addAll(propertyConfig.getImports());
                    }
                    classInfo.setImports(classInfo.getImports().stream().distinct().collect(Collectors.toList()));
                    propertyInfo.setLast(false);
                    if (propertyConfig.getAnnotations() != null && propertyConfig.getAnnotations().size() > 0) {
                        propertyInfo.setAnnotations(new ArrayList<>());
                        for (String anno : propertyConfig.getAnnotations()) {
                            propertyInfo.getAnnotations().add(Vars.exec(anno, column));
                        }
                    }
                    classInfo.getProperties().add(propertyInfo);
                }
                if (classInfo.getProperties().size() > 1) {
                    classInfo.getProperties().get(classInfo.getProperties().size() - 1).setLast(true);
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
        if (config.getStyle() != null && config.getStyle().getIndentStyle() != null) {
            String indentStyle = config.getStyle().getIndentStyle().trim();
            if (Vars.INDENT_STYLES.containsKey(indentStyle)) {
                return Vars.INDENT_STYLES.get(indentStyle).apply(config.getStyle().getIndent());
            }
        }
        return Vars.INDENT_STYLES.get("space").apply(4);
    }
}
