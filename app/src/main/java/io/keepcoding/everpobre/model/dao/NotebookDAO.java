package io.keepcoding.everpobre.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import io.keepcoding.everpobre.model.Notebook;
import io.keepcoding.everpobre.model.db.DBHelper;

import static io.keepcoding.everpobre.model.db.DBConstants.*;

public class NotebookDAO {
    public static final String[] allColumns = {
            KEY_NOTEBOOK_ID,
            KEY_NOTEBOOK_NAME,
            KEY_NOTEBOOK_CREATION_DATE,
            KEY_NOTEBOOK_MODIFICATION_DATE
    };

    private Context context;

    public NotebookDAO(Context context) {
        this.context = context;
    }

    public long insert(Notebook notebook) {
        if (notebook == null) {
            return 0;
        }
        // insert
        DBHelper db = DBHelper.getInstance(context);

        long id = db.getWritableDatabase().insert(TABLE_NOTEBOOK, null, this.getContentValues(notebook));
        notebook.setId(id);
        db.close();
        db=null;

        return id;
    }

    public void update(long id, Notebook notebook) {
        if (notebook == null) {
            return;
        }

        DBHelper db = DBHelper.getInstance(context);

        db.getWritableDatabase().update(TABLE_NOTEBOOK, this.getContentValues(notebook), KEY_NOTEBOOK_ID + "=" + id, null);

        db.close();
        db=null;
    }

    public void delete(long id) {
        DBHelper db = DBHelper.getInstance(context);

        db.getWritableDatabase().delete(TABLE_NOTEBOOK,  KEY_NOTEBOOK_ID + " = " + id, null);

        db.close();
        db=null;
    }

    public void deleteAll() {
        DBHelper db = DBHelper.getInstance(context);

        db.getWritableDatabase().delete(TABLE_NOTEBOOK,  null, null);

        db.close();
        db=null;
    }

    public static ContentValues getContentValues(Notebook notebook) {
        ContentValues content = new ContentValues();
        content.put(KEY_NOTEBOOK_NAME, notebook.getName());
        //content.put(KEY_NOTEBOOK_ID, notebook.getId());
        content.put(KEY_NOTEBOOK_CREATION_DATE, DBHelper.convertDateToLong(notebook.getCreationDate()));
        content.put(KEY_NOTEBOOK_MODIFICATION_DATE, DBHelper.convertDateToLong(notebook.getModificationDate()));

        return content;
    }


    // convenience method
    public static Notebook notebookFromCursor(Cursor c) {
        assert c != null;

        Notebook n = new Notebook(c.getString(c.getColumnIndex(KEY_NOTEBOOK_NAME)));
        n.setId(c.getInt(c.getColumnIndex(KEY_NOTEBOOK_ID)));

        Long creationDate = c.getLong(c.getColumnIndex(KEY_NOTEBOOK_CREATION_DATE));
        Long modificationDate = c.getLong(c.getColumnIndex(KEY_NOTEBOOK_MODIFICATION_DATE));

        n.setCreationDate(DBHelper.convertLongToDate(creationDate));
        n.setModificationDate(DBHelper.convertLongToDate(modificationDate));

        return n;
    }

    public Cursor queryCursor() {
        // select
        DBHelper db = DBHelper.getInstance(context);

        Cursor c = db.getReadableDatabase().query(TABLE_NOTEBOOK, allColumns, null, null, null, null, null);

        return c;
    }


    /**
     * Returns a Notebook object from its id
     * @param id - the notebook id in db
     * @return Notebook object if found, null otherwise
     */
    public Notebook query(long id) {
        Notebook notebook = null;

        DBHelper db = DBHelper.getInstance(context);

        String where = KEY_NOTEBOOK_ID + "=" + id;
        Cursor c = db.getReadableDatabase().query(TABLE_NOTEBOOK, allColumns, where, null, null, null, null);
        if (c != null) {
            if (c.getCount() > 0) {
                c.moveToFirst();
                notebook = notebookFromCursor(c);
            }
        }
        c.close();
        db.close();
        return notebook;
    }



}
