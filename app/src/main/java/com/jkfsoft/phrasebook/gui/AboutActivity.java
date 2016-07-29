package com.jkfsoft.phrasebook.gui;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import com.jkfsoft.phrasebook.R;
import com.jkfsoft.phrasebook.Service.FileService;
import com.jkfsoft.phrasebook.logic.listener.AboutActivityOnClickListener;

/**
 * Created by Dmitry Sokolyuk on 26.07.2016.
 */
public class AboutActivity extends AppCompatActivity {

    private Button btnSupportCall;
    private Button btnExportToSDCard;
    private Button btnCleanDB;
    private AboutActivityOnClickListener onClickListener;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        btnSupportCall = (Button)findViewById(R.id.btnSupportCall);
        btnExportToSDCard = (Button)findViewById(R.id.btnExportToSDCard);
        btnCleanDB = (Button)findViewById(R.id.btnCleanDB);

        onClickListener = new AboutActivityOnClickListener(this);

        btnSupportCall.setOnClickListener(onClickListener);
        btnExportToSDCard.setOnClickListener(onClickListener);
        btnCleanDB.setOnClickListener(onClickListener);

    }

}

