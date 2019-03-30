package io.eightpigs.m2m.model.config;

import java.util.List;
import java.util.Map;

/**
 * class config.
 *
 * @author eightpigs <eightpigs@outlook.com>
 * @date 2019-03-30
 */
public class Class {

    /**
     * The corresponding table name, if *, represents all the tables that need to be generated.
     */
    private String tableName;

    /**
     * The generated class name, if empty, by default hump style.
     */
    private String className;

    /**
     * Whether to use the comment of the table.
     */
    private Boolean comment;

    /**
     * Whether to generate a full parameter or an empty constructor.
     */
    private Map<String, Boolean> constructors;

    /**
     * Custom annotation.
     */
    private List<String> annotations;

    /**
     * Package to be imported.
     */
    private List<String> imports;

    /**
     * properties.
     */
    private List<Property> properties;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Boolean getComment() {
        return comment;
    }

    public void setComment(Boolean comment) {
        this.comment = comment;
    }

    public Map<String, Boolean> getConstructors() {
        return constructors;
    }

    public void setConstructors(Map<String, Boolean> constructors) {
        this.constructors = constructors;
    }

    public List<String> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<String> annotations) {
        this.annotations = annotations;
    }

    public List<String> getImports() {
        return imports;
    }

    public void setImports(List<String> imports) {
        this.imports = imports;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }
}
