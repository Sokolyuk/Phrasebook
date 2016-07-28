package com.jkfsoft.phrasebook.model;

/**
 * Created by Dmitry Sokolyuk on 28.07.2016.
 */

/**
 * Used to controlling of data changing & saving in DB
 */
public abstract class ADataSaveControlledRow implements IDataSaveControlledRow {

    private boolean insertedRow = true;
    private boolean updatedRow = false;
    private boolean deletedRow = false;

    /**
     * Need insert into db
     * @return
     */
    public boolean isInsertedRow() {
        return insertedRow;
    }

    /**
     * Need update in db
     * @return
     */
    public boolean isUpdatedRow() {
        return updatedRow;
    }

    /**
     * Need delete in db
     * @return
     */
    public boolean isDeletedRow() {
        return deletedRow;
    }

    /**
     * Need insert or update in db
     * @return
     */
    public boolean isModifiedRow() {
        return insertedRow || updatedRow || deletedRow;
    }

    /**
     * No more need to save in db
     */
    public void setRowAsSaved() {
        insertedRow = false;
        updatedRow = false;
        deletedRow = false;
    }

    /**
     * Use to manage of data's state
     *
     * insertedRow need to check first, it has priority befor updatedRow
     *
     */
    public void setRowAsInserted() {
        insertedRow = true;
    }

    /**
     * Use to manage of data's state
     *
     * updatedRow doesn't matter if insertedRow is true
     */
    public void setRowAsUpdated() {
        updatedRow = true;
    }

    /**
     * Use to manage of data's state
     *
     * deletedRow doesn't matter if insertedRow is true
     */
    public void setRowAsDeleted() {
        deletedRow = true;
    }

}
