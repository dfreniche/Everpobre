package io.keepcoding.everpobre.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import butterknife.ButterKnife;
import io.keepcoding.everpobre.R;
import io.keepcoding.everpobre.adapters.DataGridAdapter;
import io.keepcoding.everpobre.fragments.DataGridFragment;
import io.keepcoding.everpobre.model.Notebook;
import io.keepcoding.everpobre.model.dao.NotebookDAO;
import io.keepcoding.everpobre.provider.EverpobreProvider;
import io.keepcoding.everpobre.util.Constants;

public class MainActivity extends AppCompatActivity {

    DataGridFragment dataGridFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        dataGridFragment = (DataGridFragment) getFragmentManager().findFragmentById(R.id.grid_fragment);

        dataGridFragment.setDataGridUri(EverpobreProvider.NOTEBOOKS_URI);

        dataGridFragment.setOnDataGridFragmentListener(new DataGridFragment.OnDataGridFragmentClickListener() {
            @Override
            public void dataGridElementClick(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public boolean dataGridElementLongClick(AdapterView<?> parent, View view, int position, long id) {
                final NotebookDAO notebookDAO = new NotebookDAO(getBaseContext());

                Notebook notebook = notebookDAO.query(id);
                Intent i = new Intent(MainActivity.this, EditNotebookActivity.class);
                i.putExtra(Constants.intent_key_notebook_id, notebook.getId());
                startActivity(i);

                return false;
            }
        });

        dataGridFragment.setElementDataSource(new DataGridAdapter.DataGridAdapterElementDataSource() {
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
    }

    @Override
    protected void onResume() {
        super.onResume();
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
