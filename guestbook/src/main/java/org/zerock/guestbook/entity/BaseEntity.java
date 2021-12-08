package org.zerock.guestbook.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(value = {AuditingEntityListener.class})
@Getter
public class BaseEntity {

    // @MappedSuperclass
    // 해당 어노테이션이 적용된 클래스는 테이블로 생성되지 않음
    // 실제 테이블은 이 클래스를 상속한 엔티티의 클래스로 생성됨

    // JPA에서 사용하는 엔티티 객체들은 영속 콘텍스트라는 곳에서 관리되는 객체
    // 이 객체들이 반영되면 결과적으로 데이터 베이스에 저장됨

    // @EntityListeners
    // JPA 내부에서 엔티티 객체가 생성/변경되는 것을 감지하는 역할
    // 이를 통해서 적절한 값이 들어감

    // @CreatedDate
    // JPA에서 엔티티의 생성 시간을 처리
    // @LastModifiedDate
    // 최종 수정을 자동으로 처리하는 용도로 사용

    @CreatedDate
    @Column(name = "regdate", updatable = false) // updateble=false : 객체를 데이터에 반영할 때 반영 X
    private LocalDateTime regDate;

    @LastModifiedDate
    @Column(name = "moddate")
    private LocalDateTime modDate;

}
