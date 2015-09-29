package io.keepcoding.everpobre.fragments;


import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import io.keepcoding.everpobre.R;
import io.keepcoding.everpobre.adapters.DataGridAdapter;

public class DataGridFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>  {
    public interface OnDataGridFragmentClickListener {

        public void dataGridElementClick(AdapterView<?> parent, View view, int position, long id);
        public boolean dataGridElementLongClick(AdapterView<?> parent, View view, int position, long id);
    }
    private OnDataGridFragmentClickListener listener;

    private Uri dataGridUri;
    private String[] projection;
    private String selection;
    private String[] selectionArgs;
    private String sortOrder;

    private DataGridAdapter adapter;
    private GridView gridNotebooks;

    private DataGridAdapter.DataGridAdapterElementDataSource elementDataSource;

    public DataGridFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_data_grid, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        LoaderManager loader = getLoaderManager();
        loader.initLoader(0, null, this);
    }


    public void setAdapter(DataGridAdapter adapter) {
        this.adapter = adapter;
   }

    public void setOnDataGridFragmentListener(OnDataGridFragmentClickListener listener) {
        if (listener == null) {
            return;
        }
        this.listener = listener;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        CursorLoader loader = new CursorLoader(getActivity(), dataGridUri, projection, selection, selectionArgs, sortOrder);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        adapter = new DataGridAdapter(getActivity(), cursor, elementDataSource);

        gridNotebooks = (GridView)getActivity().findViewById(R.id.grid_notebooks);

        gridNotebooks.setAdapter(adapter);

        //Set Long-Clickable
        gridNotebooks.setLongClickable(true);
        gridNotebooks.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (listener != null) {
                    return listener.dataGridElementLongClick(parent, view, position, id);
                } else {
                    return false;
                }
            }
        });

        gridNotebooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listener != null) {
                    listener.dataGridElementClick(parent, view, position, id);
                }
            }
        });
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    public Uri getDataGridUri() {
        return dataGridUri;
    }

    public void setDataGridUri(Uri dataGridUri) {
        this.dataGridUri = dataGridUri;
    }

    public String[] getProjection() {
        return projection;
    }

    public void setProjection(String[] projection) {
        this.projection = projection;
    }

    public String getSelection() {
        return selection;
    }

    public void setSelection(String selection) {
        this.selection = selection;
    }

    public String[] getSelectionArgs() {
        return selectionArgs;
    }

    public void setSelectionArgs(String[] selectionArgs) {
        this.selectionArgs = selectionArgs;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public DataGridAdapter.DataGridAdapterElementDataSource getElementDataSource() {
        return elementDataSource;
    }

    public void setElementDataSource(DataGridAdapter.DataGridAdapterElementDataSource elementDataSource) {
        this.elementDataSource = elementDataSource;
    }
}
