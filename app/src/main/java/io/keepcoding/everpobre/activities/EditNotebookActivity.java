package io.keepcoding.everpobre.activities;

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

public class EditNotebookActivity extends AppCompatActivity {

    @Bind (R.id.edit_notebook_name) EditText editNotebookName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_notebook);

        ButterKnife.bind(this);
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

                String name = "" + editNotebookName.getText();
                if ("".equals(name)) {
                    Toast.makeText(this, "We need a name", Toast.LENGTH_SHORT).show();
                    return true;
                }

                NotebookDAO notebookDao = new NotebookDAO(this);

                Notebook notebook = new Notebook(name);
                notebookDao.insert(notebook);

                finish();
                break;
        }
        return super.onOptionsItemSelected(item);

    }
}
