package org.zerock.mreview.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.zerock.mreview.dto.ReviewDTO;
import org.zerock.mreview.entity.Movie;
import org.zerock.mreview.entity.Review;
import org.zerock.mreview.repository.ReviewRepository;
import org.zerock.mreview.service.ReviewService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    @Override
    public List<ReviewDTO> selectReviewListOfMovie(Long mno) {
        Movie movie = Movie.builder().id(mno).build();

        List<Review> reviewList = reviewRepository.findByMove(movie);

        return reviewList.stream().map(movieReview -> entityToDto(movieReview)).collect(Collectors.toList());
    }

    @Override
    public Long insertReview(ReviewDTO reviewDTO) {
        Review review = dtoToEntity(reviewDTO);

        reviewRepository.save(review);

        return review.getId();
    }

    @Override
    public void updateReview(ReviewDTO reviewDTO) {
        Optional<Review> result = reviewRepository.findById(reviewDTO.getMovieId());

        if (result.isPresent()) {
            Review review = result.get();
            review.changeGrade(reviewDTO.getGrade());
            review.changeText(reviewDTO.getText());

            reviewRepository.save(review);
        }

    }

    @Override
    public void deleteReview(Long rno) {
        reviewRepository.deleteById(rno);
    }
}
