package com.jkfsoft.phrasebook.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Card contains all data of card and tag and lang and text
 * Note Card extends from DataSaveControlledContainer.
 * There's implements controlling state of data.
 * It need for optimization queries to db
 *
 * Created by Dmitry Sokolyuk on 27.07.2016.
 */
public class Card extends ADataSaveControlledRow implements IDataSaveControlled {
    private Long id;
    private int learned;

    private List<Tag> tags = new ArrayList<>();
    private List<CardText> cardTexts = new ArrayList<>();

    /**
     * Constructor for new Card
     */
    public Card() {
        this.id = null;
        setRowAsInserted();//if it has id then it saved in db
        this.learned = 0;
    }

    /**
     * Constructor for load from db
     *
     * @param id
     * @param learned
     */
    public Card(Long id, int learned) {
        this.id = id;
        if (id != null) setRowAsSaved(); else setRowAsInserted();//if it has id then it saved in db
        this.learned = learned;
    }

    /**
     * Try add new element in array if it not exists
     *
     * @param lang_id
     * @param getCardText
     */
    public void tryAddCardText(long lang_id, IGetCardText getCardText) {
        if (cardTexts != null && getCardText != null) {
            for(CardText ct: cardTexts) {
                if (ct.getLang().getId() == lang_id)
                    return;
            }
            //Lang not found. Callback to data reader.
            cardTexts.add(getCardText.getCardText());
        }
    }

    /**
     * Callback interface for tryAddCardText
     */
    public interface IGetCardText {
        public CardText getCardText();
    }

    /**
     *  Try add new element in array if it not exists
     * @param tag_id
     * @param getTag
     */
    public void tryAddTag(long tag_id, IGetTag getTag) {
        if (tags != null && getTag != null) {
            for(Tag t: tags) {
                if (t.getId() == tag_id)
                    return;
            }
            //Tag not found. Callback to data reader.
            tags.add(getTag.getTag());
        }
    }

    /**
     * Callback interface for tryAddTag
     */
    public interface IGetTag {
        public Tag getTag();
    }

    @Override
    public boolean isModified() {
        return isModifiedRow() || isModifiedChildData();
    }

    protected boolean isModifiedChildDataTags() {
        for(Tag row: tags) {
            if(row.isModifiedRow()) return true;
        }
        return false;
    }

    protected boolean isModifiedChildDataCardTexts() {
        for(CardText row: cardTexts) {
            if(row.isModifiedRow()) return true;
        }
        return false;
    }

    @Override
    public boolean isModifiedChildData() {
        return isModifiedChildDataTags() || isModifiedChildDataCardTexts();
    }

    //region getters / setters / toString()
    public List<Tag> getTags() {
        return tags;
    }

    public List<CardText> getCardTexts() {
        return cardTexts;
    }

    /**
     * Get table id
     * @return
     */
    public Long getId() {
        return id;
    }

    /**
     * set table id
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
        if (id != null) setRowAsSaved(); else setRowAsInserted();
    }

    public int getLearned() {
        return learned;
    }

    public void setLearned(int learned) {
        this.learned = learned;
        setRowAsUpdated();//set as modified
    }

    @Override
    public String toString() {
        return "Card{" +
                "tags=" + tags +
                ", cardTexts=" + cardTexts +
                '}';
    }
    //endregion

}
