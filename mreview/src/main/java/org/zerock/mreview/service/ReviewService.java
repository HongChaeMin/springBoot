package org.zerock.mreview.service;

import org.zerock.mreview.dto.ReviewDTO;
import org.zerock.mreview.entity.Member;
import org.zerock.mreview.entity.Movie;
import org.zerock.mreview.entity.Review;

import java.util.List;

public interface ReviewService {

    List<ReviewDTO> selectReviewListOfMovie(Long mno);

    Long insertReview(ReviewDTO reviewDTO);

    void updateReview(ReviewDTO reviewDTO);

    void deleteReview(Long rno);

    default Review dtoToEntity(ReviewDTO reviewDTO) {
        Review review = Review.builder()
                .id(reviewDTO.getReviewId())
                .movie(Movie.builder().id(reviewDTO.getMovieId()).build())
                .member(Member.builder().id(reviewDTO.getMemberId()).build())
                .grade(reviewDTO.getGrade())
                .text(reviewDTO.getText())
                .build();
        return review;
    }

    default ReviewDTO entityToDto(Review review) {
        ReviewDTO reviewDTO = ReviewDTO.builder()
                .reviewId(review.getId())
                .memberId(review.getMember().getId())
                .movieId(review.getMovie().getId())
                .nickname(review.getMember().getNickName())
                .email(review.getMember().getEmail())
                .grade(review.getGrade())
                .text(review.getText())
                .regDate(review.getRegDate())
                .modDate(review.getModDate())
                .build();
        return reviewDTO;
    }

}
