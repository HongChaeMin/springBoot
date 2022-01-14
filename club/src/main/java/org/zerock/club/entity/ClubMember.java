package org.zerock.club.entity;

import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class ClubMember {

    @Id @GeneratedValue
    @Column(name = "club_member_id")
    private Long id;

    private String email;

    private String name;

    private String password;

    private boolean fromSocial;

    // @ElementCollection
    // 연관된 부모 Entity 하나에만 연관되어 관리된다. (부모 Entity와 독립적으로 사용 X)
    // 항상 부모와 함께 저장되고 삭제되므로 cascade 옵션은 제공하지 않는다. (cascade = ALL 인 셈)
    // 부모 Entity Id와 추가 컬럼(basic or embedded 타입)으로 구성된다.
    // 기본적으로 식별자 개념이 없으므로 컬렉션 값 변경 시, 전체 삭제 후 새로 추가한다

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default // 초기화
    private Set<ClubMemberRole> roleSet = new HashSet<>();

    public void addMemberRole(ClubMemberRole clubMemberRole) {
        roleSet.add(clubMemberRole);
    }

}
