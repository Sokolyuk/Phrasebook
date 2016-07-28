package com.jkfsoft.phrasebook.model;

/**
 * Created by Dmitry Sokolyuk on 27.07.2016.
 */
public class CardText extends ADataSaveControlledRow {
    private String text;
    private Lang lang;

    public CardText(boolean isInserted, String text, Lang lang) {
        if (isInserted) setRowAsInserted(); else setRowAsUpdated();
        this.text = text;
        this.lang = lang;
    }

    public CardText(boolean isInserted, String text, long lang_id, String lang_name) {
        if (isInserted) setRowAsInserted(); else setRowAsUpdated();
        this.text = text;
        this.lang = new Lang(lang_id, lang_name);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        setRowAsUpdated();
    }

    public Lang getLang() {
        return lang;
    }

    public void setLang(Lang lang) {
        this.lang = lang;
        setRowAsUpdated();
    }

    @Override
    public String toString() {
        return "CardText{" +
                "text='" + text + '\'' +
                ", lang=" + lang +
                '}';
    }
}
