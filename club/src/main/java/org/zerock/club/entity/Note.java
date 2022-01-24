package org.zerock.club.entity;

import lombok.*;
import org.zerock.club.repository.ClubMemberRepository;
import org.zerock.club.security.service.ClubUserDetailsService;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Note extends BaseEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private ClubMember writer;

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeContent(String content) {
        this.content = content;
    }

    // 매우 매우 매우 마음에 안들지만 안되서 추가함
    public void setClubMember(String writerEmail, ClubMemberRepository clubMemberRepository) {
        this.writer = clubMemberRepository.findByEmail(writerEmail);
    }

}
