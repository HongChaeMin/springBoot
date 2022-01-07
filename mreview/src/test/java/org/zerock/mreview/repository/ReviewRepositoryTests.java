package org.zerock.mreview.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.mreview.entity.Member;
import org.zerock.mreview.entity.Movie;
import org.zerock.mreview.entity.Review;

import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest
public class ReviewRepositoryTests {

    @Autowired
    private ReviewRepository reviewRepository;

    public void insertMovieReviews() {
        // 200개 리뷰 등록
        IntStream.rangeClosed(1, 200).forEach(i -> {
            // 영화 번호
            Long mno = (long) (Math.random() * 100) + 1;
            Movie movie = Movie.builder().id(mno).build();

            // 리뷰어 번호
            Long uno = ((long) (Math.random() * 100) + 1);
            Member member = Member.builder().id(uno).build();

            Review review = Review.builder()
                    .member(member)
                    .movie(movie)
                    .grade((int) (Math.random() * 5) + 1)
                    .text("이 영화에 대한 느낌적인 느낌 ... " + i)
                    .build();
            reviewRepository.save(review);
        });
    }

    @Test
    public void testFindByMovie() {
        Movie movie = Movie.builder().id(100L).build();

        List<Review> result = reviewRepository.findByMove(movie);

        result.forEach(movieReview -> {
            System.out.println(movieReview.getId() + "\t" + movieReview.getGrade() + "\t" +
                    movieReview.getText() + "\t" + movieReview.getMember().getEmail());
            System.out.println("-------------------------------------");
        });

    }

}
