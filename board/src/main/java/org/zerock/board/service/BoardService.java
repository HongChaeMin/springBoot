package org.zerock.board.service;

import org.zerock.board.dto.BoardDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.dto.PageResultDTO;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Member;

public interface BoardService {

    Long insertBoard(BoardDTO boardDTO);

    PageResultDTO<BoardDTO, Object[]> selectBoardList(PageRequestDTO pageRequestDTO);

    BoardDTO selectBoardOne(Long bno);

    void deleteWithReplies(Long bno);

    void updateBoard(BoardDTO boardDTO);

    default Board dtoToEntity(BoardDTO boardDTO) {
        Member member = Member.builder().email(boardDTO.getWriterEmail()).build();

        Board board = Board.builder()
                .bno(boardDTO.getBno())
                .title(boardDTO.getTitle())
                .content(boardDTO.getContent())
                .writer(member)
                .build();
        return board;
    }

    default BoardDTO entityToDto(Board board, Member member, Long replyCount) {
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .writerEmail(member.getEmail())
                .writerName(member.getName())
                .replyCount(replyCount.intValue())
                .build();

        return boardDTO;
    }
}
