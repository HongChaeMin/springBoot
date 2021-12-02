package org.zerock.ex2.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.zerock.ex2.entity.Memo;
import org.zerock.ex2.memoRepository.MemoRepository;

import javax.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import java.beans.Transient;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class MemoRepositoryTests {

    @Autowired
    MemoRepository memoRepository;

    // CRUD
    // insert, update : save(entity)
    // - entity를 비교하고 없다면 insert, 존재한다면 update를 동작시킴
    // select : findById(key type), getOne(key type)
    // - getOne(getById)는 실제 객체가 필요하기전까지 SQL을 생성하지 않음
    // delete : deleteById(key type), delete(entity)

    // 스프링에서 처리되고, 의존성 주입에 문제가 없는지 테스트
    public void testClass() {
        System.out.println(memoRepository.getClass().getName());
    }

    // 100개의 새로운 Memo객체를 생성하고 MemoRepository를 통해 insert
    public void testInsertDummies() {
        IntStream.rangeClosed(1, 100).forEach(i -> {
            Memo memo = Memo.builder().memoText("Sample..." + i).build();
            memoRepository.save(memo);
        });
    }

    // select findById
    public void testSelectFindById() {
        Long mno = 100L;

        // java.util의 Optional로 반환됨
        Optional<Memo> result = memoRepository.findById(mno);

        System.out.println("===========================");

        // null인지 체크
        if (result.isPresent()) {
            Memo memo = result.get();

            System.out.println(memo);
        }
    }

    // select getOne
    public void testSelectGetOne() {
        Long mno = 100L;

        // 책에서 나온거
        // Memo memo = memoRepository.getOne(mno);

        // 요즘은 이거 쓰나봄
        Memo memo = memoRepository.getById(mno);

        System.out.println("===========================");

        System.out.println(memo);
    }

    public void testUpdate() {
        Memo memo = Memo.builder().mno(100L).memoText("Update Text...100").build();

        System.out.println(memoRepository.save(memo));
    }

    public void testDelete() {
        Long mno = 100L;

        memoRepository.deleteById(mno);
    }

    // 페이징 처리
    public void testPageDefault() {
        // 1page 10개
        Pageable pageable = PageRequest.of(0, 10);

        Page<Memo> result = memoRepository.findAll(pageable);

        System.out.println(result);

        System.out.println("------------------------------");

        // 총 페이지 갯수
        System.out.println("Total Pages : " + result.getTotalPages());

        // 전체 페이지 갯수
        System.out.println("Total Count : " + result.getTotalElements());

        // 현재 페이지 번호 (0부터 시작)
        System.out.println("Page Number : " + result.getNumber());

        // 페이지당 데이터 개수
        System.out.println("Page Size : " + result.getSize());

        // 다음 페이지 존재 여부
        System.out.println("has next page? : " + result.hasNext());

        // 시작 페이지(0) 여부
        System.out.println("first page? : " + result.isFirst());

        System.out.println("------------------------------");

        // .getContent() return type : List<Entity>
        for (Memo memo : result.getContent()) {
            System.out.println(memo);
        }
    }

    @Test
    public void testPageSort() {
        // mno기준으로 desc정렬
        Sort st1 = Sort.by("mno").descending();

        Pageable pageable = PageRequest.of(0, 10, st1);

        Page<Memo> result = memoRepository.findAll(pageable);

        result.get().forEach(memo -> {
            System.out.println(memo);
        });

        // 책에 여러개의 정렬 조건을 다르게 지정할 수 있다고 예제 나왔는데
        // 이제는 안되나봄 나중에 여러 개의 정렬 조건 찾아보기!
        // Sort sort1 = Sort.by("mno").descending();
        // Sort sort2 = Sort.by("memoText").ascending();
        // Sort sortAll = sort1.add(sort2);
    }

}
