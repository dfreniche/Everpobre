package io.keepcoding.everpobre.activities;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.keepcoding.everpobre.R;
import io.keepcoding.everpobre.model.Notebook;
import io.keepcoding.everpobre.model.dao.NotebookDAO;
import io.keepcoding.everpobre.provider.EverpobreProvider;
import io.keepcoding.everpobre.util.Constants;

public class EditNotebookActivity extends AppCompatActivity {
    private enum EditMode {
        ADDING,
        EDITING,
        DELETING
    }
    private long notebookId;
    Notebook notebookToEdit;
    EditMode mode;

    @Bind (R.id.edit_notebook_name) EditText editNotebookName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_notebook);

        ButterKnife.bind(this);

        Intent i = getIntent();
        notebookId = i.getLongExtra(Constants.intent_key_notebook_id, -1);
        if (notebookId == -1) {
            mode = EditMode.ADDING;
        } else {
            mode = EditMode.EDITING;
            notebookToEdit = new NotebookDAO(this).query(notebookId);
            if (notebookToEdit != null) {
                editNotebookName.setText(notebookToEdit.getName());
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_notebook, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit_notebook_save:
                saveNotebook();
                finish();

                break;

            case R.id.action_delete_notebook:
                deleteNotebook();
                finish();

                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    private void deleteNotebook() {
        EverpobreProvider.deleteNotebook(notebookId);
    }

    private void saveNotebook() {
        ContentResolver cr = getContentResolver();

        // NotebookDAO notebookDao = new NotebookDAO(this);
        String name = "" + editNotebookName.getText();
        if ("".equals(name)) {
            Toast.makeText(this, "We need a name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mode == EditMode.ADDING) {
            Notebook notebook = new Notebook(name);
            cr.insert(EverpobreProvider.NOTEBOOKS_URI, NotebookDAO.getContentValues(notebook));
            //notebookDao.insert(notebook);
        } else {
            notebookToEdit.setName(name);
            String sUri = EverpobreProvider.NOTEBOOKS_URI.toString() + "/" + notebookToEdit.getId();
            Uri uri = Uri.parse(sUri);
            cr.update(uri, NotebookDAO.getContentValues(notebookToEdit), null, null);
            //notebookDao.update(notebookId, notebookToEdit);
        }
    }
}
