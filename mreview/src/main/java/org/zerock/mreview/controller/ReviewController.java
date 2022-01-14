package org.zerock.mreview.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zerock.mreview.dto.ReviewDTO;
import org.zerock.mreview.service.ReviewService;

import java.util.List;

@RestController
@Log4j2
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/{mno}/all")
    public ResponseEntity<List<ReviewDTO>> selectReviewList(@PathVariable("mno") Long mno) {
        log.info("------------------ list ------------------");
        log.info("Movie Id : " + mno);

        List<ReviewDTO> reviewDTOList = reviewService.selectReviewListOfMovie(mno);

        return new ResponseEntity<>(reviewDTOList, HttpStatus.OK);
    }

    @PostMapping("/{mno}")
    public ResponseEntity<Long> insertReview(@RequestBody ReviewDTO reviewDTO) {
        log.info("---------------insert reivew---------------");
        log.info("reviewDTO : " + reviewDTO);

        Long mno = reviewService.insertReview(reviewDTO);

        return new ResponseEntity<>(mno, HttpStatus.OK);
    }

    @PutMapping("/{mno}/{reviewnum}")
    public ResponseEntity<Long> updateReview(@PathVariable("reviewnum") Long reviewNum, @RequestBody ReviewDTO reviewDTO) {
        log.info("---------------update reivew---------------");
        log.info("reviewDTO : " + reviewDTO);

        reviewService.updateReview(reviewDTO);

        return new ResponseEntity<>(reviewNum, HttpStatus.OK);
    }

    @DeleteMapping("/{mno}/{reviewnum}")
    public ResponseEntity<Long> deleteReview(@PathVariable("reviewnum") Long reviewNum) {
        log.info("---------------update reivew---------------");
        log.info("reviewNum : " + reviewNum);

        reviewService.deleteReview(reviewNum);

        return new ResponseEntity<>(reviewNum, HttpStatus.OK);
    }

}
