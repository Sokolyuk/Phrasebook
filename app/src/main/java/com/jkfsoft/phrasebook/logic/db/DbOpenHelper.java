package com.jkfsoft.phrasebook.logic.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.jkfsoft.phrasebook.gui.MainActivity;
import com.jkfsoft.phrasebook.logic.db.structure.TblCard_tag;
import com.jkfsoft.phrasebook.logic.db.structure.TblCard_lang;
import com.jkfsoft.phrasebook.logic.db.structure.TblCards;
import com.jkfsoft.phrasebook.logic.db.structure.TblLangs;
import com.jkfsoft.phrasebook.logic.db.structure.TblTags;
import com.jkfsoft.phrasebook.logic.db.structure.VwuCards;
import com.jkfsoft.phrasebook.model.Card;
import com.jkfsoft.phrasebook.model.CardText;
import com.jkfsoft.phrasebook.model.Lang;
import com.jkfsoft.phrasebook.model.Tag;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dmitry Sokolyuk on 27.07.2016.
 */
public class DbOpenHelper extends SQLiteOpenHelper {

    public static final String DB_NAME ="phrasebook.db";
    public static final int DB_VERSION = 1;

    private static DbOpenHelper instance;

    public static synchronized DbOpenHelper getInstance(Context c) {
        if (instance == null) {
            instance = new DbOpenHelper(c);
        }
        return instance;
    }

    private DbOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public synchronized void onCreate(SQLiteDatabase db) {
        //load sql-script from resources
        for(String sql: MainActivity.db_script_create) {
            db.execSQL(sql);
        }

    }

    @Override
    public synchronized void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //load sql-script from resources
        for(String sql: MainActivity.db_script_drop) {
            db.execSQL(sql);
        }

        this.onCreate(db);
    }

    public synchronized List<Lang> selectLangs() {
        ArrayList<Lang> res = new ArrayList<>();
        try(
                SQLiteDatabase d = getReadableDatabase();
                Cursor cur = d.query(TblLangs.TBL_NAME, null, null, null, null, null, null);
        ){
            if (cur != null) {
                while(cur.moveToNext()){
                    long id = cur.getLong(cur.getColumnIndex(TblLangs.COL_NAME_ID));
                    String name = cur.getString(cur.getColumnIndex(TblLangs.COL_NAME_NAME));
                    res.add(new Lang(id, name));
                }
            }
        }
        return res;
    }

    public synchronized List<Tag> selectTags() {
        ArrayList<Tag> res = new ArrayList<>();
        try(
                SQLiteDatabase d = getReadableDatabase();
                Cursor cur = d.query(TblTags.TBL_NAME, null, null, null, null, null, null);
        ){
            if (cur != null) {
                while(cur.moveToNext()){
                    long id = cur.getLong(cur.getColumnIndex(TblTags.COL_NAME_ID));
                    String name = cur.getString(cur.getColumnIndex(TblTags.COL_NAME_NAME));
                    res.add(new Tag(id, name));
                }
            }
        }
        return res;
    }

    public synchronized long insertTag(Tag t) throws Exception {
        try(SQLiteDatabase db = getWritableDatabase();) {
            Long tag_id = t.getId();
            if (tag_id != null) throw new Exception(DbConsts.ERR_IDASSIGNED);
            ContentValues cv = new ContentValues();
            cv.put(TblTags.COL_NAME_NAME, t.getName());
            long _id = db.insert(TblTags.TBL_NAME, null, cv);
            t.setId(_id);
            return _id;
        }
    }

    public synchronized int updateTag(Tag t) throws Exception {
        if (t == null || t.getId() == null || t.getId() < 1) throw new Exception(DbConsts.ERR_IDUNASSIGNED);
        try(SQLiteDatabase db = getWritableDatabase();) {
            ContentValues cv = new ContentValues();
            cv.put(TblTags.COL_NAME_NAME, t.getName());
            return db.update(TblTags.TBL_NAME, cv, String.format("%s=%s", TblTags.COL_NAME_ID, String.valueOf(t.getId())), null);
        }
    }

    public synchronized int deleteTag(Tag t) throws Exception {
        if (t == null || t.getId() == null || t.getId() < 1) throw new Exception(DbConsts.ERR_IDUNASSIGNED);
        try(SQLiteDatabase db = getWritableDatabase();){
            return db.delete(TblTags.TBL_NAME, String.format("%s=%s", TblTags.COL_NAME_ID, String.valueOf(t.getId())), null);
        }
    }

    public synchronized List<Card> selectCards() {
        ArrayList<Card> res = new ArrayList<>();
        try(
                SQLiteDatabase d = getReadableDatabase();
                Cursor cur = d.query(VwuCards.VWU_NAME, null, null, null, null, null, null);
        ){
            if (cur != null) {

                //region calling optimization
                //optimize while. Only ones call getColumnIndex()
                int i_col_id = cur.getColumnIndex(VwuCards.COL_NAME_ID);
                int i_col_learned = cur.getColumnIndex(VwuCards.COL_NAME_LEARNED);
                int i_col_text = cur.getColumnIndex(VwuCards.COL_NAME_TEXT);
                int i_col_lang_id = cur.getColumnIndex(VwuCards.COL_NAME_LANG_ID);
                int i_col_lang_name = cur.getColumnIndex(VwuCards.COL_NAME_LANG_NAME);
                int i_col_tag_id = cur.getColumnIndex(VwuCards.COL_NAME_TAG_ID);
                int i_col_tag_name = cur.getColumnIndex(VwuCards.COL_NAME_TAG_NAME);
                //endregion

                //parsing cursor
                long id_prev = 0;
                Card card = null;
                while(cur.moveToNext()){
                    long id = cur.getLong(i_col_id);
                    int card_learned = cur.getInt(i_col_learned);

                    //resultset is multiple rows, because in select used join
                    if (id != id_prev) {
                        card = new Card(id, card_learned);
                    }

                    //Add CardText
                    if (!cur.isNull(i_col_lang_id)) {
                        long lang_id = cur.getLong(i_col_lang_id);
                        //try add new CardText by id
                        card.tryAddCardText(lang_id, ()->{
                            //if id not found then callback
                            CardText ct = new CardText(cur.getString(i_col_text), lang_id, cur.getString(i_col_lang_name));
                            ct.setRowAsSaved();
                            return ct;
                        });
                    }

                    //Add Tag
                    if (!cur.isNull(i_col_tag_id)) {
                        long tag_id = cur.getLong(i_col_tag_id);
                        //try add new Tag by id
                        card.tryAddTag(tag_id, ()->{
                            //if id not found then callback
                            Tag t = new Tag(tag_id, cur.getString(i_col_tag_name));
                            t.setRowAsSaved();
                            return t;
                        });
                    }

                    //only first row of resultset need to add as new element
                    if (id != id_prev) {
                        id_prev = id;
                        card.setRowAsSaved();
                        res.add(card);
                    }
                }
            }
        }
        return res;
    }

    public synchronized long insertCard(Card c) throws Exception {
        try(SQLiteDatabase db = getWritableDatabase();) {
            if (c == null) throw new Exception(DbConsts.ERR_STATEMENTCARDNOTASSIGNED);
            Long card_id = c.getId();
            if (card_id != null) throw new Exception(DbConsts.ERR_IDASSIGNED);

            //open transaction
            db.beginTransaction();
            try{
                //insert into table card
                ContentValues card_cv = new ContentValues();
                card_cv.put(TblCards.COL_NAME_LEARNED, c.getLearned());
                card_id = db.insert(TblCards.TBL_NAME, null, card_cv);

                //insert card's tags into card_tag by id
                for(Tag t: c.getTags()) {
                    ContentValues card_tag_cv = new ContentValues();
                    card_tag_cv.put(TblCard_tag.COL_NAME_CARD_ID, card_id);
                    card_tag_cv.put(TblCard_tag.COL_NAME_TAG_ID, t.getId());
                    card_id = db.insert(TblCards.TBL_NAME, null, card_tag_cv);
                }

                //insert card's texts into card_lang by id
                for(CardText ct: c.getCardTexts()) {
                    ContentValues card_lang_cv = new ContentValues();
                    card_lang_cv.put(TblCard_lang.COL_NAME_CARD_ID, card_id);
                    card_lang_cv.put(TblCard_lang.COL_NAME_LANG_ID, ct.getLang().getId());
                    card_id = db.insert(TblCard_lang.TBL_NAME, null, card_lang_cv);
                }

                //commit
                db.setTransactionSuccessful();

                //set new id to new Card
                c.setId(card_id);

                return card_id;
            }finally{
                //end of transaction. Without setTransactionSuccessful it do rollback. Android's Spezialitaet.
                db.endTransaction();
            }
        }
    }

    public synchronized int updateCard(Card c) throws Exception {
        if (c == null || c.getId() == null || c.getId() < 1) throw new Exception(DbConsts.ERR_IDUNASSIGNED);

        if (!c.isModified()) return 0;

        try(SQLiteDatabase db = getWritableDatabase();) {

            long card_id = c.getId();
            int recaff = 0;

            //open transaction
            db.beginTransaction();
            try{
                if (c.isModifiedChildData()) {
                    //delete sub tag
                    recaff += db.delete(TblCard_tag.TBL_NAME, String.format("%s=%s", TblCard_tag.COL_NAME_CARD_ID, String.valueOf(card_id)), null);

                    for(Tag t: c.getTags()) {
                        //insert sub tag
                        ContentValues card_tag_cv = new ContentValues();
                        card_tag_cv.put(TblCard_tag.COL_NAME_CARD_ID, card_id);
                        card_tag_cv.put(TblCard_tag.COL_NAME_TAG_ID, t.getId());
                        db.insert(TblCard_tag.TBL_NAME, null, card_tag_cv);
                        recaff++;
                    }

                    recaff += db.delete(TblCard_lang.TBL_NAME, String.format("%s=%s", TblCard_lang.COL_NAME_CARD_ID, String.valueOf(card_id)), null);

                    for(CardText ct: c.getCardTexts()) {
                        ContentValues card_lang_cv = new ContentValues();
                        card_lang_cv.put(TblCard_lang.COL_NAME_CARD_ID, card_id);
                        card_lang_cv.put(TblCard_lang.COL_NAME_LANG_ID, ct.getLang().getId());
                        card_lang_cv.put(TblCard_lang.COL_NAME_TEXT, ct.getText());
                        db.insert(TblCard_lang.TBL_NAME, null, card_lang_cv);
                        recaff++;
                    }
                }

                //update card
                if (c.isModifiedRow()) {
                    ContentValues card_cv = new ContentValues();
                    card_cv.put(TblCards.COL_NAME_LEARNED, c.getLearned());
                    recaff += db.update(TblCards.TBL_NAME, card_cv, String.format("%s=%s", TblCards.COL_NAME_ID, String.valueOf(card_id)), null);
                }

                //commit
                db.setTransactionSuccessful();

                c.setAsSaved();

                //count of records affected. Used for extra controll of actions
                return recaff;
            }finally{
                //end of transaction. Without setTransactionSuccessful it do rollback. Android's Spezialitaet.
                db.endTransaction();
            }

        }
    }

    public synchronized int deleteCard(Card c) throws Exception {
        if (c == null || c.getId() == null || c.getId() < 1) throw new Exception(DbConsts.ERR_IDUNASSIGNED);
        try(SQLiteDatabase db = getWritableDatabase();){

            long card_id = c.getId();
            int recaff = 0;

            //open transaction
            db.beginTransaction();
            try{
                //delete from card_tag where card_id = ?
                recaff += db.delete(TblCard_tag.TBL_NAME, String.format("%s=%s", TblCard_tag.COL_NAME_CARD_ID, String.valueOf(card_id)), null);

                //delete from card_lang where card_id = ?
                recaff += db.delete(TblCard_lang.TBL_NAME, String.format("%s=%s", TblCard_lang.COL_NAME_CARD_ID, String.valueOf(card_id)), null);

                //delete from card where id = ?
                recaff += db.delete(TblCards.TBL_NAME, String.format("%s=%s", TblCards.COL_NAME_ID, String.valueOf(card_id)), null);

                //commit
                db.setTransactionSuccessful();

                //count of records affected. Used for extra controll of actions
                return recaff;
            }finally{
                //end of transaction. Without setTransactionSuccessful it do rollback. Android's Spezialitaet.
                db.endTransaction();
            }
        }
    }





}
