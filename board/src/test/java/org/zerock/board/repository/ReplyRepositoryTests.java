package org.zerock.board.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Reply;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class ReplyRepositoryTests {

    @Autowired
    private ReplyRepository replyRepository;

    public void insertReply() {
        IntStream.rangeClosed(1, 300).forEach(i -> {
            // 게시판 번호 랜덤
            long bno = (long) (Math.random() * 100) + 1;

            Board board = Board.builder().bno(bno).build();

            Reply reply = Reply.builder()
                    .text("Reply... " + i)
                    .board(board)
                    .replyer("guest")
                    .build();
            replyRepository.save(reply);
        });
    }

    public void testRead1() {

        // Eager loading
        // 즉시 로딩 : 한 번에 연관관계가 있는 모든 엔티티를 가져옴

        // Lazy loading
        // 지연 로딩
        // 장점 : 조인을 하지 않기 때문에 단순하게 하나의 테이블을 이용하는 경우에는 빠른 속도의 처리가 가능
        // 단점 : 연관관계가 복잡한 경우에는 여러 번의 쿼리가 실행

        // !지연 로딩을 기본으로 사용하고, 상황에 맞게 필요한 방법을 찾는다

        // member entity 까지 모두 조인처리됨
        Optional<Reply> result = replyRepository.findById(100L);

        Reply reply = result.get();

        System.out.println(reply);
        System.out.println(reply.getBoard());
    }

    public void testListByBoard() {
        List<Reply> replyList = replyRepository.getRepliesByBoardOrderByBno(Board.builder().bno(3L).build());

        replyList.forEach(reply -> System.out.println(reply));
    }

}
