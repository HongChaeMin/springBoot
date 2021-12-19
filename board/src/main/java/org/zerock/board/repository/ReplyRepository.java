package org.zerock.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.zerock.board.entity.Reply;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    // https://velog.io/@dnjscksdn98/JPA-Hibernate-Spring-Data-JPA-Modifying
    @Modifying
    @Query(value = "DELETE FROM Reply r WHERE r.board.bno =:bno")
    void deleteByBno(Long bno);

}
