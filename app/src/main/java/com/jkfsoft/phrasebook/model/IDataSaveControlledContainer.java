package com.jkfsoft.phrasebook.model;

/**
 * Created by Dmitry Sokolyuk on 28.07.2016.
 */
public interface IDataSaveControlledContainer extends IDataSaveControlledRow {

    /**
     * get value of isModifiedChildData
     *
     * @return
     */
    public boolean isModifiedChildData();

}
