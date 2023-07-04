package org.judeukkim.springbootKDJ.service;

import lombok.RequiredArgsConstructor;
import org.judeukkim.springbootKDJ.dto.BoardDto;
import org.judeukkim.springbootKDJ.entity.Board;
import org.judeukkim.springbootKDJ.entity.Member;
import org.judeukkim.springbootKDJ.repository.BoardRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;


    public Page<BoardDto> boardPagingList(Pageable pageable) {
        //DB -> Entity
        Page<Board> boardEntityList = boardRepository.findAll(pageable);//1개의 DB 전체데이터 가져오기

        //Dto List
        Page<BoardDto> boardDtoList = boardEntityList.map(BoardDto::entityToDto);

        return boardDtoList;
    }

    @Transactional
    public void boardWrite(BoardDto boardDto) {
        Board boardEntity = Board.dtoToEntity(boardDto);
        boardRepository.save(boardEntity);
    }
    //조회수
    @Transactional
    public void hitCount(Long no) {
        boardRepository.hitCountUp(no);
    }

    //게시글 상세보기
    public BoardDto boardDetailView(Long no) {
        Optional<Board> optionalBoard=boardRepository.findById(no);
        if(optionalBoard.isPresent()){
            BoardDto boardDto = BoardDto.entityToDto(optionalBoard.get());
            return boardDto;
        }else{
            return null;
        }
    }

    public void boardUpdate(Long no, BoardDto boardDto) {
        Board boardEntity = Board.dtoToEntity(boardDto);
        boardRepository.save(boardEntity);
    }

    public int deleteOk(Long no) {
        Board boardEntity= boardRepository.findById(no).get();
        boardRepository.delete(boardEntity);
        if (boardRepository.findById(no)!=null){
            return 0;
        }
        return 1;
    }
}
