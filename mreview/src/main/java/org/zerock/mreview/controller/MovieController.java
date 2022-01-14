package org.zerock.mreview.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.mreview.dto.MovieDTO;
import org.zerock.mreview.dto.PageRequestDTO;
import org.zerock.mreview.service.MovieService;

@Controller
@RequestMapping("/movie")
@Log4j2
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @GetMapping("/register")
    public void insertMovie() {
    }

    @PostMapping(value = "/register")
    public String insertMovie(MovieDTO movieDTO, RedirectAttributes redirectAttributes) {
        log.info("MovieDTO :::::::::::::::: " + movieDTO);
        log.info("insertMovie ::::::::::::: ");

        Long mno = movieService.insertMovie(movieDTO);

        redirectAttributes.addFlashAttribute("msg", mno);

        return "redirect:/movie/list";
    }

    @GetMapping("/list")
    public void selectMovieList(PageRequestDTO pageRequestDTO, Model model) {
        log.info("pageRequestDTO :::::::::: " + pageRequestDTO);

        model.addAttribute("result", movieService.selectMovieList(pageRequestDTO));
    }

    @GetMapping({"/read", "/modify"})
    public void selectMovieOne(long mno, @ModelAttribute("requestDTO") PageRequestDTO pageRequestDTO, Model model) {
        log.info("movie_id :::::::::::::::: " + mno);

        MovieDTO movieDTO = movieService.selectMovieOne(mno);

        model.addAttribute("dto", movieDTO);
    }


}
