package io.keepcoding.everpobre.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;

import io.keepcoding.everpobre.model.Notebook;
import io.keepcoding.everpobre.model.db.DBHelper;

import static io.keepcoding.everpobre.model.db.DBConstants.*;

public class NotebookDAO implements DAOPersistable<Notebook>{
    public static final String[] allColumns = {
            KEY_NOTEBOOK_ID,
            KEY_NOTEBOOK_NAME,
            KEY_NOTEBOOK_CREATION_DATE,
            KEY_NOTEBOOK_MODIFICATION_DATE
    };

    private WeakReference<Context> context;

    public NotebookDAO(@NonNull Context context) {
        this.context = new WeakReference<Context>(context);
    }

    @Override
    public long insert(@NonNull Notebook notebook) {
        if (notebook == null) {
            return 0;
        }
        // insert
        DBHelper db = DBHelper.getInstance(context.get());

        long id = db.getWritableDatabase().insert(TABLE_NOTEBOOK, null, this.getContentValues(notebook));
        notebook.setId(id);
        db.close();
        db=null;

        return id;
    }

    @Override
    public void update(long id, @NonNull Notebook notebook) {
        if (notebook == null) {
            return;
        }

        DBHelper db = DBHelper.getInstance(context.get());

        db.getWritableDatabase().update(TABLE_NOTEBOOK, this.getContentValues(notebook), KEY_NOTEBOOK_ID + "=" + id, null);

        db.close();
        db=null;
    }

    @Override
    public void delete(long id) {
        DBHelper db = DBHelper.getInstance(context.get());

        db.getWritableDatabase().delete(TABLE_NOTEBOOK,  KEY_NOTEBOOK_ID + " = " + id, null);

        db.close();
        db=null;
    }

    @Override
    public void deleteAll() {
        DBHelper db = DBHelper.getInstance(context.get());

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

    public @NonNull Cursor queryCursor() {
        // select
        DBHelper db = DBHelper.getInstance(context.get());

        Cursor c = db.getReadableDatabase().query(TABLE_NOTEBOOK, allColumns, null, null, null, null, null);

        return c;
    }


    /**
     * Returns a Notebook object from its id
     * @param id - the notebook id in db
     * @return Notebook object if found, null otherwise
     */
    @Override
    public @Nullable Notebook query(long id) {
        Notebook notebook = null;

        DBHelper db = DBHelper.getInstance(context.get());

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
