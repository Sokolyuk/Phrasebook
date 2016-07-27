package com.jkfsoft.phrasebook.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dmitry Sokolyuk on 27.07.2016.
 */
public class Card {
    private Long id;

    private List<Tag> cardTags = new ArrayList<>();
    private List<CardText> cardTexts = new ArrayList<>();

    public Card(Long id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return "Card{" +
                "cardTags=" + cardTags +
                ", cardTexts=" + cardTexts +
                '}';
    }
}
