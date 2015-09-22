package io.keepcoding.everpobre.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Notebook {
    private long id;
    private String name;
    private Date creationDate;
    private Date modificationDate;
    private List<Note> notes;


    public Notebook(String name) {
        this.name = name;
        this.creationDate = new Date();
    }

    public List<Note> allNotes() {
        if (notes == null) {
            notes = new ArrayList<Note>();
        }

        return notes;
    }

    public void addNote(Note note) {
        allNotes().add(note);
    }

    public void addNote(String noteText) {
        Note note = new Note(this, noteText);
        addNote(note);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }
}
