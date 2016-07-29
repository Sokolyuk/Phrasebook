package com.jkfsoft.phrasebook.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.jkfsoft.phrasebook.R;
import com.jkfsoft.phrasebook.model.Tag;
import com.jkfsoft.phrasebook.utils.Edit;

/**
 * Created by Dmitry Sokolyuk on 26.07.2016.
 */
public class EditTagActivity extends AppCompatActivity {

    public static final String PARAM_NAME = "name";
    public static final String PARAM_ID = "id";

    private EditText edit_tag_name;
    private TextInputLayout edit_tag_nameWrapper;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tag);

        edit_tag_name = (EditText)findViewById(R.id.edit_tag_name);
        edit_tag_nameWrapper = (TextInputLayout)findViewById(R.id.edit_tag_nameWrapper);

        Tag tag = null;

        Long tmpTag_id = null;

        if (getIntent() != null && getIntent().getExtras() != null) {

            Bundle b = getIntent().getExtras();

            String tmpText = b.getString(PARAM_NAME);
            tmpTag_id = b.getLong(PARAM_ID);

            if (edit_tag_name != null) {
                edit_tag_name.setText(tmpText);
            }

        }

        final Long ltmpTag_id = tmpTag_id;

        ((FloatingActionButton) findViewById(R.id.fab)).setOnClickListener(v->{
            if (!validate()) return;

            Intent result = new Intent();
            result.putExtra(PARAM_NAME, edit_tag_name.getText().toString());
            result.putExtra(PARAM_ID, ltmpTag_id);
            setResult(RESULT_OK, result);

            finish();

        });

    }

    protected boolean validate() {
        if (Edit.validateText(this, edit_tag_name, edit_tag_nameWrapper)) {
            return true;
        }
        return false;
    }

}
