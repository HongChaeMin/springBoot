package org.zerock.mreview.service;

import org.zerock.mreview.dto.MovieDTO;
import org.zerock.mreview.dto.MovieImageDTO;
import org.zerock.mreview.dto.PageRequestDTO;
import org.zerock.mreview.dto.PageResultDTO;
import org.zerock.mreview.entity.Movie;
import org.zerock.mreview.entity.MovieImage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface MovieService {

    Long insertMovie(MovieDTO movieDTO);

    PageResultDTO<MovieDTO, Object[]> selectMovieList(PageRequestDTO requestDto);

    MovieDTO selectMovieOne(Long mno);

    default Map<String, Object> dtoToEntity(MovieDTO movieDTO) {
        Map<String, Object> entityMap = new HashMap<>();

        Movie movie = Movie.builder()
                .id(movieDTO.getMno())
                .title(movieDTO.getTitle())
                .build();

        entityMap.put("movie", movie);

        List<MovieImageDTO> imageDTOList = movieDTO.getImageDTOList();

        List<MovieImage> movieImageList = imageDTOList.stream().map(movieImageDTO -> {
            MovieImage movieImage = MovieImage.builder()
                    .path(movieImageDTO.getFolderPath())
                    .imgName(movieImageDTO.getImageURL())
                    .uuid(movieImageDTO.getUuid())
                    .movie(movie)
                    .build();
            return movieImage;
        }).collect(Collectors.toList());

        entityMap.put("imgList", movieImageList);

        return entityMap;
    }

    default MovieDTO entityToDto(Movie movie, List<MovieImage> movieImageList, Double avg, Long reviewCnt) {

        MovieDTO movieDTO = MovieDTO.builder()
                .mno(movie.getId())
                .title(movie.getTitle())
                .regDate(movie.getRegDate())
                .modDate(movie.getModDate())
                .build();

        List<MovieImageDTO> movieImageDTOList = movieImageList.stream().map(movieImage -> {
            return MovieImageDTO.builder()
                    .folderPath(movieImage.getPath())
                    .fileName(movieImage.getImgName())
                    .uuid(movieImage.getUuid())
                    .build();
        }).collect(Collectors.toList());

        movieDTO.setImageDTOList(movieImageDTOList);
        movieDTO.setAvg(avg);
        movieDTO.setReviewCnt(reviewCnt);

        return movieDTO;
    }

}
