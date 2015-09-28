package io.keepcoding.everpobre.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import io.keepcoding.everpobre.R;
import io.keepcoding.everpobre.adapters.DataGridAdapter;

public class DataGridFragment extends Fragment {
    public interface OnDataGridFragmentClickListener {
        public void dataGridElementClick(AdapterView<?> parent, View view, int position, long id);
        public boolean dataGridElementLongClick(AdapterView<?> parent, View view, int position, long id);
    }

    private OnDataGridFragmentClickListener listener;
    private DataGridAdapter adapter;
    private GridView gridNotebooks;

    public DataGridFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_data_grid, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void setAdapter(DataGridAdapter adapter) {
        this.adapter = adapter;

        gridNotebooks = (GridView)getActivity().findViewById(R.id.grid_notebooks);

        gridNotebooks.setAdapter(adapter);

        //Set Long-Clickable
        gridNotebooks.setLongClickable(true);
        gridNotebooks.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return listener.dataGridElementLongClick(parent, view, position, id);
            }
        });

        gridNotebooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listener.dataGridElementClick(parent, view, position, id);

            }
        });

    }

    public void setOnDataGridFragmentListener(OnDataGridFragmentClickListener listener) {
        if (listener == null) {
            return;
        }
        this.listener = listener;
    }
}
