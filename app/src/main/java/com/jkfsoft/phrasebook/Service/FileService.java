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

    public void doExportTagsToSDCard(List<Tag> tags) {
        try{
            String state = Environment.getExternalStorageState();

            if(state.equals(Environment.MEDIA_MOUNTED)) {
                File fileExternal = Environment.getExternalStorageDirectory();
                String path = fileExternal.getAbsolutePath() + File.separator + getString(R.string.app_name) + File.separator;
                File dir = new File(path);
                if (!dir.mkdirs()) throw new Exception(getString(R.string.err_cant_mkdirs));
                File f = new File(dir, getString(R.string.str_export_file_name));
                try(FileOutputStream fos = new FileOutputStream(f);
                    OutputStreamWriter osw = new OutputStreamWriter(fos);){
                    for(Tag t: tags){
                        osw.write(t.toCSVExport());
                    }
                }
            }

        }catch(Exception e){
            Log.e(this.getClass().getSimpleName(), e.getMessage());
        }

    }

}
