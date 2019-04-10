package io.eightpigs.m2m.model.parse;

import io.eightpigs.m2m.model.config.Class;
import io.eightpigs.m2m.model.config.Config;
import io.eightpigs.m2m.model.db.Table;

import java.util.List;

public class ClassInfo {
    private Config config;
    private Class classConfig;
    private Table table;
    private String className;
    private String _package;
    private List<String> imports;
    private List<PropertyInfo> properties;
    private Boolean fullConstructor;
    private Boolean emptyConstructor;
    private List<String> annotations;

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

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public List<String> getImports() {
        return imports;
    }

    public void setImports(List<String> imports) {
        this.imports = imports;
    }

    public void setProperties(List<PropertyInfo> properties) {
        this.properties = properties;
    }

    public Class getClassConfig() {
        return classConfig;
    }

    public void setClassConfig(Class classConfig) {
        this.classConfig = classConfig;
    }

    public Boolean getFullConstructor() {
        return fullConstructor;
    }

    public void setFullConstructor(Boolean fullConstructor) {
        this.fullConstructor = fullConstructor;
    }

    public Boolean getEmptyConstructor() {
        return emptyConstructor;
    }

    public void setEmptyConstructor(Boolean emptyConstructor) {
        this.emptyConstructor = emptyConstructor;
    }

    public List<String> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<String> annotations) {
        this.annotations = annotations;
    }
}
