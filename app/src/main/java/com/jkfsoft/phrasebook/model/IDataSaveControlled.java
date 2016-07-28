package com.jkfsoft.phrasebook.model;

/**
 * Created by Dmitry Sokolyuk on 28.07.2016.
 */
public interface IDataSaveControlled extends IDataSaveControlledContainer {

    /**
     * get value of isModified
     *
     * @return
     */
    public boolean isModified();

}
