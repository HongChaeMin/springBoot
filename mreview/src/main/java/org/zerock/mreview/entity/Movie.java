package org.zerock.mreview.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Movie extends BaseEntity{

    @Id @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "movie_id")
    private Long id;

    private String title;

}
