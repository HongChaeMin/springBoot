package org.zerock.board.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.board.dto.BoardDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.dto.PageResultDTO;

@SpringBootTest
public class BoardServiceTests {

    @Autowired
    private BoardService boardService;

    public void testInsertBoard() {
        BoardDTO dto = BoardDTO.builder()
                .title("Test...Title")
                .content("Test...Content")
                .writerEmail("user99@minHong.com")
                .build();
        Long bno = boardService.insertBoard(dto);
    }

    public void testSelectBoardList() {
        PageRequestDTO pageRequestDTO = new PageRequestDTO();

        PageResultDTO<BoardDTO, Object[]> result = boardService.selectBoardList(pageRequestDTO);

        for (BoardDTO boardDTO : result.getDtoList()) {
            System.out.println(boardDTO);
        }
    }

    public void testSelectBoardOne() {
        Long bno = 100L;

        BoardDTO boardDTO = boardService.selectBoardOne(bno);

        System.out.println(boardDTO);
    }

    public void testDelete() {
        Long bno = 1L;
        boardService.deleteWithReplies(bno);
    }

    @Test
    public void testUpdate() {
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(99L)
                .title("update title...")
                .content("update content...")
                .build();

        boardService.updateBoard(boardDTO);
    }

}
