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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_card);

        Card card = null;
        Bundle b = getIntent().getExtras();

        if (b != null) {
            long id = b.getLong("id");

            for(Card _c: MainActivity.getCards()){
                if (_c.getId() == id) {
                    card = _c;
                    break;
                }
            }

            if (card != null)
                cardToControls(card);
        }

        final Card lcard = card;

        ((FloatingActionButton) findViewById(R.id.fab)).setOnClickListener(v->{

            if (lcard == null) {
                //insert action

                //new Card
                Card tmpCard = new Card();

                //save data from controls to class Card
                controlsToCard(tmpCard);

                DBMgr.insertCardThr(EditCardActivity.this, tmpCard, new IThrRes() {
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

                //save data of controls to cars in MainActivity.mCards
                controlsToCard(lcard);

                //update in db & set to card status as saved
                DBMgr.updateCardThr(EditCardActivity.this, lcard, new IThrRes() {
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

    protected void cardToControls(Card c) {

    }

    protected void controlsToCard(Card c) {

    }



}

