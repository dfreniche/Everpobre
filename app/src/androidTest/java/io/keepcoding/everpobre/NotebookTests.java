package io.keepcoding.everpobre;

import android.test.AndroidTestCase;

import io.keepcoding.everpobre.model.Note;
import io.keepcoding.everpobre.model.Notebook;

public class NotebookTests extends AndroidTestCase {
    public void testCanCreateNotebook() {
        Notebook notebook = new Notebook("To do");

        assertNotNull(notebook);
        assertEquals("To do", notebook.getName());
        assertNotNull(notebook.getCreationDate());
    }

    public void testNotebookAddNotes() {
        Notebook notebook = new Notebook("To do");

        assertNotNull(notebook);

        notebook.addNote("First note");
        notebook.addNote(new Note(notebook, "second"));

        assertNotNull(notebook.allNotes());
        assertEquals(notebook.allNotes().get(0).getText(), "First note");
    }
}

