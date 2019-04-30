package io.eightpigs.m2m.model.config;

import java.util.List;

/**
 * m2m config
 *
 * @author eightpigs <eightpigs@outlook.com>
 * @date 2019-03-30
 */
public class Config {

    /**
     * database config.
     */
    private Database database;

    /**
     * style config.
     */
    private Style style;

    /**
     * package config.
     */
    private Package _package;

    /**
     * The name of the table to be processed, if it is empty, then handle all the tables.
     */
    private List<String> tables;

    /**
     * Information about creation.
     */
    private Info info;

    /**
     * class config.
     */
    private Class[] classes;

    /**
     * The target directory, if not specified, is placed directly in the project's src directory.
     * Is the relative path of the current project.
     * default: src
     */
    private String path;

    public Database getDatabase() {
        return database;
    }

    public void setDatabase(Database database) {
        this.database = database;
    }

    public Style getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    public Package getPackage() {
        return _package;
    }

    public void setPackage(Package _package) {
        this._package = _package;
    }

    public Class[] getClasses() {
        return classes;
    }

    public void setClasses(Class[] classes) {
        this.classes = classes;
    }

    public Package get_package() {
        return _package;
    }

    public void set_package(Package _package) {
        this._package = _package;
    }

    public List<String> getTables() {
        return tables;
    }

    public void setTables(List<String> tables) {
        this.tables = tables;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
