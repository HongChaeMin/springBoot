package org.zerock.club.service;

import org.zerock.club.dto.NoteDTO;
import org.zerock.club.entity.ClubMember;
import org.zerock.club.entity.Note;

import java.util.List;

public interface NoteService {

    Long register(NoteDTO noteDTO);

    NoteDTO get(Long id);

    void modify(NoteDTO noteDTO);

    void remove(Long id);

    List<NoteDTO> getAllWithWriter(String writerEmail);

    default Note dtoToEntity(NoteDTO noteDTO) {
        Note note = Note.builder()
                .id(noteDTO.getId())
                .title(noteDTO.getTitle())
                .content(noteDTO.getContent())
                .writer(ClubMember.builder().email(noteDTO.getWriterEmail()).build())
                .build();
        return note;
    }

    default NoteDTO entityToDto(Note note) {
        NoteDTO noteDTO = NoteDTO.builder()
                .id(note.getId())
                .title(note.getTitle())
                .content(note.getContent())
                .writerEmail(note.getWriter().getEmail())
                .regDate(note.getRegDate())
                .modDate(note.getModDate())
                .build();
        return noteDTO;
    }

}
