package io.keepcoding.everpobre.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import io.keepcoding.everpobre.R;

public class DataGridAdapter extends CursorAdapter {

    public interface DataGridAdapterElementDataSource {
        public String getDataGridElementTitle(Cursor cursor);
        public int getDataGridElementImageResource(Cursor cursor);
        public int getIconNotebookResourceId();
        public int getTitleTextViewResourceId();
    }

    private LayoutInflater layoutInflater;
    private WeakReference<Context> context;

    private DataGridAdapterElementDataSource dataProvider;

    public DataGridAdapter(Context context, Cursor c, DataGridAdapterElementDataSource dataProvider) {
        super(context, c, 0);
        this.context = new WeakReference<Context>(context);
        this.layoutInflater = LayoutInflater.from(context);
        this.dataProvider = dataProvider;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View v = layoutInflater.inflate(R.layout.view_notebook, parent, false);
        return v;
    }

    /**
     *
     * @param   view
     *          The view in which the elements we set up here will be displayed.
     *
     * @param   context
     *          The running context where this ListView adapter will be active.
     *
     * @param   c
     *          The Cursor containing the query results we will display.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // add defensive code

        ImageView item_image = (ImageView) view.findViewById(dataProvider.getIconNotebookResourceId());
        TextView txtTitle = (TextView)view.findViewById(dataProvider.getTitleTextViewResourceId());

        String title = dataProvider.getDataGridElementTitle(cursor);
        txtTitle.setText(title);

        int imageResourceId = dataProvider.getDataGridElementImageResource(cursor);
        if (imageResourceId > 0) {
            item_image.setImageResource(imageResourceId);
        }

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }
}
