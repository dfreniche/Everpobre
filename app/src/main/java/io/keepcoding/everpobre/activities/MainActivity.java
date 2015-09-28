package io.keepcoding.everpobre.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.ButterKnife;
import io.keepcoding.everpobre.R;
import io.keepcoding.everpobre.adapters.DataGridAdapter;
import io.keepcoding.everpobre.fragments.DataGridFragment;
import io.keepcoding.everpobre.model.Notebook;
import io.keepcoding.everpobre.model.dao.NotebookDAO;

public class MainActivity extends AppCompatActivity {

    DataGridFragment dataGridFragment;

    DataGridAdapter adapter;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        // insertDummyData();

        dataGridFragment = (DataGridFragment) getFragmentManager().findFragmentById(R.id.grid_fragment);

        refreshData();

    }

    @Override
    protected void onResume() {
        super.onResume();

        refreshData();
    }

    private void refreshData() {
        final NotebookDAO notebookDAO = new NotebookDAO(this);
        cursor = notebookDAO.queryCursor();

        adapter = new DataGridAdapter(this, cursor, new DataGridAdapter.DataGridAdapterElementDataSource() {
            @Override
            public String getDataGridElementTitle(Cursor cursor) {
                Notebook notebook = NotebookDAO.notebookFromCursor(cursor);
                return notebook.getName();
            }

            @Override
            public int getDataGridElementImageResource(Cursor cursor) {
                return R.drawable.notebook;
            }

            @Override
            public int getIconNotebookResourceId() {
                return R.id.icon_notebook;
            }

            @Override
            public int getTitleTextViewResourceId() {
                return R.id.txt_notebook_name;
            }
        });
        dataGridFragment.setAdapter(adapter);
    }

    private void insertDummyData() {
        final NotebookDAO notebookDAO = new NotebookDAO(this);

        for (int i = 0; i < 10; i++) {
            Notebook notebook = new Notebook("To do " + i);
            notebookDAO.insert(notebook);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_main_edit_notebook_action) {
            Intent i = new Intent(this, EditNotebookActivity.class);
            startActivity(i);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
