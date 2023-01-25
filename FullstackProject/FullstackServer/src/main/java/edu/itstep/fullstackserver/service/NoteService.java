package edu.itstep.fullstackserver.service;

import edu.itstep.fullstackserver.entity.Note;

import java.util.List;

public interface NoteService {

    List<Note>getAllNotes();

    Note saveOrUpdateNote(Note note);

    Note getNoteById(int id);

    void deleteNoteById(int id);
}
