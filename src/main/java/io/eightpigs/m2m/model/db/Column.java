package io.eightpigs.m2m.model.db;

/**
 * Column Info
 *
 * @author eightpigs <eightpigs@outlook.com>
 * @date 2019-03-28
 */
public class Column {

    /**
     * field name
     */
    private String name;

    /**
     * field type
     * eg: varchar / bit
     */
    private String type;

    /**
     * field type length
     * Extract from the column type.
     */
    private Integer length;

    /**
     * column comment.
     */
    private String comment;

    public Column(String name, String type, Integer length, String comment) {
        this.name = name;
        this.type = type;
        this.length = length;
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
