package org.zerock.guestbook.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.zerock.guestbook.entity.Guestbook;
import org.zerock.guestbook.entity.QGuestbook;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class GuestbookRepositoryTests {

    @Autowired
    private GuestbookRepository guestbookRepository;

    // 등록 테스트
    public void insertDummies() {
        IntStream.rangeClosed(1, 300).forEach(i -> {
            Guestbook guestbook = Guestbook.builder()
                    .title("Title... " + i)
                    .content("Content... " + i)
                    .writer("user" + i)
                    .build();
            System.out.println(guestbookRepository.save(guestbook));
        });
    }

    // update test
    // title, content, moddate가 바뀌었는지 확인
    public void updateTest() {
        Optional<Guestbook> result = guestbookRepository.findById(300L);

        if (result.isPresent()) {
            Guestbook guestbook = result.get();

            guestbook.updateTitle("Change Title...");
            guestbook.updateContent("Change Content...");

            guestbookRepository.save(guestbook);
        }
    }

    // Querydsl 사용법
    // - BooleanBuilder 생성
    // - 조건에 맞는 구문은 Querydsl에서 사용하는 Predicate 타입의 함수를 생성
    // - BooleanBuilder에 작성된 Perdicate를 추가하고 실행
    public void testQuery1() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("gno").descending());

        String keyword = "1";

        // 1
        // Q도메인 클래스를 얻어옴 (엔티티 클래스에 선언된 필드들을 변수로 활용할 수 있음)
        QGuestbook qGuestbook = QGuestbook.guestbook;

        // 2
        // BooleanBuilder는 where문에 들어가는 조건들을 넣어주는 컨테이너라고 간주
        BooleanBuilder builder = new BooleanBuilder();

        // 3
        // 원하는 조건은 필드 값과 같이 결합해서 생성함
        // com.querydsl.core.types.Predicate 타입임 (! java에 있는 타입 아님)
        BooleanExpression expression = qGuestbook.title.contains(keyword);

        // 4
        // 만들어진 조건은 where문에 and나 or같은 키워드와 결합 시켜줘야됨
        builder.and(expression);

        // 5
        // GuestbookRepository에 추가된 QuerydslPredicateExcutor 인터페이스의 findAll를 사용할 수 있음
        Page<Guestbook> result = guestbookRepository.findAll(builder, pageable);

        result.stream().forEach(guestbook -> {
            System.out.println(guestbook);
        });
    }

    // 다중 항목 검색 테스트
    @Test
    public void testQuery2() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("gno").descending());

        QGuestbook qGuestbook = QGuestbook.guestbook;

        String keyword = "1";

        BooleanBuilder builder = new BooleanBuilder();

        BooleanExpression exTitle = qGuestbook.title.contains(keyword);

        BooleanExpression exContent = qGuestbook.content.contains(keyword);

        BooleanExpression exAll = exTitle.or(exContent);

        builder.and(exAll);

        builder.and(qGuestbook.gno.gt(0L));

        Page<Guestbook> result = guestbookRepository.findAll(builder, pageable);

        result.stream().forEach(guestbook -> {
            System.out.println(guestbook);
        });

        // 얘는 설명보다 쿼리를 직접 보는게 나을것같아서 쿼리 적어놓음
        /*
        select
            guestbook0_.gno as gno1_0_,
            guestbook0_.moddate as moddate2_0_,
            guestbook0_.regdate as regdate3_0_,
            guestbook0_.content as content4_0_,
            guestbook0_.title as title5_0_,
            guestbook0_.writer as writer6_0_
        from
            guestbook guestbook0_
        where
            (
                guestbook0_.title like ? escape '!'
                or guestbook0_.content like ? escape '!'
            )
            and guestbook0_.gno>?
        order by
            guestbook0_.gno desc limit ?
        Hibernate:
        select
            count(guestbook0_.gno) as col_0_0_
        from
            guestbook guestbook0_
        where
            (
                guestbook0_.title like ? escape '!'
                or guestbook0_.content like ? escape '!'
            )
            and guestbook0_.gno>?
        */
    }

}
