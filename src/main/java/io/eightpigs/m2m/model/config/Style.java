package io.eightpigs.m2m.model.config;

/**
 * code style.
 *
 * @author eightpigs <eightpigs@outlook.com>
 * @date 2019-03-28
 */
public class Style {

    /**
     * default 4
     */
    private Integer indent;

    /**
     * Indented style:
     * - space
     * - tab
     * <p>
     * default space
     */
    private String indentStyle;

    public Integer getIndent() {
        return indent;
    }

    public void setIndent(Integer indent) {
        this.indent = indent;
    }

    public String getIndentStyle() {
        return indentStyle;
    }

    public void setIndentStyle(String indentStyle) {
        this.indentStyle = indentStyle;
    }
}
