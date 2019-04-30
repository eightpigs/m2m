package io.eightpigs.m2m.model.parse;

import io.eightpigs.m2m.model.config.Property;
import io.eightpigs.m2m.model.db.Column;

import java.util.List;

public class PropertyInfo {

    private Property config;

    private Column column;

    private String propertyName;

    /**
     * Upper Camel Case name.
     */
    private String upperCaseName;

    private String[] javaTypeAndImport;

    /**
     * last property in class.
     */
    private Boolean last;

    private List<String> annotations;

    public PropertyInfo(Property config, Column column, String[] javaTypeAndImport) {
        this.config = config;
        this.column = column;
        this.javaTypeAndImport = javaTypeAndImport;
    }

    public Property getConfig() {
        return config;
    }

    public void setConfig(Property config) {
        this.config = config;
    }

    public Column getColumn() {
        return column;
    }

    public void setColumn(Column column) {
        this.column = column;
    }

    public String[] getJavaTypeAndImport() {
        return javaTypeAndImport;
    }

    public void setJavaTypeAndImport(String[] javaTypeAndImport) {
        this.javaTypeAndImport = javaTypeAndImport;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getUpperCaseName() {
        return upperCaseName;
    }

    public void setUpperCaseName(String upperCaseName) {
        this.upperCaseName = upperCaseName;
    }

    public Boolean getLast() {
        return last;
    }

    public void setLast(Boolean last) {
        this.last = last;
    }

    public List<String> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<String> annotations) {
        this.annotations = annotations;
    }
}
