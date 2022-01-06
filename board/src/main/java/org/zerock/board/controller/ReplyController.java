package org.zerock.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zerock.board.dto.ReplyDTO;
import org.zerock.board.service.ReplyService;

import java.util.List;

@RestController
@RequestMapping("/replies/")
@Log4j2
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService;

    @GetMapping(value = "/board/{bno}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReplyDTO>> selectReplyByBoard(@PathVariable("bno") Long bno) {
        log.info("bno ::::::::::::::::::::: " + bno);

        return new ResponseEntity<>(replyService.selectReplyList(bno), HttpStatus.OK);
    }

    @PostMapping(value = "/insert")
    public ResponseEntity<Long> insertReplyByBoard(@RequestBody ReplyDTO replyDTO) {
        log.info("replyDTO ::::::::::::::::::::: " + replyDTO);

        Long rno = replyService.insertReply(replyDTO);

        return new ResponseEntity<>(rno, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{rno}")
    public ResponseEntity<String> deleteReplyByBoard(@PathVariable("rno") Long rno) {
        log.info("rno :::::::::::::::::::::" + rno);

        replyService.deleteReply(rno);

        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @PutMapping(value = "/update")
    public ResponseEntity<String> updateReplyByBoard(@RequestBody ReplyDTO replyDTO) {
        log.info("replyDTO ::::::::::::::::::::: " + replyDTO);

        replyService.updateReply(replyDTO);

        return new ResponseEntity<>("success", HttpStatus.OK);
    }

}
