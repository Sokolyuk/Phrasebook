package com.jkfsoft.phrasebook.logic.listener;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;

import com.jkfsoft.phrasebook.R;
import com.jkfsoft.phrasebook.gui.AboutActivity;
import com.jkfsoft.phrasebook.gui.FragmentHome;
import com.jkfsoft.phrasebook.gui.FragmentTags;
import com.jkfsoft.phrasebook.gui.MainActivity;
import com.jkfsoft.phrasebook.logic.db.DbOpenHelper;

/**
 * Created by Dmitry Sokolyuk on 28.07.2016.
 */
public class AboutActivityOnClickListener implements View.OnClickListener {

    private Activity activity;
    public static final int PERMISSIONS_REQUEST_CALL_PHONE = 1;
    public static final int PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 2;

    public AboutActivityOnClickListener(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSupportCall:

                //check permission
                if (ActivityCompat.checkSelfPermission(this.activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    //ask permission
                    this.activity.requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, PERMISSIONS_REQUEST_CALL_PHONE);
                } else {
                    //we have a permission & now call
                    this.activity.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(activity.getString(R.string.str_tel_support))));
                }
                break;
            case R.id.btnCleanDB:
                DbOpenHelper oh = MainActivity.getmOpenHelper();
                try(SQLiteDatabase db = oh.getWritableDatabase();){
                    //open transaction
                    db.beginTransaction();
                    try{

                        //load sql-script from resources
                        for(String sql: MainActivity.db_script_drop) {
                            db.execSQL(sql);
                        }

                        //load sql-script from resources
                        for(String sql: MainActivity.db_script_create) {
                            db.execSQL(sql);
                        }

                        //commit
                        db.setTransactionSuccessful();

                    }finally{
                        //end of transaction. Without setTransactionSuccessful it do rollback. Android's Spezialitaet.
                        db.endTransaction();
                    }

                    //reload db & refresh listviews
                    MainActivity.reLoadTables(activity);
                    FragmentHome.mCardsListViewAdaptor.notifyDataSetChanged();
                    FragmentTags.mTagsListViewAdaptor.notifyDataSetChanged();

                }
                break;
            case R.id.btnExportToSDCard:
                //check permission
                if (ActivityCompat.checkSelfPermission(this.activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //ask permission to sd-card
                    this.activity.requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                } else {

                    if (((AboutActivity)activity).getFileService() == null) {
                        Log.e("!!!!!!!!!!", "getFileService() == null");
                    }

                    ((AboutActivity)activity).getFileService().doExportTagsToSDCard(MainActivity.getTags());
                    MainActivity.showMess(activity, activity.getString(R.string.str_export_to_sdcard_is_ok));
                }

                break;
            default:

        }
    }
}
