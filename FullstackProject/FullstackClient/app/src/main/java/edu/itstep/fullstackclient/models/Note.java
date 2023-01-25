package edu.itstep.fullstackclient.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Note implements Serializable {

    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("noteAuthor")
    private String noteAuthor;

    @Expose
    @SerializedName("noteTitle")
    private String noteTitle;

    @Expose
    @SerializedName("noteText")
    private String noteText;

    public Note() {
    }

    public Note(String noteAuthor, String noteTitle, String noteText) {
        this.noteAuthor = noteAuthor;
        this.noteTitle = noteTitle;
        this.noteText = noteText;
    }

    public int getId() {
        return id;
    }

//    public void setId(int id) {
//        this.id = id;
//    }

    public String getNoteAuthor() {
        return noteAuthor;
    }

    public void setNoteAuthor(String noteAuthor) {
        this.noteAuthor = noteAuthor;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", noteAuthor='" + noteAuthor + '\'' +
                ", noteTitle='" + noteTitle + '\'' +
                ", noteText='" + noteText + '\'' +
                '}';
    }
}
