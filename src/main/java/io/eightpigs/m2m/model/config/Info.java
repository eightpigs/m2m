package io.eightpigs.m2m.model.config;

/**
 * info config.
 * author or date.
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
