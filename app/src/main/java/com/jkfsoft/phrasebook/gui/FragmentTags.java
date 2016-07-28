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
import com.jkfsoft.phrasebook.model.Tag;
import com.jkfsoft.phrasebook.utils.IThrRes;

/**
 * Created by Dmitry Sokolyuk on 26.07.2016.
 */
public class FragmentTags extends Fragment {

    //region Navigation
    public static TagsListViewAdaptor mTagsListViewAdaptor;
    public static ListView mTagsListView;
    //endregion

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragmant_tags, container, false);

        //region init navigation
        mTagsListViewAdaptor = new TagsListViewAdaptor(getActivity());
        mTagsListView = (ListView)v.findViewById(R.id.listViewTags);
        mTagsListView.setAdapter(mTagsListViewAdaptor);
        registerForContextMenu(mTagsListView);
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
        Tag s = (Tag) lv.getItemAtPosition(acmi.position);

        menu.setHeaderTitle(String.format(getString(R.string.str_tag_contextmenu_header), s.getName()));
        menu.add(0, v.getId(), IContextMenu.cmTagEdit, getString(R.string.str_tag_contextmenu_edit));
        menu.add(0, v.getId(), IContextMenu.cmTagDelete, getString(R.string.str_tag_contextmenu_delete));
    }

    /**
     * Local optimization of source code
     *
     * @param item
     * @return
     */
    protected Tag tagByMenuItem(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        return (Tag) mTagsListView.getItemAtPosition(info.position);
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
                case IContextMenu.cmTagEdit:
                    //start EditTag activity for a selected tag in db
                    Intent i = new Intent(getActivity(), EditTagActivity.class);
                    i.putExtra("id", tagByMenuItem(item).getId());
                    startActivity(i);
                    break;
                case IContextMenu.cmTagDelete:
                    //async request to delete selected tag from db
                    Tag t = tagByMenuItem(item);
                    DBMgr.deleteTagThr(getActivity(), t, new IThrRes() {
                        @Override
                        public void onSuccess(Object res) {
                            //deleting is done
                            mTagsListViewAdaptor.notifyDataSetChanged();
                            MainActivity.showMess(getContext(), String.format(getString(R.string.str_tag_deleted), t.getName()));
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
     * Adaptor for a tag's ListView
     *
     */
    public class TagsListViewAdaptor extends BaseAdapter {
        private LayoutInflater layoutInflater;
        private final Context context;

        // default constructor
        public TagsListViewAdaptor(Context context){
            this.context = context;
            this.layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            if (MainActivity.getTags() == null) {
                return 0;
            } else {
                return MainActivity.getTags().size();
            }
        }

        @Override
        public Object getItem(int position) {
            if (MainActivity.getTags() == null) {
                return null;
            } else {
                return MainActivity.getTags().get(position);
            }

        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.listview_item_tag, parent, false);
            }

            Tag t = (Tag)getItem(position);
            ((TextView)convertView.findViewById(R.id.listview_item_tag_name)).setText(t.getName());

            convertView.setOnClickListener(v->{
                Intent i = new Intent(getActivity(), EditTagActivity.class);
                i.putExtra("id", t.getId());
                startActivity(i);
            });

            convertView.setOnLongClickListener(v->{
                ((Activity)context).openContextMenu(v);
                return true;
            });

            return convertView;
        }
    }

}
