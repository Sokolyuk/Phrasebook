package com.jkfsoft.phrasebook.Service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.jkfsoft.phrasebook.R;
import com.jkfsoft.phrasebook.model.Tag;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

/**
 * Created by Dmitry Sokolyuk on 28.07.2016.
 */
public class FileService extends Service {

    public class FileServiceBinder extends Binder {
        public FileService getService() {
            // Return this instance of LocalService so clients can call public methods
            return FileService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new FileServiceBinder();
    }


}
