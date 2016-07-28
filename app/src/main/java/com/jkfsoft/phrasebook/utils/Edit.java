package com.jkfsoft.phrasebook.utils;

import android.app.Activity;
import android.support.design.widget.TextInputLayout;
import android.widget.EditText;

import com.jkfsoft.phrasebook.R;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

/**
 * Created by Dmitry Sokolyuk on 20.07.2016.
 */
public class Edit {

    public static boolean validateSpinner(Activity a, MaterialBetterSpinner s) {
        if (s.getText().length() == 0) {
            s.setError(a.getString(R.string.err_field_is_empty));
            //edit_store_name.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    public static boolean validateText(Activity a, EditText e, TextInputLayout w) {
        if (e.getText().length() == 0) {
            w.setError(a.getString(R.string.err_field_is_empty));
            //edit_store_name.requestFocus();
            return false;
        } else {
            w.setErrorEnabled(false);
            return true;
        }
    }

    public static boolean validateDouble(Activity a, EditText e, TextInputLayout w) {
        if (validateText(a, e, w)) {
            try{
                Double.valueOf(e.getText().toString());
                w.setErrorEnabled(false);
                return true;
            }catch(Exception ex){
                w.setError(a.getString(R.string.err_incorrect_double_value));
            }
        }
        return false;
    }



}
