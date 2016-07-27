package com.jkfsoft.phrasebook.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dmitry Sokolyuk on 27.07.2016.
 */
public class Card {
    private Long id;

    private List<Tag> tags = new ArrayList<>();
    private List<CardText> cardTexts = new ArrayList<>();

    public Card(Long id) {
        this.id = id;
    }

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

    public interface IGetCardText {
        public CardText getCardText();
    }

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

    public interface IGetTag {
        public Tag getTag();
    }

    //region getters / setters / toString()
    public List<Tag> getTags() {
        return tags;
    }

    public List<CardText> getCardTexts() {
        return cardTexts;
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
