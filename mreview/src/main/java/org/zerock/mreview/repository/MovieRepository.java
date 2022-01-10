package org.zerock.mreview.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.mreview.entity.Movie;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long>{

    @Query("select m, mi, avg(coalesce(r.grade, 0)), count(distinct r) from Movie m"
            + " left join MovieImage mi on mi.movie = m"
            + " left join Review r on r.movie = m group by r.movie")
    Page<Object[]> getListPage(Pageable pageable);

    @Query("select m, mi, avg(coalesce(r.grade, 0)), count(r) from Movie m" +
            " left join MovieImage mi on mi.movie = m" +
            " left join Review r on r.movie = m" +
            " where m.id = :id group by mi")
    List<Object[]> getMovieWithAll(Long id);

}