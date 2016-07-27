package com.jkfsoft.phrasebook.model;

/**
 * Created by Dmitry Sokolyuk on 27.07.2016.
 */
public class CardText {
    private String text;
    private Lang lang;

    public CardText(String text, Lang lang) {
        this.text = text;
        this.lang = lang;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Lang getLang() {
        return lang;
    }

    public void setLang(Lang lang) {
        this.lang = lang;
    }

    @Override
    public String toString() {
        return "CardText{" +
                "text='" + text + '\'' +
                ", lang=" + lang +
                '}';
    }
}
