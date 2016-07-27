package com.jkfsoft.phrasebook.model;

/**
 * Created by Dmitry Sokolyuk on 27.07.2016.
 */
public class Lang {

    private Long id;
    private String name;

    public Lang(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Lang{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
