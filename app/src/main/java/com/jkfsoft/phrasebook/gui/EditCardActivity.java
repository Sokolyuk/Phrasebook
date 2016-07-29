package com.jkfsoft.phrasebook.gui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jkfsoft.phrasebook.R;
import com.jkfsoft.phrasebook.logic.DBMgr;
import com.jkfsoft.phrasebook.model.Card;
import com.jkfsoft.phrasebook.model.CardText;
import com.jkfsoft.phrasebook.model.Lang;
import com.jkfsoft.phrasebook.model.Tag;
import com.jkfsoft.phrasebook.utils.IThrRes;

/**
 * Created by Dmitry Sokolyuk on 26.07.2016.
 */
public class EditCardActivity extends AppCompatActivity {

    //curren editing/new Card
    public static Card mCurrentCard = null;

    public static CardTextsListViewAdaptor mCardTextsListViewAdaptor;
    public static ListView mCardTextsListView;

    public static CardTagsListViewAdaptor mCardTagsListViewAdaptor;
    public static ListView mCardTagsListView;

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


        mCardTextsListViewAdaptor = new CardTextsListViewAdaptor(this);
        mCardTextsListView = (ListView)findViewById(R.id.listView_EditCard_CardTexts);
        mCardTextsListView.setAdapter(mCardTextsListViewAdaptor);
        //registerForContextMenu(mCardTextsListView);

        mCardTagsListViewAdaptor = new CardTagsListViewAdaptor(this);
        mCardTagsListView = (ListView)findViewById(R.id.listView_EditCard_Tags);
        mCardTagsListView.setAdapter(mCardTagsListViewAdaptor);
        //registerForContextMenu(mCardTagsListView);


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

    /**
     * Receive result from EditCardTextActivity
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case GUIConsts.ACTIVITY_REQUEST_CODE_EDIT_CARD_TEXT:
                if (resultCode == RESULT_OK) {
                    if(data != null && data.getExtras() != null) {
                        long lang_id = data.getExtras().getLong(EditCardTextActivity.PARAM_LANG_ID);
                        long lang_id_saved = data.getExtras().getLong(EditCardTextActivity.PARAM_LANG_ID_SAVED);
                        String lang_name = data.getExtras().getString(EditCardTextActivity.PARAM_LANG_NAME);
                        String text = data.getExtras().getString(EditCardTextActivity.PARAM_TEXT);
                        boolean isNew = true;
                        for(CardText ct: mCurrentCard.getCardTexts()){
                            if(ct.getLang().getId() == lang_id_saved) {
                                isNew = false;
                                //handle change of Lang
                                if (lang_id_saved != lang_id)
                                    ct.setLang(new Lang(lang_id, lang_name));
                                ct.setText(text);
                                break;
                            }
                        }

                        if (isNew) {
                            mCurrentCard.getCardTexts().add(new CardText(text, lang_id, lang_name));

                        }

                        mCardTextsListViewAdaptor.notifyDataSetChanged();
                    }
                }
                break;
            case GUIConsts.ACTIVITY_REQUEST_CODE_SELECT_TAG:
                break;
            default:
        }
    }

    /**
     * Adaptor for a CardText's ListView
     *
     */
    public class CardTextsListViewAdaptor extends BaseAdapter {
        private LayoutInflater layoutInflater;
        private final Context context;

        // default constructor
        public CardTextsListViewAdaptor(Context context){
            this.context = context;
            this.layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            if (mCurrentCard == null || mCurrentCard.getCardTexts() == null) {
                return 0;
            } else {
                return mCurrentCard.getCardTexts().size();
            }
        }

        @Override
        public Object getItem(int position) {
            if (mCurrentCard.getCardTexts() == null) {
                return null;
            } else {
                return mCurrentCard.getCardTexts().get(position);
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            CardText ct = (CardText)getItem(position);

            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.listview_item_cars_data_row, parent, false);
            }

            ((TextView)convertView.findViewById(R.id.card_data_row_lang_name)).setText(ct.getLang().getName());
            ((TextView)convertView.findViewById(R.id.card_data_row_text)).setText(ct.getText() + " :: " + ct.getDataSaveControlledRowAsString());

            convertView.setOnClickListener(v->{
                Intent intent = new Intent(EditCardActivity.this, EditCardTextActivity.class);
                intent.putExtra(EditCardTextActivity.PARAM_TEXT, ct.getText());
                intent.putExtra(EditCardTextActivity.PARAM_LANG_ID, ct.getLang().getId());
                intent.putExtra(EditCardTextActivity.PARAM_LANG_ID_SAVED, ct.getLang().getId());
                startActivityForResult(intent, GUIConsts.ACTIVITY_REQUEST_CODE_EDIT_CARD_TEXT);
            });

            convertView.setOnLongClickListener(v->{
                ((Activity)context).openContextMenu(v);
                return true;
            });

            return convertView;
        }

    }

    /**
     * Adaptor for a CardTags' ListView
     *
     */
    public class CardTagsListViewAdaptor extends BaseAdapter {
        private LayoutInflater layoutInflater;
        private final Context context;

        // default constructor
        public CardTagsListViewAdaptor(Context context){
            this.context = context;
            this.layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            if (mCurrentCard == null || mCurrentCard.getTags() == null) {
                return 0;
            } else {
                return mCurrentCard.getTags().size();
            }
        }

        @Override
        public Object getItem(int position) {
            if (mCurrentCard.getTags() == null) {
                return null;
            } else {
                return mCurrentCard.getTags().get(position);
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            Tag t = (Tag)getItem(position);

            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.listview_item_tag, parent, false);
            }

            TextView txtName = (TextView)convertView.findViewById(R.id.listview_item_tag_name);
            txtName.setText(t.getName());

            if (t.isDeletedRow()) {
                txtName.setPaintFlags(txtName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                txtName.setPaintFlags(txtName.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            }

            convertView.setOnClickListener(v->{
                if (t.isDeletedRow()) {
                    t.setRowAsUndeleted();
                } else {
                    t.setRowAsDeleted();
                }
            });

            convertView.setOnLongClickListener(v->{
                ((Activity)context).openContextMenu(v);
                return true;
            });

            return convertView;
        }

    }

}

