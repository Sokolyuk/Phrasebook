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
    private FileService fileService;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e("!!!!!!!!!!", "onServiceConnected");
            AboutActivity.this.fileService = ((FileService.FileServiceBinder)service).getService();
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e("!!!!!!!!!!", "onServiceDisconnected");
            AboutActivity.this.fileService = null;
        }
    };

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

    public FileService getFileService() {
        return fileService;
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindFileService();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindFileService();
    }

    private void bindFileService() {
        Log.e("!!!!!!!!!!", "bindFileService");
        this.bindService(new Intent(this, FileService.class), serviceConnection, this.BIND_AUTO_CREATE);
    }

    private void unbindFileService() {
        Log.e("!!!!!!!!!!", "unbindFileService");
        if (fileService != null) {
            fileService.stopSelf();
            unbindService(serviceConnection);
            fileService = null;
        }
    }
}

