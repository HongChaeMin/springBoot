package org.zerock.club.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zerock.club.dto.NoteDTO;
import org.zerock.club.entity.Note;
import org.zerock.club.repository.ClubMemberRepository;
import org.zerock.club.repository.NoteRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;

    private final ClubMemberRepository clubMemberRepository;

    @Override
    public Long register(NoteDTO noteDTO) {
        Note note = dtoToEntity(noteDTO);
        note.setClubMember(note.getWriter().getEmail(), clubMemberRepository);
        noteRepository.save(note);
        return note.getId();
    }

    @Override
    public NoteDTO get(Long id) {
        Optional<Note> result = noteRepository.getWithWriter(id);
        return result.isPresent() ? entityToDto(result.get()) : null;
    }

    @Override
    public void modify(NoteDTO noteDTO) {
        Optional<Note> result = noteRepository.findById(noteDTO.getId());
        if(result.isPresent()) {
            Note note = result.get();
            note.changeTitle(noteDTO.getTitle());
            note.changeContent(noteDTO.getContent());
            noteRepository.save(note);
        }
    }

    @Override
    public void remove(Long id) {
        noteRepository.deleteById(id);
    }

    @Override
    public List<NoteDTO> getAllWithWriter(String writerEmail) {
        List<Note> noteList = noteRepository.getList(writerEmail);
        return noteList.stream().map(note -> entityToDto(note)).collect(Collectors.toList());
    }

}
