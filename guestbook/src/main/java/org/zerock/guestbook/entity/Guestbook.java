package org.zerock.guestbook.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Guestbook extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gno;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(length = 1500, nullable = false)
    private String content;

    @Column(length = 50, nullable = false)
    private String writer;

    // 엔티티 클래스는 가능하면 stter 관련 기능을 만들지 않는 것이 권장 사항임
    // 필요에 따라서는 수정 기능을 만들기도 함
    // (엔티티 클래스가 내부에서 변경되면 JPA를 관리하는 쪽이 복잡해질 우려가 있기 때문에
    // 가능하면 최소한의 수정이 가능하도록 하는것을 권장)
    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateContent(String content) {
        this.content = content;
    }

}
