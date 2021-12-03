package org.zerock.ex2.memoRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.ex2.entity.Memo;

import javax.transaction.Transactional;
import java.util.List;

// JpaRepository 선언만으로 자동으로 빈 등록됨
public interface MemoRepository  extends JpaRepository<Memo, Long> {

    // 쿼리 메서드
    // : 메서드의 이름 자체가 질의(query)문 : 메서드의 이름 자체로 질의 조건을 만들어냄
    // : select를 하는 작업이라면 List 타입이나 배열을 이용할 수 있음
    // : 파라미터에 Pageable 타입을 넣는 경우에는 무조건 Page<E> 타입

    // Spring Data JPA Reference를 이용해서 더 찾아보기 (구글링하거나)
    // https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.query-methods

    // 기본 예제
    List<Memo> findByMnoBetweenOrderByMnoDesc(Long from, Long to);

    // 쿼리 메서드와 Pageable의 결합
    Page<Memo> findByMnoBetween(Long from, Long to, Pageable pageable);

    // deleteBy로 시작하는 삭제 처리
    // 레퍼런스 보니까 없어진 것 같음 @Query 사용하기
    // void deleteMemoByMnoLessThan(Long num);

    // @Query 어노테이션
    // - 필요한 데이터만 선별적으로 추출하는 기능이 가능
    // - 데이터베이스에 맞는 순수한 SQL을 사용하는 기능
    // - CRUD가 아닌 DML 등을 처리하는 기능 (@Modifying과 함께 사용)

    // Memo를 mno의 역순으로 정렬
    @Query("select m from Memo m order by m.mno desc")
    List<Memo> getListDesc();

    // @Query의 파라미터 바인딩
    // 1. '?1,?2'와 1부터 시작하는 파라미터의 순서를 이용하는 방식
    // 2. '.xxx'와 같이 ':파라미터 이름'을 활용하는 방식
    // 3. ':#{ }'와 같이 자바 빈 스타일을 이용하는 방식

    // ex 2
    @Transactional
    @Modifying
    @Query("update Memo m set m.memoText = :memoText where m.mno = :mno")
    int updateMemoText2(@Param("mno") Long mno, @Param("memoText") String memoText);

    // ex 3
    @Transactional
    @Modifying
    @Query("update Memo m set m.memoText = :#{#param.memoText} where m.mno = :#{#param.mno}")
    int updateMemoText3(@Param("mno") Long mno, @Param("memoText") String memoText);

    // @Query와 페이징 처리
    @Query(value = "select m from Memo m where m.mno > :mno",
        countName = "select count(m) from Memo m where m.mno > :mno") // Pageable 타입의 파라미터를 전달해줌
    Page<Memo> getListWithQuery(Long mno, Pageable pageable);

    // Object[] 리턴
    // @Query의 가장 큰 장점 : 현재 필요한 데이터만을 Object[] 의 형태로 선별적으로 추출 가능 (join이나 group by를 이용하는 경우)
    @Query(value = "select m.mno, m.memoText, CURRENT_DATE from Memo m where m.mno > :mno",
        countName = "select count(m) from Memo m where m.mno > :mno")
    Page<Object[]> getListWithQueryObject(Long mno, Pageable pageable);

    // Native SQL 처리
    // @Query의 강력한 기능 : DB 고유의 SQL 구문을 그대로 활용 -> JPA의 장점을 잃어버리긴함
    @Query(value = "select * from memo where mno > 0",
        nativeQuery = true)
    List<Object[]> getNativeResult(); // 확실히 mes같은 프로젝트에는 jpa가 안맞는 것 같네욥?

}
