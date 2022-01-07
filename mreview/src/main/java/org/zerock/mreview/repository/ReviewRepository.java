package org.zerock.mreview.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.zerock.mreview.entity.Member;
import org.zerock.mreview.entity.Movie;
import org.zerock.mreview.entity.Review;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    // 책에서는 쿼리문이 없음 근데 에러나서 짬
    @Query("select r from Review r where r.movie = :movie")
    // 이거 안하고 메소드에 @Transactional 붙어도 됨 (쿼리 있을 때)
    @EntityGraph(attributePaths = {"member"}, type = EntityGraph.EntityGraphType.FETCH)
    List<Review> findByMove(Movie movie);

    // 그냥 지우면 리뷰 테이블을 다 지울 때 까지 지움 - 이게 더 효율적
    @Modifying
    @Query("delete from Review r where r.member = :member")
    void deleteByMember(Member member);

    // @EntityGraph

    // attributePaths : 로딩 설정을 변경하고 싶은 속성의 이름을 배열로 명시

    // type :
    //    FETCH - 명시한 속성은 EAGER로 처리하고, 나머지는 LAZY로 처리
    //    LOAD - 명시한 속성은 EAGER로 처리하고, 나머지는 엔티티 클래스에 명시되거나, 기본 방식으로 처리
}
