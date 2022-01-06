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
    // org.hibernate.TransientPropertyValueException
    // https://postitforhooney.tistory.com/entry/JavaJPAHibernate-CascadeType%EB%9E%80-%EA%B7%B8%EB%A6%AC%EA%B3%A0-%EC%A2%85%EB%A5%98
    // 여기 더 알아보기 , cascade = CascadeType.ALL 설정했을 때 업데이트는 되는데 인설트가 안됨
    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

}
