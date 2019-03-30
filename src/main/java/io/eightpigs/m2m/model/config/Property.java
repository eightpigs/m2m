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
     * property name.
     * If empty, it represents all property.
     */
    private String name;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
