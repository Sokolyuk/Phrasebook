package com.jkfsoft.phrasebook.model;

/**
 * Created by Dmitry Sokolyuk on 27.07.2016.
 */
public class Tag extends ADataSaveControlledRow {
    private Long id;
    private String name;

    public Tag(Long id, String name) {
        this.id = id;
        if (id != null) setRowAsSaved(); else setRowAsInserted();
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
        if (id != null) setRowAsSaved(); else setRowAsInserted();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        setRowAsUpdated();
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public String toCSVExport() {
        return String.format("%s%s%s\n", name, IModelConsts.SPLIT, String.valueOf(id));
    }
}
