package edu.itstep.fullstackserver.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "notes")
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "note_author")
    private String noteAuthor;

    @Column(name = "note_title")
    private String noteTitle;

    @Column(name = "note_text")
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

/*
    public void setId(int id) {
        this.id = id;
    }
*/

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
