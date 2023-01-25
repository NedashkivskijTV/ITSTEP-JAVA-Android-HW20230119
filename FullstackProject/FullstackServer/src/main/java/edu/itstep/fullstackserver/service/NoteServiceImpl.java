package edu.itstep.fullstackserver.service;

import edu.itstep.fullstackserver.entity.Note;
import edu.itstep.fullstackserver.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class NoteServiceImpl implements NoteService{

    @Autowired
    private NoteRepository noteRepository;

    @Override
    @Transactional
    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    @Override
    @Transactional
    public Note saveOrUpdateNote(Note note) {
        return noteRepository.save(note);
    }

    @Override
    @Transactional
    public Note getNoteById(int id) {
        Optional<Note> optional = noteRepository.findById(id);
//        if(optional.isPresent()){
//            return optional.get();
//        }
//        return null;
        return optional.orElse(null); // вищезакоментовану конструкцію запропоновано замінити даним кодом
    }

    @Override
    @Transactional
    public void deleteNoteById(int id) {
        noteRepository.deleteById(id);
    }
}
