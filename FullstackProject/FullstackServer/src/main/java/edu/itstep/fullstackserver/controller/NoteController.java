package edu.itstep.fullstackserver.controller;

import edu.itstep.fullstackserver.entity.Note;
import edu.itstep.fullstackserver.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class NoteController {

    @Autowired
    private NoteService noteService;

    @GetMapping("/notes")
    public List<Note> getAllNotes() {
        return noteService.getAllNotes();
    }

    @GetMapping("/notes/{id}")
    public Note getNoteById(@PathVariable int id) {
        return noteService.getNoteById(id);
    }

    @PostMapping("/notes")
    public Note addNote(@RequestBody Note note) {
        return noteService.saveOrUpdateNote(note);
    }

    @PutMapping("/notes")
    public Note updateNote(@RequestBody Note note) {
        return noteService.saveOrUpdateNote(note);
    }

    
    @DeleteMapping("/notes/{id}")
    public Note deleteNoteById(@PathVariable int id) {
        Note note = getNoteById(id);
        if (note != null) {
            noteService.deleteNoteById(id);
        }else{
            note = new Note();
        }
        return note;
    }

/*
    @DeleteMapping("/notes/{id}")
    public String deleteNoteById(@PathVariable int id) {

        // у разі повернення методом повідомлення у вигляді рядка, при використанні деяких технологій на боці клієнта
        // (наприклад Retrofit2) можуть генеруватись помилки, пов’язані з тим, що відповідь отримуватиметься не у формі JSON, а у вигляді HTML

        // Алгоритм видалення з попередньою перевіркою наявності даних та перевіркою результатів видалення
        Note note = getNoteById(id);
        String message;
        if (note != null) {
            noteService.deleteNoteById(id);
            if (getNoteById(id) == null) {
                message = "Element with id = " + id + " was deleted";
            } else {
                message = "The element with id = " + id + " was not deleted. Try again later";
            }
        } else {
            message = "The element with the specified id = " + id + " is absent in the database";
        }
        return message;
    }
*/

}
