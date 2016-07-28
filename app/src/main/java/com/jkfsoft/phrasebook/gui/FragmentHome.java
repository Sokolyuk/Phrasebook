package com.jkfsoft.phrasebook.gui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jkfsoft.phrasebook.R;
import com.jkfsoft.phrasebook.logic.DBMgr;
import com.jkfsoft.phrasebook.logic.listener.IContextMenu;
import com.jkfsoft.phrasebook.model.Card;
import com.jkfsoft.phrasebook.model.CardText;
import com.jkfsoft.phrasebook.utils.IThrRes;

/**
 * Created by Dmitry Sokolyuk on 26.07.2016.
 */
public class FragmentHome extends Fragment {

    //region Navigation
    public static CardsListViewAdaptor mCardsListViewAdaptor;
    public static ListView mCardsListView;
    //endregion


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragmant_home, container, false);

        //region init navigation
        mCardsListViewAdaptor = new CardsListViewAdaptor(getActivity());
        mCardsListView = (ListView)v.findViewById(R.id.listViewCards);
        mCardsListView.setAdapter(mCardsListViewAdaptor);
        registerForContextMenu(mCardsListView);
        //endregion

        return v;
    }

    /**
     * Setup context menu
     *
     * @param menu
     * @param v
     * @param menuInfo
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        ListView lv = (ListView) v;
        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
        Card s = (Card) lv.getItemAtPosition(acmi.position);

        menu.setHeaderTitle(String.format(getString(R.string.str_card_contextmenu_header), String.valueOf(s.getId())));
        menu.add(0, v.getId(), IContextMenu.cmCardEdit, getString(R.string.str_card_contextmenu_edit));
        menu.add(0, v.getId(), IContextMenu.cmCardDelete, getString(R.string.str_card_contextmenu_delete));
    }

    /**
     * Local optimization of source code
     *
     * @param item
     * @return
     */
    protected Card cardByMenuItem(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        return (Card)mCardsListView.getItemAtPosition(info.position);
    }

    /**
     * Handling of context menu events
     *
     * @param item
     * @return
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (getUserVisibleHint()) {

            switch (item.getOrder()){
                case IContextMenu.cmCardEdit:
                    //start EditCard activity for a selected card in db
                    Intent i = new Intent(getActivity(), EditCardActivity.class);
                    i.putExtra("id", cardByMenuItem(item).getId());
                    startActivity(i);
                    break;
                case IContextMenu.cmCardDelete:
                    //async request to delete selected card from db
                    Card c = cardByMenuItem(item);
                    DBMgr.deleteCardThr(getActivity(), c, new IThrRes() {
                        @Override
                        public void onSuccess(Object res) {
                            //deleting is done
                            mCardsListViewAdaptor.notifyDataSetChanged();
                            mCardsListViewAdaptor.notifyDataSetInvalidated();
                            int recaff = res == null?0:(Integer)res;
                            MainActivity.showMess(getContext(), String.format("Card '%s' deleted. Recaff = '%s'", String.valueOf(c.getId()), String.valueOf(recaff)));
                        }
                        @Override
                        public void onException(Exception e) {
                            //when deleting error occured
                            MainActivity.showMess(getContext(), e.getMessage());
                        }
                    });
                    break;
                default:
            }
            return super.onContextItemSelected(item);
        }
        return false;

    }

    /**
     * Adaptor for a card's ListView
     *
     */
    public class CardsListViewAdaptor extends BaseAdapter {
        private LayoutInflater layoutInflater;
        private final Context context;

        // default constructor
        public CardsListViewAdaptor(Context context){
            this.context = context;
            this.layoutInflater = LayoutInflater.from(context);
        }

/*        @Override
        public int getViewTypeCount() {
            return getCount();
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }
*/

        @Override
        public int getCount() {
            if (MainActivity.getCards() == null) {
                return 0;
            } else {
                return MainActivity.getCards().size();
            }
        }

        @Override
        public Object getItem(int position) {
            if (MainActivity.getCards() == null) {
                return null;
            } else {
                return MainActivity.getCards().get(position);
            }

        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            //if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.listview_item_card, parent, false);
            //}

            Card c = (Card)getItem(position);

            ViewGroup data_root = (ViewGroup)convertView.findViewById(R.id.listview_item_cars_data_root);

            //data_root.removeAllViewsInLayout();

            for(CardText ct: c.getCardTexts()){
                addRowText(data_root, ct);
            }

            //((TextView)convertView.findViewById(R.id.listview_item_card_name)).setText(t.getName());

            convertView.setOnClickListener(v->{
                Intent i = new Intent(getActivity(), EditCardActivity.class);
                i.putExtra("id", c.getId());
                startActivity(i);
            });

            convertView.setOnLongClickListener(v->{
                ((Activity)context).openContextMenu(v);
                return true;
            });

            return convertView;
        }

        protected void addRowText(ViewGroup data_root, CardText ct) {
            View v = layoutInflater.inflate(R.layout.listview_item_cars_data_row, null);

            ((TextView)v.findViewById(R.id.card_data_row_lang_name)).setText(ct.getLang().getName());
            ((TextView)v.findViewById(R.id.card_data_row_text)).setText(ct.getText());

            data_root.addView(v);
        }
    }

}
