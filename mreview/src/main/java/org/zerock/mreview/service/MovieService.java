package org.zerock.mreview.service;

import org.zerock.mreview.dto.MovieDTO;
import org.zerock.mreview.dto.MovieImageDTO;
import org.zerock.mreview.entity.Movie;
import org.zerock.mreview.entity.MovieImage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface MovieService {

    Long insertMovie(MovieDTO movieDTO);

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

}
