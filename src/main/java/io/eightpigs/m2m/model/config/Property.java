package io.eightpigs.m2m.model.config;

import java.util.List;

/**
 * Class property config.
 *
 * @author eightpigs <eightpigs@outlook.com>
 * @date 2019-03-29
 */
public class Property {

    /**
     * column name.
     * If *, it represents all property.
     */
    private String columnName;

    /**
     * The generated property name, if empty, by default hump style.
     */
    private String propertyName;

    /**
     * Whether to generate a setter.
     */
    private Boolean getter;

    /**
     * Whether to generate a Getter.
     */
    private Boolean setter;

    /**
     * Whether to use the comment of the field in the table.
     */
    private Boolean comment;

    /**
     * Custom annotation.
     */
    private List<String> annotations;

    /**
     * Package to be imported.
     */
    private List<String> imports;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public Boolean getGetter() {
        return getter;
    }

    public void setGetter(Boolean getter) {
        this.getter = getter;
    }

    public Boolean getSetter() {
        return setter;
    }

    public void setSetter(Boolean setter) {
        this.setter = setter;
    }

    public Boolean getComment() {
        return comment;
    }

    public void setComment(Boolean comment) {
        this.comment = comment;
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
}
