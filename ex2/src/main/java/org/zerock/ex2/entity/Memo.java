package org.zerock.ex2.entity;

import lombok.*;

import javax.persistence.*;

@Entity // 해당 클래스가  엔티티를 위한 클래스이며, JPA로 관리되는 엔티티 객체라는 것을 의미
@Table(name = "tbl_memo") // 엔티티 클래스를 어떠한 테이블로 생성할 것인지에 대한 정보
@ToString
@Getter // getter 메소드 생성
@Builder // 객체를 생성할수있게 처리해줌
@AllArgsConstructor // builder와 함게 사용
@NoArgsConstructor // builder와 함게 사용
public class Memo {

    // 키 생성 전략 4가지
    // - AUTO(default) : JPA 구현체가 생성 방식을 결정
    // - IDENTITY : 사용하는 데이터베이스가 키 생성을 결정 (mysql, mariadb 에서는 auto increment 방식 사용)
    // - SEQUENCE : 데이터베이스의 sequence를 이용해서 키 생성 (@SequenceGenerator와 같이 사용)
    // - TABLE : 키 생성 전용 테이블을 행성해서 키 생성 (@TableGenerator와 함께 사용)

    @Id // PK에 해당하는 특정 필드
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment 라고 생각하면 됨
    private Long mno;

    @Column(length = 200, nullable = false)
    private String memoText;

}
