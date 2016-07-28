package com.jkfsoft.phrasebook.gui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;

import com.jkfsoft.phrasebook.R;
import com.jkfsoft.phrasebook.logic.DBMgr;
import com.jkfsoft.phrasebook.model.Card;
import com.jkfsoft.phrasebook.utils.IThrRes;

/**
 * Created by Dmitry Sokolyuk on 26.07.2016.
 */
public class EditCardActivity extends AppCompatActivity {

    //curren editing/new Card
    public static Card mCurrentCard = null;

    /**
     * on Create
     * @param savedInstanceState
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_card);

        mCurrentCard = null;

        //search selected Card by id if it edit action
        Bundle b = getIntent().getExtras();
        if (b != null) {
            long id = b.getLong("id");

            for(Card _c: MainActivity.getCards()){
                if (_c.getId() == id) {
                    mCurrentCard = _c;
                    break;
                }
            }
        }

        if (mCurrentCard == null)
            mCurrentCard = new Card(); //it's new action

        //load data
        cardToControls(mCurrentCard);

        ((FloatingActionButton) findViewById(R.id.fab)).setOnClickListener(v->{
            //save data from controls to class Card
            controlsToCard(mCurrentCard);

            if (mCurrentCard.getId() == null) {
                //insert action

                DBMgr.insertCardThr(EditCardActivity.this, mCurrentCard, new IThrRes() {
                    @Override
                    public void onSuccess(Object result) {
                        FragmentHome.mCardsListViewAdaptor.notifyDataSetChanged();
                        finish();
                    }
                    @Override
                    public void onException(Exception e) {
                        MainActivity.showMess(EditCardActivity.this, e.getMessage());
                    }
                });

            } else {
                //update action

                //update in db & set to card status as saved
                DBMgr.updateCardThr(EditCardActivity.this, mCurrentCard, new IThrRes() {
                    @Override
                    public void onSuccess(Object result) {
                        FragmentHome.mCardsListViewAdaptor.notifyDataSetChanged();
                        finish();
                    }
                    @Override
                    public void onException(Exception e) {
                        MainActivity.showMess(EditCardActivity.this, e.getMessage());
                    }
                });
            }
        });


    }

    @Override
    protected void onDestroy() {
        //clean global variable
        mCurrentCard = null;
        super.onDestroy();
    }

    protected void cardToControls(Card c) {

    }

    protected void controlsToCard(Card c) {

    }

    public static Card getCurrentCard() {
        return mCurrentCard;
    }


}

