package com.jkfsoft.phrasebook.gui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.jkfsoft.phrasebook.R;
import com.jkfsoft.phrasebook.model.CardText;
import com.jkfsoft.phrasebook.model.Lang;
import com.jkfsoft.phrasebook.utils.Edit;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

/**
 * Created by Dmitry Sokolyuk on 28.07.2016.
 */
public class EditCardTextActivity extends AppCompatActivity {

    public static final String PARAM_TEXT = "text";
    public static final String PARAM_LANG_ID = "lang_id";

    private EditText edit_cardText_text;
    private MaterialBetterSpinner edit_cardText_lang;
    private TextInputLayout edit_cardText_textWrapper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_cardtext);

        edit_cardText_text = (EditText)findViewById(R.id.edit_cardtext_text);
        edit_cardText_lang = (MaterialBetterSpinner)findViewById(R.id.edit_cardtext_lang);
        setupStoreSpinner();

        edit_cardText_textWrapper = (TextInputLayout)findViewById(R.id.edit_cardtext_textWrapper);

        CardText cardText = null;
        Bundle b = getIntent().getExtras();

        if (b != null) {
            String tmpText = b.getString(PARAM_TEXT);
            long tmpLang_id = b.getLong(PARAM_LANG_ID);

            if (edit_cardText_text != null) {
                edit_cardText_text.setText(tmpText);
            }

            setSpinnerText(tmpLang_id);
        }

        ((FloatingActionButton) findViewById(R.id.fab)).setOnClickListener(v->{
            if (!validate()) return;
            Lang sl = getLangBySpinner();
            if (sl == null) {
                MainActivity.showMess(this, "Lang is null.");
                return;
            }

            Intent result = new Intent();
            result.putExtra(PARAM_TEXT, edit_cardText_text.getText().toString());
            result.putExtra(PARAM_LANG_ID, sl.getId());
            setResult(Activity.RESULT_OK, result);

            finish();

        });

    }

    /**
     * Search Lang by MainActivity.Langs & set value
     *
     * @param lang_id
     */
    private void setSpinnerText(long lang_id) {
        if (edit_cardText_lang != null) {
            for(Lang l: MainActivity.getLangs()){
                if (l.getId() == lang_id) {
                    edit_cardText_lang.setText(l.getName());
                    break;
                }
            }
        }
    }

    // tmp arr langs
    private String[] stringArr;

    /**
     * Configure Adapter for spinner to select Lang
     */
    private void setupStoreSpinner() {
        stringArr = new String[MainActivity.getLangs().size()];
        for(int i = 0; i < MainActivity.getLangs().size(); i++)
            stringArr[i] = MainActivity.getLangs().get(i).getName();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, stringArr);

        edit_cardText_lang.setAdapter(adapter);
    }

    private Lang getLangBySpinner() {
        String lang_name = edit_cardText_lang.getText().toString();
        if (lang_name == null || lang_name.isEmpty()) return null;

        for(Lang l:  MainActivity.getLangs()) {
            if (l.getName().equals(lang_name)) {
                return l;
            }
        }
        return null;
    }

    protected boolean validate() {
        if (Edit.validateText(this, edit_cardText_text, edit_cardText_textWrapper) &
                Edit.validateSpinner(this, edit_cardText_lang)) {
            return true;
        }
        return false;
    }



}
