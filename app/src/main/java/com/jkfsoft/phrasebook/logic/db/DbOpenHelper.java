package com.jkfsoft.phrasebook.logic.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jkfsoft.phrasebook.model.Card;
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
    public void onCreate(SQLiteDatabase db) {

        //stub !!! must redesigned
        db.execSQL("CREATE TABLE `tag` (\n" +
                    "\t`id`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "\t`name`\tTEXT NOT NULL UNIQUE\n" +
                    ");");
        db.execSQL("CREATE TABLE `lang` (\n" +
                    "\t`id`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "\t`name`\tTEXT NOT NULL UNIQUE\n" +
                    ");");
        db.execSQL("CREATE TABLE `card` (\n" +
                    "\t`id`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "\t`learned` INTEGER default 0\n" +
                    ");");
        db.execSQL("CREATE TABLE `card_tag` (\n" +
                    "\t`card_id`\tINTEGER,\n" +
                    "\t`tag_id`\tINTEGER,\n" +
                    "\tPRIMARY KEY(card_id,tag_id),\n" +
                    "\tFOREIGN KEY(card_id) REFERENCES card(id),\n" +
                    "\tFOREIGN KEY(tag_id) REFERENCES tag(id)\n" +
                    ");");
        db.execSQL("CREATE TABLE `card_lang` (\n" +
                    "\t`card_id`\tINTEGER,\n" +
                    "\t`lang_id`\tINTEGER,\n" +
                    "\t`text`\tTEXT NOT NULL,\n" +
                    "\tPRIMARY KEY(card_id,lang_id),\n" +
                    "\tFOREIGN KEY(card_id) REFERENCES card(id),\n" +
                    "\tFOREIGN KEY(lang_id) REFERENCES lang(id)\n" +
                    ");");
        db.execSQL("create view vwu_card as select c.id as `id`, cl.lang_id as `lang_id`, l.name as `lang_name`, cl.text as `text`, t.id as `tag_id`, t.name as `tag_name` from card c\n" +
                    "\tleft outer join card_tag ct on ct.card_id=c.id\n" +
                    "\tleft outer join tag t on t.id=ct.tag_id\n" +
                    "\tleft outer join card_lang cl on cl.card_id=c.id\n" +
                    "\tleft outer join lang l on l.id=cl.lang_id\n" +
                    "order by c.id, cl.lang_id, ct.tag_id;");

        db.execSQL("insert into `lang` (name)values('En');\n");
        db.execSQL("insert into `lang` (name)values('De');\n");
        db.execSQL("insert into `lang` (name)values('Ru');\n");

        db.execSQL("insert into tag (`name`)values('Hochsprache');\n");
        db.execSQL("insert into tag (`name`)values('Umgangsprach');\n");
        db.execSQL("insert into tag (`name`)values('Geschäfte');\n");
        db.execSQL("insert into tag (`name`)values('Offene Verkehr');\n");
        db.execSQL("insert into tag (`name`)values('Hause');\n");
        db.execSQL("insert into tag (`name`)values('Straße');\n");
        db.execSQL("insert into tag (`name`)values('Arbeit');\n");

        db.execSQL("insert into card (learned)values(0);\n");
        db.execSQL("insert into card (learned)values(0);\n");
        db.execSQL("insert into card (learned)values(0);\n");
        db.execSQL("insert into card (learned)values(0);\n");
        db.execSQL("insert into card (learned)values(0);\n");

        db.execSQL("insert into card_tag (card_id,tag_id)values(2,2);\n");
        db.execSQL("insert into card_tag (card_id,tag_id)values(2,5);\n");
        db.execSQL("insert into card_tag (card_id,tag_id)values(3,7);\n");

        db.execSQL("insert into card_lang(card_id,lang_id,text)values(1,1,'All roads lead to Rome');\n");
        db.execSQL("insert into card_lang(card_id,lang_id,text)values(1,2,'Alle Wege führen nach Rom');\n");
        db.execSQL("insert into card_lang(card_id,lang_id,text)values(1,3,'Все дороги ведут в Рим');\n");

        db.execSQL("insert into card_lang(card_id,lang_id,text)values(2,1,'Cause time fun hour');\n");
        db.execSQL("insert into card_lang(card_id,lang_id,text)values(2,3,'Делу время, потехе час');\n");

        db.execSQL("insert into card_lang(card_id,lang_id,text)values(5,3,'test-123');");

        //db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //stub !!! must redesigned

        db.execSQL("drop view if exists vwu_card;");
        db.execSQL("drop TABLE if exists `card_tag`;");
        db.execSQL("drop TABLE if exists `card_lang`;");
        db.execSQL("drop TABLE if exists `tag`;");
        db.execSQL("drop TABLE if exists `lang`;");
        db.execSQL("drop TABLE if exists `card`;");

        this.onCreate(db);
    }

    public List<Lang> selectLangs() {
        ArrayList<Lang> res = new ArrayList<>();
        try(
                SQLiteDatabase d = getReadableDatabase();
                Cursor cur = d.query(TblLangs.TBL_NAME, null, null, null, null, null, null);
        ){
            if (cur != null) {
                while(cur.moveToNext()){
                    long id = cur.getLong(cur.getColumnIndex(TblLangs.COL_NAME_ID));
                    String name = cur.getString(cur.getColumnIndex(TblLangs.TBL_NAME));
                    res.add(new Lang(id, name));
                }
            }
        }
        return res;
    }

    public List<Tag> selectTags() {
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

    public synchronized long insertTag(Tag t) {
        try(SQLiteDatabase db = getWritableDatabase();) {
            ContentValues cv = new ContentValues();
            cv.put(TblTags.COL_NAME_ID, t.getId());
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

    private Lang _loadLang(Cursor cur) {

    }

    private Tag _loadTag(Cursor cur) {

    }

    public List<Card> selectCards() {
        ArrayList<Card> res = new ArrayList<>();
        try(
                SQLiteDatabase d = getReadableDatabase();
                Cursor cur = d.query(TblCards.TBL_NAME, null, null, null, null, null, null);
        ){
            if (cur != null) {
                label_card_id:
                while(cur.moveToNext()){
                    long id = cur.getLong(cur.getColumnIndex(TblCards.COL_NAME_ID));

                    Card card = new Card(id);




                    label_lang_id:
                    while(cur.moveToNext()){


                        label_tag_id:
                        while(cur.moveToNext()){

                        }



                    }


                    String text = cur.getString(cur.getColumnIndex(TblCards.COL_NAME_TEXT));

                    long lang_id = cur.getLong(cur.getColumnIndex(TblCards.COL_NAME_LANG_ID));
                    String lang_name = cur.getString(cur.getColumnIndex(TblCards.COL_NAME_LANG_NAME));

                    long tag_id = cur.getLong(cur.getColumnIndex(TblCards.COL_NAME_TAG_ID));
                    String tag_name = cur.getString(cur.getColumnIndex(TblCards.COL_NAME_LANG_NAME));



                    res.add(card);
                }
            }
        }
        return res;
    }



}
