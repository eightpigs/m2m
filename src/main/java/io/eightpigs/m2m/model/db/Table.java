package io.eightpigs.m2m.model.db;

import java.util.List;

/**
 * Table info
 *
 * @author eightpigs <eightpigs@outlook.com>
 * @date 2019-03-28
 */
public class Table {

    /**
     * table name
     */
    private String name;

    /**
     * table comment
     */
    private String comment;

    /**
     * all columns
     */
    private List<Column> columns;

    public Table(String name, String comment, List<Column> columns) {
        this.name = name;
        this.comment = comment;
        this.columns = columns;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }
}
