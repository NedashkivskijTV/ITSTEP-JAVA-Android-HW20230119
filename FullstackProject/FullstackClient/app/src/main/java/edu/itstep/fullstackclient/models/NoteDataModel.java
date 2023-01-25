package edu.itstep.fullstackclient.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NoteDataModel implements Serializable {

    private String userName;

    private List<Note> noteList = new ArrayList<>();

    private int noteId;

    public NoteDataModel() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<Note> getNoteList() {
        return noteList;
    }

    public void setNoteList(List<Note> noteList) {
        this.noteList = noteList;
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }
}
