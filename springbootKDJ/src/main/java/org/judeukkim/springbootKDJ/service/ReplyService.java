package org.judeukkim.springbootKDJ.service;

import lombok.RequiredArgsConstructor;
import org.judeukkim.springbootKDJ.dto.ReplyDto;
import org.judeukkim.springbootKDJ.entity.Board;
import org.judeukkim.springbootKDJ.entity.Reply;
import org.judeukkim.springbootKDJ.repository.BoardRepository;
import org.judeukkim.springbootKDJ.repository.ReplyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReplyService {

    private final BoardRepository boardRepository;
    private final ReplyRepository replyRepository;


    @Transactional
    public Long insertReplyDo(ReplyDto replyDto) {

        Optional<Board> boardEntitys = boardRepository.findById(replyDto.getBoardId());
        if (boardEntitys.isPresent()) {
            Board boardEntity = boardEntitys.get();

            Reply replyEntity = Reply.toReplyEntity(replyDto, boardEntity);

            return replyRepository.save(replyEntity).getId();
        } else {
            return null;
        }
    }

    public List<ReplyDto> replyDtoListDo(Long boardId) {
        Optional<Board> boardOptional = boardRepository.findById(boardId);

        if (boardOptional.isPresent()) {
            Board boardEntity = boardOptional.get();
            List<Reply> boardReplyEntities = replyRepository.findAllByBoardIdOrderByIdDesc(boardEntity);
            List<ReplyDto> list = new ArrayList<>();

            for (Reply replyEntity : boardReplyEntities) {
                ReplyDto replyDto = ReplyDto.toReplyDto(replyEntity, boardId);
                list.add(replyDto);
            }
            return list;
        } else {
            // 해당 boardId에 해당하는 게시글이 없는 경우 빈 리스트 반환 또는 예외 처리
            return Collections.emptyList(); // 빈 리스트 반환
            // 또는 예외를 던져서 상위로 처리하도록 구현 가능
            // throw new IllegalArgumentException("해당 boardId에 해당하는 게시글이 없습니다.");
        }
    }

    @Transactional
    public Long updateReply(ReplyDto replyDto) {

        Optional<Board> boardEntitys=boardRepository.findById(replyDto.getBoardId());

        if(boardEntitys.isPresent()){
            Board boardEntity=boardEntitys.get();

            Reply replyEntity = Reply.toReplyUpdateEntity(replyDto,boardEntity);

            return replyRepository.save(replyEntity).getId();
        }else{
            return null;
        }
    }

    @Transactional
    public void replyDelete(Long replyId,Long boardId) {

        Optional<Board> boardEntity=boardRepository.findById(boardId);

        if(boardEntity.isPresent()){
            Optional<Reply> replyEntity=replyRepository.findById(replyId);
            if(replyEntity.isPresent()){
                replyRepository.deleteById(replyEntity.get().id);
            }
        }
    }
}
