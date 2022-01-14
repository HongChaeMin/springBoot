package org.zerock.mreview.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.zerock.mreview.dto.MovieDTO;
import org.zerock.mreview.dto.PageRequestDTO;
import org.zerock.mreview.dto.PageResultDTO;
import org.zerock.mreview.entity.Movie;
import org.zerock.mreview.entity.MovieImage;
import org.zerock.mreview.repository.MovieImageRepository;
import org.zerock.mreview.repository.MovieRepository;
import org.zerock.mreview.service.MovieService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final MovieImageRepository movieImageRepository;

    @Override
    public Long insertMovie(MovieDTO movieDTO) {

        Map<String, Object> entityMap = dtoToEntity(movieDTO);

        Movie movie = (Movie) entityMap.get("movie");
        List<MovieImage> movieImageList = (List<MovieImage>) entityMap.get("imgList");

        movieRepository.save(movie);
        for (MovieImage movieImage : movieImageList) {
            movieImageRepository.save(movieImage);
        }

        return movie.getId();
    }

    @Override
    public PageResultDTO<MovieDTO, Object[]> selectMovieList(PageRequestDTO requestDto) {

        Pageable pageable = requestDto.getPageable(Sort.by("id").descending());

        Page<Object[]> result = movieRepository.getListPage(pageable);

        Function<Object[], MovieDTO> fn = (arr ->
                entityToDto(
                        (Movie) arr[0],
                        (List<MovieImage>) (Arrays.asList((MovieImage) arr[1])),
                        (Double) arr[2],
                        (Long) arr[3])
                    );

        return new PageResultDTO<>(result, fn);
    }

    @Override
    public MovieDTO selectMovieOne(Long mno) {
        List<Object[]> result = movieRepository.getMovieWithAll(mno);

        // result 값 = 가공 필요
        // [Movie(movie_id=92, title=Movie...92), MovieImage(img_id=261, ...), 3.25, 4]
        // [Movie(movie_id=92, title=Movie...92), MovieImage(img_id=262, ...), 3.25, 4]
        // ...

        Movie movie = (Movie) result.get(0)[0];

        List<MovieImage> movieImageList = new ArrayList<>();

        result.forEach(arr -> {
            MovieImage movieImage = (MovieImage) arr[1];
            movieImageList.add(movieImage);
        });

        Double avg = (Double) result.get(0)[2];
        Long reviewCnt = (Long) result.get(0)[3];

        return entityToDto(movie, movieImageList, avg, reviewCnt);
    }

}
