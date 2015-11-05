package io.keepcoding.everpobre.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import io.keepcoding.everpobre.R;
import io.keepcoding.everpobre.adapters.DataGridAdapter;
import io.keepcoding.everpobre.fragments.DataGridFragment;
import io.keepcoding.everpobre.model.Note;
import io.keepcoding.everpobre.model.dao.NoteDAO;
import io.keepcoding.everpobre.model.db.DBConstants;
import io.keepcoding.everpobre.provider.EverpobreProvider;
import io.keepcoding.everpobre.util.Constants;

public class ShowNotebookActivity extends AppCompatActivity {

    DataGridFragment dataGridFragment;
    private long notebookId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_notebook);

        dataGridFragment = (DataGridFragment) getFragmentManager().findFragmentById(R.id.grid_fragment);

        dataGridFragment.setDataGridUri(EverpobreProvider.NOTES_URI);

        Intent i = getIntent();
        notebookId = i.getLongExtra(Constants.intent_key_notebook_id, -1);
        dataGridFragment.setSelection(DBConstants.KEY_NOTE_NOTEBOOK + "=" + notebookId);

        dataGridFragment.setOnDataGridFragmentListener(new DataGridFragment.OnDataGridFragmentClickListener() {
            @Override
            public void dataGridElementClick(AdapterView<?> parent, View view, int position, long id) {
//                final NotebookDAO notebookDAO = new NotebookDAO(getBaseContext());
//                Notebook notebook = notebookDAO.query(id);
//
//                Intent i = new Intent(MainActivity.this, ShowNotebookActivity.class);
//                i.putExtra(Constants.intent_key_notebook_id, notebook.getId());
//                startActivity(i);
            }

            @Override
            public boolean dataGridElementLongClick(AdapterView<?> parent, View view, int position, long id) {
//                final NotebookDAO notebookDAO = new NotebookDAO(getBaseContext());
//
//                Notebook notebook = notebookDAO.query(id);
//                Intent i = new Intent(MainActivity.this, EditNotebookActivity.class);
//                i.putExtra(Constants.intent_key_notebook_id, notebook.getId());
//                startActivity(i);

                return false;
            }
        });

        dataGridFragment.setElementDataSource(new DataGridAdapter.DataGridAdapterElementDataSource() {
            @Override
            public String getDataGridElementTitle(Cursor cursor) {
                Note note = NoteDAO.noteFromCursor(cursor);
                return note.getText();
            }

            @Override
            public int getDataGridElementImageResource(Cursor cursor) {
                return R.drawable.note;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_show_notebook, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
