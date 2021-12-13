package org.zerock.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.board.entity.Board;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    // 한 개의 로우(Object) 내에 Object[]로 나옴
    @Query("select b, w from Board b left join b.writer w where b.bno =:bno") // 내부에 있는 엔티티를 이용할 때는 on 부분이 없음
    Object getBoardWithWriter(@Param("bno") Long bno);

    // Board는 Reply를 참조하고 있지 않음
    // 연관 관계가 없기 때문에 on을 사용함
    @Query("SELECT b, r FROM Board b LEFT JOIN Reply r ON r.board = b WHERE b.bno = :bno")
    List<Object[]> getBoardWithReply(@Param("bno") Long bno);

    @Query(value = "SELECT b, w, count(r)" +
            " FROM Board b" +
            " LEFT JOIN b.writer w" +
            " LEFT JOIN Reply r ON r.board = b" +
            " GROUP BY b",
            countQuery = "SELECT count(b) FROM Board b")
    Page<Object[]> getBoardWithReplyCount(Pageable pageable); // 목록 화면에 필요한 데이터

}
