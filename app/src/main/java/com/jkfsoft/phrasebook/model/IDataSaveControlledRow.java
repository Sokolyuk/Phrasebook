package com.jkfsoft.phrasebook.model;

/**
 * Created by Dmitry Sokolyuk on 28.07.2016.
 */
public interface IDataSaveControlledRow {
    /**
     * Need insert into db
     * @return
     */
    public boolean isInsertedRow();

    /**
     * Need update in db
     * @return
     */
    public boolean isUpdatedRow();

    /**
     * Need delete in db
     * @return
     */
    public boolean isDeletedRow();

    /**
     * Need insert or update in db
     * @return
     */
    public boolean isModifiedRow();

    /**
     * No more need to save in db
     */
    public void setRowAsSaved();

    /**
     * Use to manage of data's state
     *
     * insertedRow need to check first, it has priority befor updatedRow
     *
     */
    public void setRowAsInserted();

    /**
     * Use to manage of data's state
     *
     * updatedRow doesn't matter if insertedRow is true
     */
    public void setRowAsUpdated();

    /**
     * Use to manage of data's state
     *
     * deletedRow doesn't matter if insertedRow is true
     */
    public void setRowAsDeleted();
}
