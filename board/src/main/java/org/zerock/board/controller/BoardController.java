package org.zerock.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.board.dto.BoardDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.service.BoardService;

@Controller
@RequestMapping("/board")
@Log4j2
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model) {
        log.info("list... " + pageRequestDTO);

        model.addAttribute("result", boardService.selectBoardList(pageRequestDTO));
    }

    @GetMapping("/register")
    public void register() {
        log.info("register...");
    }

    @PostMapping("/register")
    public String registerPost(BoardDTO boardDTO, RedirectAttributes redirectAttributes) {
        log.info("register DTO... " + boardDTO);

        // 새로 추가된 엔티티 번호
        Long bno = boardService.insertBoard(boardDTO);

        log.info("BoardNum :::::::: " + bno);

        redirectAttributes.addAttribute("msg", bno);

        return "redirect:/board/list";
    }

}
