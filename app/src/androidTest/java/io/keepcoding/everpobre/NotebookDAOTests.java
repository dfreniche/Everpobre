package io.keepcoding.everpobre;

import android.database.Cursor;
import android.test.AndroidTestCase;

import io.keepcoding.everpobre.model.Notebook;
import io.keepcoding.everpobre.model.dao.NotebookDAO;
import io.keepcoding.everpobre.model.db.DBConstants;

public class NotebookDAOTests extends AndroidTestCase {

    public void testInsert() {
        NotebookDAO notebookDao = new NotebookDAO(getContext());
        Notebook notebook = new Notebook("To dos");
        Cursor c = notebookDao.queryCursor();
        assertNotNull(c);

        int numRecords = c.getCount();
        assertNotNull(notebook);
        assertNotNull(notebookDao);

        notebookDao.insert(notebook);
        c = null;
        c = notebookDao.queryCursor();
        assertTrue(numRecords + 1 == c.getCount());
    }

    public void testTwoInsertsWork() {
        NotebookDAO notebookDao = new NotebookDAO(getContext());
        assertNotNull(notebookDao);

        Cursor c = notebookDao.queryCursor();
        assertNotNull(c);

        int numRecords = c.getCount();

        Notebook notebook = new Notebook("1st insert");
        notebookDao.insert(notebook);

        Cursor c2 = notebookDao.queryCursor();
        assertTrue(numRecords + 1 == c2.getCount());

        Notebook notebook2 = new Notebook("2nd insert");
        notebookDao.insert(notebook2);

        Cursor c3 = notebookDao.queryCursor();
        assertTrue(numRecords + 2 == c3.getCount());
    }


    public void testDelete() {
        NotebookDAO notebookDao = new NotebookDAO(getContext());
        assertNotNull(notebookDao);

        for (int i = 0; i < 10; i++) {
            Notebook notebook = new Notebook("To do " + i);
            assertNotNull(notebook);
            notebookDao.insert(notebook);
        }

        Cursor c = notebookDao.queryCursor();
        assertNotNull(c);

        int numRecords = c.getCount();
        assertTrue(numRecords > 0);

        c.moveToFirst();
        notebookDao.delete(c.getLong(c.getColumnIndex(DBConstants.KEY_NOTEBOOK_ID)));
        c.close();

        c = notebookDao.queryCursor();

        assertTrue(numRecords-1 == c.getCount());
    }

    public void testDeleteAll() {
        NotebookDAO notebookDao = new NotebookDAO(getContext());
        assertNotNull(notebookDao);

        notebookDao.deleteAll();

        Cursor c = notebookDao.queryCursor();
        assertNotNull(c);

        int numRecords = c.getCount();
        assertTrue(numRecords == 0);
    }


}
