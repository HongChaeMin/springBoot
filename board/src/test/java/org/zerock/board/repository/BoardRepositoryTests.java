package org.zerock.board.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Member;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class BoardRepositoryTests {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private MemberRepository memberRepository;

    public void insertBoard() {
        IntStream.rangeClosed(1, 100).forEach(i -> {
            Member member = Member.builder().email("user" + i + "@minHong.com").build();
            Board board = Board.builder()
                    .bno((long) i)
                    .title("Title... " + i)
                    .content("Content... " + i)
                    .writer(member)
                    .build();
            boardRepository.save(board);
        });
    }

    @Transactional // 해당 매서드를 하나의 트랜잭션으로 처리
    public void testRead1() {

        // 자동으로 left outer join 이 됨됨
       Optional<Board> result = boardRepository.findById(100L);

        Board board = result.get();

        System.out.println(board);
        System.out.println(board.getWriter());
    }

    public void testReadWithWriter() {
        // 지연 로딩으로 처리되었지만 쿼리를 보면 조인 처리가 되어있음
        Object result = boardRepository.getBoardWithWriter(100L);

        Object[] arr = (Object[]) result;

        System.out.println("=======================================");
        System.out.println(Arrays.toString(arr));
    }

    public void testGetBoardWithReply()  {
        List<Object[]> result = boardRepository.getBoardWithReply(100L);

        for (Object[] obj : result) {
            System.out.println(Arrays.toString(obj));
        }
    }

    public void testWithReplyCount() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());

        Page<Object[]> result = boardRepository.getBoardWithReplyCount(pageable);

        result.get().forEach(row -> {
            Object[] obj = row;

            System.out.println(Arrays.toString(obj));
        });
    }

    public void testSearchBoardRepository() {
        boardRepository.search1();
    }

    public void testSearchPage() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending().and(Sort.by("title").ascending()));

        Page<Object[]> result = boardRepository.searchPage("t", "1", pageable);

    }

}
