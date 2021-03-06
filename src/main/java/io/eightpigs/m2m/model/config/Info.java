package io.eightpigs.m2m.model.config;

/**
 * Configuration information for class documentation comments.
 *
 * @author eightpigs <eightpigs@outlook.com>
 * @date 2019-03-30
 */
public class Info {

    /**
     * creator.
     */
    private String author;

    /**
     * create date.
     */
    private String date;

    public Info() {
    }

    public Info(String author, String date) {
        this.author = author;
        this.date = date;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
