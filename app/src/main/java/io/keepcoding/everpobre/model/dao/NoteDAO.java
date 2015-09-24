package io.keepcoding.everpobre.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.keepcoding.everpobre.EverpobreApp;
import io.keepcoding.everpobre.model.Note;
import io.keepcoding.everpobre.model.Notebook;
import io.keepcoding.everpobre.model.db.DBHelper;

import static io.keepcoding.everpobre.model.db.DBConstants.KEY_NOTE_ADDRESS;
import static io.keepcoding.everpobre.model.db.DBConstants.KEY_NOTE_CREATION_DATE;
import static io.keepcoding.everpobre.model.db.DBConstants.KEY_NOTE_HAS_COORDINATES;
import static io.keepcoding.everpobre.model.db.DBConstants.KEY_NOTE_ID;
import static io.keepcoding.everpobre.model.db.DBConstants.KEY_NOTE_LATITUDE;
import static io.keepcoding.everpobre.model.db.DBConstants.KEY_NOTE_LONGITUDE;
import static io.keepcoding.everpobre.model.db.DBConstants.KEY_NOTE_MODIFICATION_DATE;
import static io.keepcoding.everpobre.model.db.DBConstants.KEY_NOTE_NOTEBOOK;
import static io.keepcoding.everpobre.model.db.DBConstants.KEY_NOTE_PHOTO_URL;
import static io.keepcoding.everpobre.model.db.DBConstants.KEY_NOTE_TEXT;
import static io.keepcoding.everpobre.model.db.DBConstants.TABLE_NOTE;

public class NoteDAO implements DAOPersistable<Note> {

    public static final String[] allColumns = {
            KEY_NOTE_ID,
            KEY_NOTE_TEXT,
            KEY_NOTE_CREATION_DATE,
            KEY_NOTE_MODIFICATION_DATE,
            KEY_NOTE_PHOTO_URL,
            KEY_NOTE_LATITUDE,
            KEY_NOTE_LONGITUDE,
            KEY_NOTE_HAS_COORDINATES,
            KEY_NOTE_ADDRESS,
            KEY_NOTE_NOTEBOOK
    };

    private Context context;

    public NoteDAO(Context context) {
        this.context = context;
    }

    @Override
    public long insert(@NonNull Note note) {
        if (note == null) {
            return 0;
        }
        // insert
        DBHelper db = DBHelper.getInstance(context);

        long id = db.getWritableDatabase().insert(TABLE_NOTE, null, this.getContentValues(note));

        db.close();
        db=null;

        return id;
    }

    @Override
    public void update(long id, @NonNull Note note) {
        if (note == null) {
            return;
        }

        DBHelper db = DBHelper.getInstance(context);

        db.getWritableDatabase().update(TABLE_NOTE, this.getContentValues(note), KEY_NOTE_ID + "=" + id, null);

        db.close();
        db=null;
    }

    @Override
    public void delete(long id) {
        DBHelper db = DBHelper.getInstance(context);

        db.getWritableDatabase().delete(TABLE_NOTE,  KEY_NOTE_ID + " = " + id, null);

        db.close();
        db=null;
    }

    @Override
    public void deleteAll() {
        DBHelper db = DBHelper.getInstance(context);

        db.getWritableDatabase().delete(TABLE_NOTE,  null, null);

        db.close();
        db=null;
    }

    public static ContentValues getContentValues(Note note) {
        ContentValues content = new ContentValues();
        content.put(KEY_NOTE_TEXT, note.getText());
        content.put(KEY_NOTE_CREATION_DATE, DBHelper.convertDateToLong(note.getCreationDate()));
        content.put(KEY_NOTE_MODIFICATION_DATE, DBHelper.convertDateToLong(note.getModificationDate()));
        content.put(KEY_NOTE_PHOTO_URL, note.getPhotoUrl());
        content.put(KEY_NOTE_NOTEBOOK, note.getNotebook().getId());
        content.put(KEY_NOTE_LATITUDE, note.getLatitude());
        content.put(KEY_NOTE_LONGITUDE, note.getLongitude());
        content.put(KEY_NOTE_HAS_COORDINATES, DBHelper.convertBooleanToInt(note.hasCoordinates()));
        content.put(KEY_NOTE_ADDRESS, note.getAddress());
        return content;
    }


    // convenience method
    public static Note noteFromCursor(Cursor c) {
        assert c != null;

        NotebookDAO notebookDAO = new NotebookDAO(EverpobreApp.getAppContext());
        Notebook notebook = notebookDAO.query(c.getInt(c.getColumnIndex(KEY_NOTE_NOTEBOOK)));
        Note n = new Note(notebook, c.getString(c.getColumnIndex(KEY_NOTE_TEXT)));
        n.setId(c.getInt(c.getColumnIndex(KEY_NOTE_ID)));

        Long creationDate = c.getLong(c.getColumnIndex(KEY_NOTE_CREATION_DATE));
        Long modificationDate = c.getLong(c.getColumnIndex(KEY_NOTE_MODIFICATION_DATE));

        n.setCreationDate(DBHelper.convertLongToDate(creationDate));
        n.setModificationDate(DBHelper.convertLongToDate(modificationDate));

        n.setPhotoUrl(c.getString(c.getColumnIndex(KEY_NOTE_PHOTO_URL)));

        n.setAddress(c.getString(c.getColumnIndex(KEY_NOTE_ADDRESS)));

        boolean hasCoordinates = c.getInt(c.getColumnIndex(KEY_NOTE_HAS_COORDINATES)) == 1 ? true : false;
        n.setHasCoordinates(hasCoordinates);
        if (hasCoordinates) {
            n.setLatitude(c.getFloat(c.getColumnIndex(KEY_NOTE_LATITUDE)));
            n.setLongitude(c.getFloat(c.getColumnIndex(KEY_NOTE_LONGITUDE)));

        }
        return n;
    }

    @Override
    public Cursor queryCursor() {
        // select
        DBHelper db = DBHelper.getInstance(context);

        Cursor c = db.getReadableDatabase().query(TABLE_NOTE, allColumns, null, null, null, null, null);

        return c;
    }


    /**
     * Returns a Note object from its id
     * @param id - the note id in db
     * @return Note object if found, null otherwise
     */
    @Override
    public @Nullable Note query(long id) {
        Note note = null;

        DBHelper db = DBHelper.getInstance(context);

        String where = KEY_NOTE_ID + "=" + id;
        Cursor c = db.getReadableDatabase().query(TABLE_NOTE, allColumns, where, null, null, null, null);
        if (c != null) {
            if (c.getCount() > 0) {
                c.moveToFirst();
                note = noteFromCursor(c);
            }
        }
        c.close();
        db.close();
        return note;
    }


}

