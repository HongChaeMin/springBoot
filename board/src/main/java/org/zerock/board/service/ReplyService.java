package org.zerock.board.service;

import org.zerock.board.dto.ReplyDTO;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Reply;

import java.util.List;

public interface ReplyService {

    // 댓글의 등록
    Long insertReply(ReplyDTO replyDTO);

    // 특정 게시물의 댓글 목록
    List<ReplyDTO> selectReplyList(Long bno);

    // 댓글 수정
    void updateReply(ReplyDTO replyDTO);

    // 댓글 삭제
    void deleteReply(Long rno);

    // dto to entity (board 객체 처리 함께)
    default Reply dtoToEntity(ReplyDTO replyDTO) {

        Board board = Board.builder().bno(replyDTO.getBno()).build();

        Reply reply = Reply.builder()
                .rno(replyDTO.getRno())
                .text(replyDTO.getText())
                .replyer(replyDTO.getReplyer())
                .board(board)
                .build();

        return reply;
    }

    // entity to dto (board 객체 처리 X)
    default ReplyDTO entityToDto(Reply reply) {
        ReplyDTO replyDTO = ReplyDTO.builder()
                .rno(reply.getRno())
                .text(reply.getText())
                .replyer(reply.getReplyer())
                .bno(reply.getBoard().getBno())
                .regDate(reply.getRegDate())
                .modDate(reply.getModDate())
                .build();
        return replyDTO;
    }

}
