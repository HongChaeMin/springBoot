package org.zerock.board.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "board")
public class Reply extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno;

    private String text;

    private String replyer;

    // https://jamong-icetea.tistory.com/236
    // https://velog.io/@max9106/JPA%EC%97%94%ED%8B%B0%ED%8B%B0-%EC%83%81%ED%83%9C-Cascade
    // https://postitforhooney.tistory.com/entry/JavaJPAHibernate-CascadeType%EB%9E%80-%EA%B7%B8%EB%A6%AC%EA%B3%A0-%EC%A2%85%EB%A5%98
    // 여기 더 알아보기 , cascade = CascadeType.ALL 설정했을 때 업데이트는 되는데 인설트가 안됨
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private Board board;

    // Cascade 종류

    // 보통은 ALL을 사용함

    // CascadeType.ALL : 상위 엔티티에서 하위 엔티티로 모든 작업을 전파
    // CascadeType.PERSIST : 하위 엔티티까지 영속성 전달 (상위 엔티티를 저장하면 하위 엔티티까지 저장)
    // CascadeType.MERGE : 하위 엔티티까지 병합 작업을 지속 (상위 엔티티와 하위 엔티티를 조회한 후 업데이트)
    // CascadeType.REMOVE : 하위 엔티티까지 제거 작업을 지속 (연결된 하위 엔티티까지 엔티티 제거)
    // CascadeType.REFRESH : DB로부터 인스턴스 값 다시 읽어오기 (연결된 하위 엔티티까지 인스턴스 값 새로고침)
    // CascadeType.DETACH : 영속성 컨텍스트에서 엔티티 제거 (연결된 하위 엔티티까지 영속성 제거)

    // 문제점
    // 댓글 수정을 하면 새로운 보드가 등록 -> 업데이트가 안됨

    // 해결 방법
    // bno을 넘겨서 merge로 설정을 해 조회 후 업데이트를 시킴

}
