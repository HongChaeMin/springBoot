package org.zerock.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.zerock.board.dto.ReplyDTO;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Reply;
import org.zerock.board.repository.ReplyRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService {

    private final ReplyRepository replyRepository;

    @Override
    public Long insertReply(ReplyDTO replyDTO) {
        Reply reply = dtoToEntity(replyDTO);

        replyRepository.save(reply);

        return reply.getRno();
    }

    @Override
    public List<ReplyDTO> selectReplyList(Long bno) {
        List<Reply> replyDTOS = replyRepository.getRepliesByBoardOrderByBno(Board.builder().bno(bno).build());

        return replyDTOS.stream().map(reply -> entityToDto(reply)).collect(Collectors.toList());
    }

    @Override
    public void updateReply(ReplyDTO replyDTO) {
        Reply reply = dtoToEntity(replyDTO);
        replyRepository.save(reply);
    }

    @Override
    public void deleteReply(Long rno) {
        replyRepository.deleteById(rno);
    }
}
