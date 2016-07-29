package com.jkfsoft.phrasebook.model;

/**
 * Created by Dmitry Sokolyuk on 27.07.2016.
 */
public class CardText extends ADataSaveControlledRow {
    private String text;
    private Lang lang;
    private Long savedLang_id;


    /**
     * By default created as inserted. To set saved all you need call setAsSaved()
     *
     * @param text
     * @param lang_id
     * @param lang_name
     */
    public CardText(String text, long lang_id, String lang_name) {
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

    /**
     * savedLang_id need to update in db
     *
     * @return
     */
    public Long getSavedLang_id() {
        return savedLang_id;
    }

    /**
     * Save land_id from db
     */
    @Override
    public void setRowAsSaved() {
        super.setRowAsSaved();
        savedLang_id = lang.getId();
    }
}
