package com.jkfsoft.phrasebook.logic;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;

import com.jkfsoft.phrasebook.gui.MainActivity;
import com.jkfsoft.phrasebook.model.Card;
import com.jkfsoft.phrasebook.model.Lang;
import com.jkfsoft.phrasebook.model.Tag;
import com.jkfsoft.phrasebook.utils.IThrRes;

import java.util.List;

/**
 * Created by Dmitry Sokolyuk on 27.07.2016.
 */
public class DBMgr {

    public static void selectTags(Context context) {
        selectTags(context, null);
    }

    public static void selectTags(Context context, IThrRes i) {
        try {
            final List<Tag> res = MainActivity.getmOpenHelper().selectTags();
            MainActivity.setTags(res);
            if (i != null) i.onSuccess(res);
        }catch(Exception e) {
            e.printStackTrace();
            if (i != null) {
                i.onException(e);
            } else {
                MainActivity.showMess(context, e.getMessage());
            }
        }
    }

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

    public static void selectCards(Context context) {
        selectCards(context, null);
    }

    public static void selectCards(Context context, IThrRes i) {
        try {
            final List<Card> res = MainActivity.getmOpenHelper().selectCards();
            MainActivity.setCards(res);
            if (i != null) i.onSuccess(res);
        }catch(Exception e) {
            e.printStackTrace();
            if (i != null) {
                i.onException(e);
            } else {
                MainActivity.showMess(context, e.getMessage());
            }

        }
    }

    public static void selectCardsThr(Activity a, IThrRes i) {
        new Thread(()->{
            try {
                final List<Card> res = MainActivity.getmOpenHelper().selectCards();
                a.runOnUiThread(()->{
                    MainActivity.setCards(res);
                    if (i != null) i.onSuccess(res);
                });
            }catch(Exception e) {
                e.printStackTrace();
                if (i != null) ((Activity)a).runOnUiThread(()->{i.onException(e);});
            }
        }).start();
    }

    public static void insertCardThr(Activity a, Card c, IThrRes i) {
        new Thread(()->{
            try {
                final long res_id = MainActivity.getmOpenHelper().insertCard(c);
                a.runOnUiThread(()->{
                    MainActivity.getCards().add(c);
                    if (i != null) i.onSuccess(res_id);
                });
            }catch(Exception e) {
                e.printStackTrace();
                if (i != null) ((Activity)a).runOnUiThread(()->{i.onException(e);});
            }
        }).start();
    }

    public static void updateCardThr(Activity a, Card c, IThrRes i) {
        new Thread(()->{
            try {
                final int recaff = MainActivity.getmOpenHelper().updateCard(c);
                if (i != null) a.runOnUiThread(()->{i.onSuccess(recaff);});
            }catch(Exception e) {
                e.printStackTrace();
                if (i != null) a.runOnUiThread(()->{i.onException(e);});
            }
        }).start();
    }

    public static void deleteCardThr(Activity a, Card c, IThrRes i) {
        new Thread(() -> {
            try {
                final int res_recaff = MainActivity.getmOpenHelper().deleteCard(c);
                a.runOnUiThread(()->{
                    MainActivity.getTags().remove(c);
                    if (i != null) i.onSuccess(res_recaff);
                });
            }catch(Exception e) {
                e.printStackTrace();
                if (i != null) a.runOnUiThread(()->{i.onException(e);});
            }
        }).start();
    }

    public static void selectLangs(Context context) {
        selectLangs(context, null);
    }

    public static void selectLangs(Context context, IThrRes i) {
        try {
            final List<Lang> res = MainActivity.getmOpenHelper().selectLangs();
            MainActivity.setLangs(res);
            if (i != null) i.onSuccess(res);
        }catch(Exception e) {
            e.printStackTrace();
            if (i != null) {
                i.onException(e);
            } else {
                MainActivity.showMess(context, e.getMessage());
            }

        }
    }

}
