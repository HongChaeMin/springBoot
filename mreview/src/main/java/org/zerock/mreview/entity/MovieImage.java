package org.zerock.mreview.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString (exclude = "movie_id")
public class MovieImage extends BaseEntity{

    @Id @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "movie_image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    private Movie movie;

    private String uuid;

    private String imgName;

    private String path;
}
