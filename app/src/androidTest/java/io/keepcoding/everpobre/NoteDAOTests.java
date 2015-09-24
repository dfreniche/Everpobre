package io.keepcoding.everpobre;

import android.database.Cursor;
import android.test.AndroidTestCase;

import io.keepcoding.everpobre.model.Note;
import io.keepcoding.everpobre.model.Notebook;
import io.keepcoding.everpobre.model.dao.NoteDAO;
import io.keepcoding.everpobre.model.dao.NotebookDAO;
import io.keepcoding.everpobre.model.db.DBConstants;


public class NoteDAOTests extends AndroidTestCase {
    public void testInsert() {
        NotebookDAO notebookDao = new NotebookDAO(getContext());
        Notebook notebook = new Notebook("Notebook");
        notebookDao.insert(notebook);

        NoteDAO noteDao = new NoteDAO(getContext());
        Note note = new Note(notebook, "To dos");
        Cursor c = noteDao.queryCursor();
        assertNotNull(c);

        int numRecords = c.getCount();
        assertNotNull(notebook);
        assertNotNull(noteDao);

        noteDao.insert(note);
        c = null;
        c = noteDao.queryCursor();
        assertTrue(numRecords + 1 == c.getCount());
    }

    public void testDelete() {
        NotebookDAO notebookDao = new NotebookDAO(getContext());
        Notebook notebook = new Notebook("Notebook");
        notebookDao.insert(notebook);

        NoteDAO noteDao = new NoteDAO(getContext());
        assertNotNull(noteDao);

        for (int i = 0; i < 10; i++) {
            Note note = new Note(notebook, "To do " + i);
            assertNotNull(note);
            noteDao.insert(note);
        }

        Cursor c = noteDao.queryCursor();
        assertNotNull(c);

        int numRecords = c.getCount();
        assertTrue(numRecords > 0);

        c.moveToFirst();
        noteDao.delete(c.getLong(c.getColumnIndex(DBConstants.KEY_NOTE_ID)));
        c.close();

        c = noteDao.queryCursor();

        assertTrue(numRecords-1 == c.getCount());
    }

    public void testDeleteAll() {
        NoteDAO noteDao = new NoteDAO(getContext());
        assertNotNull(noteDao);

        noteDao.deleteAll();

        Cursor c = noteDao.queryCursor();
        assertNotNull(c);

        int numRecords = c.getCount();
        assertTrue(numRecords == 0);
    }

    public void testUpdateNote() {
        NotebookDAO notebookDao = new NotebookDAO(getContext());
        Notebook notebook = new Notebook("Notebook");
        notebookDao.insert(notebook);

        NoteDAO noteDao = new NoteDAO(getContext());
        assertNotNull(noteDao);

        Note note = new Note(notebook, "Update me! ");
        assertNotNull(note);
        long insertedId = noteDao.insert(note);

        note = null;

        note = noteDao.query(insertedId);
        assertNotNull(note);

        note.setText("Updated!");
        noteDao.update(insertedId, note);

        note = null;
        note = noteDao.query(insertedId);
        assertNotNull(note);
        assertEquals("Updated!", note.getText());
    }
}
