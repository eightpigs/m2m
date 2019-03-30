package io.eightpigs.m2m.model.config;

import java.util.Map;

/**
 * Package config.
 *
 * @author eightpigs <eightpigs@outlook.com>
 * @date 2019-03-29
 */
public class Package {

    /**
     * base package.
     */
    private String base;

    /**
     * Relationship between data table name prefix and package name.
     * If the table name contains the specified prefix, it is assigned to the corresponding package.
     */
    private Map<String, String> group;

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public Map<String, String> getGroup() {
        return group;
    }

    public void setGroup(Map<String, String> group) {
        this.group = group;
    }
}
