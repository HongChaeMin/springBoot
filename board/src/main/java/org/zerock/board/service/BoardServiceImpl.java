package org.zerock.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.zerock.board.dto.BoardDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.dto.PageResultDTO;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Member;
import org.zerock.board.repository.BoardRepository;
import org.zerock.board.repository.MemberRepository;
import org.zerock.board.repository.ReplyRepository;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Log4j2
public class BoardServiceImpl implements BoardService {

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository; // μλ μ£Όμ final
    private final ReplyRepository replyRepository;

    @Override
    public Long insertBoard(BoardDTO boardDTO){
        log.info(boardDTO);

        Member member = memberRepository.getById(boardDTO.getWriterEmail());

        System.out.println("member :::::::::::: " + member);

        Board board = Board.builder()
                .title(boardDTO.getTitle())
                .content(boardDTO.getContent())
                .writer(member)
                .build();

        boardRepository.save(board);

        System.out.println("result ::::::::::::::: " + board.getBno());

        return board.getBno();
    }

    @Override
    public PageResultDTO<BoardDTO, Object[]> selectBoardList(PageRequestDTO pageRequestDTO) {
        log.info(pageRequestDTO);

        Function<Object[], BoardDTO> fn = (en -> entityToDto((Board) en[0], (Member) en[1], (Long) en[2]));

        Page<Object[]> result = boardRepository.getBoardWithReplyCount(pageRequestDTO.getPageable(Sort.by("bno").descending()));

        /*Page<Object[]> result = boardRepository.searchPage(
                pageRequestDTO.getType(),
                pageRequestDTO.getKeyword(),
                pageRequestDTO.getPageable(Sort.by("bno").descending())
        );*/

        return new PageResultDTO<>(result, fn);
    }

    @Override
    public BoardDTO selectBoardOne(Long bno) {
        Object result = boardRepository.getBoardByBno(bno);

        Object[] arr = (Object[]) result;

        return entityToDto((Board) arr[0], (Member) arr[1], (Long) arr[2]);
    }

    @Transactional
    @Override
    public void deleteWithReplies(Long bno) {
        // λΆλͺ νμ€νΈ μ½λμμ μ λμμλ...!!!! μ κ·Έλ
        replyRepository.deleteByBno(bno);
        boardRepository.deleteById(bno);
    }

    @Override
    public void updateBoard(BoardDTO boardDTO) {
        Optional<Board> board = boardRepository.findById(boardDTO.getBno());

        if (board.isPresent()) {
            Board result = board.get();

            result.setTitle(boardDTO.getTitle());
            result.setContent(boardDTO.getContent());

            boardRepository.save(result);
        }
    }

}
