package io.keepcoding.everpobre;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.util.Log;

import io.keepcoding.everpobre.model.Note;
import io.keepcoding.everpobre.model.Notebook;
import io.keepcoding.everpobre.model.dao.NoteDAO;
import io.keepcoding.everpobre.model.dao.NotebookDAO;
import io.keepcoding.everpobre.model.db.DBConstants;
import io.keepcoding.everpobre.provider.EverpobreProvider;


public class EverpobreProviderTests extends AndroidTestCase {

	public void testQueryAllNotebooks() {
		ContentResolver cr = getContext().getContentResolver();
		assertNotNull(cr);

		Cursor cursor = cr.query(EverpobreProvider.NOTEBOOKS_URI, NotebookDAO.allColumns, null, null, null);
		assertNotNull(cursor);
	}

	public void testInsertOneNotebook() {
		ContentResolver cr = getContext().getContentResolver();
		assertNotNull(cr);

		Notebook notebook = new Notebook("To do");
		assertNotNull(notebook);


		Uri uri = cr.insert(EverpobreProvider.NOTEBOOKS_URI, NotebookDAO.getContentValues(notebook));
		assertNotNull(uri);
		Log.d("", uri.toString());

	}

	public void testDeleteOneNotebook() {
		ContentResolver cr = getContext().getContentResolver();
		assertNotNull(cr);

		Notebook notebook = new Notebook("Delete me");
		assertNotNull(notebook);

		Uri uri = cr.insert(EverpobreProvider.NOTEBOOKS_URI, NotebookDAO.getContentValues(notebook));
		assertNotNull(uri);

		// count number of elements after insert
		Cursor cursor = cr.query(EverpobreProvider.NOTEBOOKS_URI, NotebookDAO.allColumns, null, null, null);
		assertNotNull(cursor);
		int numRecords = cursor.getCount();

		cr.delete(uri, null, null);
		cursor = cr.query(EverpobreProvider.NOTEBOOKS_URI, NotebookDAO.allColumns, null, null, null);
		assertNotNull(cursor);

		assertTrue(cursor.getCount()+1 == numRecords);
	}

	public void testDeleteAllNotebooks() {
		ContentResolver cr = getContext().getContentResolver();
		assertNotNull(cr);

		cr.delete(EverpobreProvider.NOTEBOOKS_URI, null, null);
		Cursor cursor = cr.query(EverpobreProvider.NOTEBOOKS_URI, NotebookDAO.allColumns, null, null, null);
		assertNotNull(cursor);

		assertTrue(cursor.getCount() == 0);
	}

	public void testUpdateNotebook() {
		ContentResolver cr = getContext().getContentResolver();
		assertNotNull(cr);

		Notebook notebook = new Notebook("Update me!");
		assertNotNull(notebook);

		Uri uri = cr.insert(EverpobreProvider.NOTEBOOKS_URI, NotebookDAO.getContentValues(notebook));
		assertNotNull(uri);
		Log.d("", uri.toString());

		Cursor c = cr.query(uri, null, null, null, null);
		assertNotNull(c);
		assertEquals(1, c.getCount());
		c.moveToFirst();
		Notebook readNotebook = NotebookDAO.notebookFromCursor(c);
		assertNotNull(readNotebook);
		assertEquals("Update me!", readNotebook.getName());
		c.close();
		c = null;

		readNotebook.setName("Updated!");
		ContentValues values = NotebookDAO.getContentValues(readNotebook);
		int updatedRecords = cr.update(uri, values, null, null);
		assertEquals(1, updatedRecords);

		c = cr.query(uri, null, null, null, null);
		assertNotNull(c);
		assertEquals(1, c.getCount());
		c.moveToFirst();
		readNotebook = NotebookDAO.notebookFromCursor(c);
		c.close();
		c = null;
		assertNotNull(readNotebook);
		assertEquals("Updated!", readNotebook.getName());
	}

	// Notes tests

	public void testInsertNote() {
		ContentResolver cr = getContext().getContentResolver();
		assertNotNull(cr);

		Notebook notebook = new Notebook("Parent");
		assertNotNull(notebook);

		Uri notebookUri = cr.insert(EverpobreProvider.NOTEBOOKS_URI, NotebookDAO.getContentValues(notebook));
		assertNotNull(notebookUri);
		notebook.setId(Integer.valueOf(EverpobreProvider.getIdFromUri(notebookUri)));

		for (int i=0; i<10; i++) {
			Note n = new Note(notebook, "Note " + i);
			Uri insertedUri = cr.insert(EverpobreProvider.NOTES_URI, NoteDAO.getContentValues(n));
			assertNotNull(insertedUri);
		}

		Cursor c = cr.query(EverpobreProvider.NOTES_URI, null, DBConstants.KEY_NOTE_NOTEBOOK + "=" + EverpobreProvider.getIdFromUri(notebookUri), null, null);
		assertNotNull(c);
		assertEquals(10, c.getCount());
	}

	public void testDeleteOneNote() {
		ContentResolver cr = getContext().getContentResolver();
		assertNotNull(cr);

		Notebook notebook = new Notebook("Parent of deleted note");
		assertNotNull(notebook);

		Uri notebookUri = cr.insert(EverpobreProvider.NOTEBOOKS_URI, NotebookDAO.getContentValues(notebook));
		assertNotNull(notebookUri);
		notebook.setId(Integer.valueOf(EverpobreProvider.getIdFromUri(notebookUri)));

		Note n = new Note(notebook, "detele this Note ");
		Uri noteUri = cr.insert(EverpobreProvider.NOTES_URI, NoteDAO.getContentValues(n));
		assertNotNull(noteUri);


		// count number of elements after insert
		Cursor cursor = cr.query(EverpobreProvider.NOTES_URI, NoteDAO.allColumns, null, null, null);
		assertNotNull(cursor);
		int numRecords = cursor.getCount();
		cursor.close();
		cursor = null;

		cr.delete(noteUri, null, null);
		cursor = cr.query(EverpobreProvider.NOTES_URI, NoteDAO.allColumns, null, null, null);
		assertNotNull(cursor);

		assertTrue(cursor.getCount()+1 == numRecords);
	}

	public void testDeleteAllNotes() {
		ContentResolver cr = getContext().getContentResolver();
		assertNotNull(cr);

		cr.delete(EverpobreProvider.NOTES_URI, null, null);
		Cursor cursor = cr.query(EverpobreProvider.NOTES_URI, NoteDAO.allColumns, null, null, null);
		assertNotNull(cursor);

		assertTrue(cursor.getCount() == 0);
	}

	public void testDeleteOneNotebookDeletesAllNotes() {
		ContentResolver cr = getContext().getContentResolver();
		assertNotNull(cr);

		Notebook notebook = new Notebook("Parent");
		assertNotNull(notebook);

		Uri notebookUri = cr.insert(EverpobreProvider.NOTEBOOKS_URI, NotebookDAO.getContentValues(notebook));
		assertNotNull(notebookUri);
		notebook.setId(Integer.valueOf(EverpobreProvider.getIdFromUri(notebookUri)));

		for (int i=0; i<10; i++) {
			Note n = new Note(notebook, "Note from testDeleteOneNotebookDeletesAllNotes" + i);
			Uri insertedUri = cr.insert(EverpobreProvider.NOTES_URI, NoteDAO.getContentValues(n));
			assertNotNull(insertedUri);
		}

		Cursor c = cr.query(EverpobreProvider.NOTES_URI, null, DBConstants.KEY_NOTE_NOTEBOOK + "=" + EverpobreProvider.getIdFromUri(notebookUri), null, null);
		assertNotNull(c);
		assertEquals(10, c.getCount());

		cr.delete(notebookUri, null, null);
		c.close();
		c = null;

		c = cr.query(notebookUri, null, null, null, null);
		assertNotNull(c);
		assertEquals(0, c.getCount());
		c.close();
		c = null;

		c = cr.query(EverpobreProvider.NOTES_URI, NoteDAO.allColumns, DBConstants.KEY_NOTE_NOTEBOOK + "=" + EverpobreProvider.getIdFromUri(notebookUri), null, null);
		assertNotNull(c);
		assertEquals(0, c.getCount());
	}

	public void testUpdateNote() {
		ContentResolver cr = getContext().getContentResolver();
		assertNotNull(cr);

		Notebook notebook = new Notebook("Parent of updated note");
		assertNotNull(notebook);

		Uri notebookUri = cr.insert(EverpobreProvider.NOTEBOOKS_URI, NotebookDAO.getContentValues(notebook));
		assertNotNull(notebookUri);
		notebook.setId(Integer.valueOf(EverpobreProvider.getIdFromUri(notebookUri)));

		Note n = new Note(notebook, "Note from testUpdateNote");
		Uri insertedUri = cr.insert(EverpobreProvider.NOTES_URI, NoteDAO.getContentValues(n));
		assertNotNull(insertedUri);

		Cursor c = cr.query(EverpobreProvider.NOTES_URI, null, DBConstants.KEY_NOTE_NOTEBOOK + "=" + EverpobreProvider.getIdFromUri(notebookUri), null, null);
		assertNotNull(c);
		assertEquals(1, c.getCount());
		
		n.setText("Updated in testUpdateNote");
		cr.update(insertedUri, NoteDAO.getContentValues(n), null, null);
		c = cr.query(insertedUri, null, null, null, null);
		assertNotNull(c);
		assertEquals(1, c.getCount());
		
		c.moveToFirst();
		Note updatedNote = NoteDAO.noteFromCursor(c);
		assertEquals("Updated in testUpdateNote", updatedNote.getText());
	}
	
	//
	// test convenience methods
	//
	
	public void testGetAllNotebooks() {
		Cursor c = EverpobreProvider.getAllNotebooks();
		assertNotNull(c);
		
	}
	
	public void testInsertNotebook() {
		Cursor c = EverpobreProvider.getAllNotebooks();
		assertNotNull(c);
		int numRecords = c.getCount();

		Notebook n = new Notebook("Inserted in testInsertNotebook");
		EverpobreProvider.insertNotebook(n);
		c = EverpobreProvider.getAllNotebooks();
		assertNotNull(c);
		assertEquals(numRecords+1, c.getCount());
	}
	
	public void testDeleteNotebook() {
		EverpobreApp.getAppContext();
		
		Notebook n = new Notebook("Inserted in testInsertNotebook2");
		Uri uri = EverpobreProvider.insertNotebook(n);
		Cursor c = EverpobreProvider.getAllNotebooks();
		assertNotNull(c);
		int numRecords = c.getCount();

		EverpobreProvider.deleteNotebook(n);
		c = EverpobreProvider.getAllNotebooks();
		assertNotNull(c);
		assertEquals(numRecords-1, c.getCount());
	}
	
	public void testDeleteNotebookWithId() {
		Notebook n = new Notebook("Inserted in testInsertNotebook2");
		Uri uri = EverpobreProvider.insertNotebook(n);
		Cursor c = EverpobreProvider.getAllNotebooks();
		assertNotNull(c);
		int numRecords = c.getCount();

		EverpobreProvider.deleteNotebook(n.getId());
		c = EverpobreProvider.getAllNotebooks();
		assertNotNull(c);
		assertEquals(numRecords-1, c.getCount());
	}
	
	public void testInsertNotes() {
		Notebook n = new Notebook("Inserted in testInsertNotebook2");
		Uri uri = EverpobreProvider.insertNotebook(n);

		Cursor c = EverpobreProvider.getAllNotes(n);
		assertNotNull(c);
		assertEquals(0, c.getCount());
		
		Note note = new Note(n, "1st note");
		Uri insertedNote1 = EverpobreProvider.insertNote(n, note);
		assertNotNull(insertedNote1);
		
		c = EverpobreProvider.getAllNotes(n);
		assertNotNull(c);
		assertEquals(1, c.getCount());
		
		Uri insertedNote2 = EverpobreProvider.insertNote(n, "2nd note");
		assertNotNull(insertedNote2);
		c = EverpobreProvider.getAllNotes(n);
		assertNotNull(c);
		assertEquals(2, c.getCount());
		
		c.moveToFirst();
		Note testNote = NoteDAO.noteFromCursor(c);
		assertEquals("1st note", testNote.getText());
		
		c.moveToNext();
		testNote = NoteDAO.noteFromCursor(c);
		assertEquals("2nd note", testNote.getText());
	}
	
	
}
