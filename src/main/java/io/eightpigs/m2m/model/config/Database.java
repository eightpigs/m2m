package io.eightpigs.m2m.model.config;

import io.eightpigs.m2m.Vars;

/**
 * database config.
 *
 * @author eightpigs <eightpigs@outlook.com>
 * @date 2019-03-28
 */
public class Database {
    private String type;
    private String host;
    private Integer port;
    private String name;
    private String user;
    private String password;
    private String params;

    public String getType() {
        return type == null ? Vars.DATABASE_TYPE_MYSQL : type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }
}
