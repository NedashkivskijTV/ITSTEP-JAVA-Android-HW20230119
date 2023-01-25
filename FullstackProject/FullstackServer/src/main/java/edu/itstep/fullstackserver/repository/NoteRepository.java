package edu.itstep.fullstackserver.repository;

import edu.itstep.fullstackserver.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note, Integer> {
}
