package org.zerock.mreview.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {

    private Long reviewId;

    private Long movieId;

    private Long memberId;

    private String nickname;

    private String email;

    private int grade;

    private String text;

    private LocalDateTime regDate, modDate;

}
