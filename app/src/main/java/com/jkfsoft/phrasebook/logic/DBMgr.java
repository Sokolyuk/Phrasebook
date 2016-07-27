package com.jkfsoft.phrasebook.logic;

import android.app.Activity;

import com.jkfsoft.phrasebook.gui.MainActivity;
import com.jkfsoft.phrasebook.model.Tag;
import com.jkfsoft.phrasebook.utils.IThrRes;

import java.util.List;

/**
 * Created by Dmitry Sokolyuk on 27.07.2016.
 */
public class DBMgr {

    public static void selectTagsThr(Activity a, IThrRes i) {
        new Thread(()->{
            try {
                final List<Tag> res = MainActivity.getmOpenHelper().selectTags();
                a.runOnUiThread(()->{
                    MainActivity.setTags(res);
                    if (i != null) i.onSuccess(res);
                });
            }catch(Exception e) {
                e.printStackTrace();
                if (i != null) ((Activity)a).runOnUiThread(()->{i.onException(e);});
            }
        }).start();
    }

    public static void insertTagThr(Activity a, Tag t, IThrRes i) {
        new Thread(()->{
            try {
                final long res_id = MainActivity.getmOpenHelper().insertTag(t);
                a.runOnUiThread(()->{
                    MainActivity.getTags().add(t);
                    if (i != null) i.onSuccess(res_id);
                });
            }catch(Exception e) {
                e.printStackTrace();
                if (i != null) ((Activity)a).runOnUiThread(()->{i.onException(e);});
            }
        }).start();
    }

    public static void updateTagThr(Activity a, Tag t, IThrRes i) {
        new Thread(()->{
            try {
                final int recaff = MainActivity.getmOpenHelper().updateTag(t);
                if (i != null) a.runOnUiThread(()->{i.onSuccess(recaff);});
            }catch(Exception e) {
                e.printStackTrace();
                if (i != null) a.runOnUiThread(()->{i.onException(e);});
            }
        }).start();
    }

    public static void deleteTagThr(Activity a, Tag t, IThrRes i) {
        new Thread(() -> {
            try {
                final int res_recaff = MainActivity.getmOpenHelper().deleteTag(t);
                a.runOnUiThread(()->{
                    MainActivity.getTags().remove(t);
                    if (i != null) i.onSuccess(res_recaff);
                });
            }catch(Exception e) {
                e.printStackTrace();
                if (i != null) a.runOnUiThread(()->{i.onException(e);});
            }
        }).start();
    }

}
