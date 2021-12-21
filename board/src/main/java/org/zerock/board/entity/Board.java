package org.zerock.board.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "writer") // @ToString 은 항상 exclude
// 연관 관계에서는 @ToString()을 주의
// 연관 관계가 있는 엔티티 클래스의 경우 @ToString()을 할 때는 습관적으로 exclude 속성을 사용하는 것이 좋음
public class Board extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;

    private String title;

    private String content;

    // 연관관계 지정
    @ManyToOne (fetch = FetchType.LAZY, cascade = CascadeType.ALL) // 명시적으로 Lazy 로딩 지정
    private Member writer;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
