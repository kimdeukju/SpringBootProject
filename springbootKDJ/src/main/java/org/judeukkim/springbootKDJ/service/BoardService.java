package org.judeukkim.springbootKDJ.service;

import lombok.RequiredArgsConstructor;
import org.judeukkim.springbootKDJ.dto.BoardDto;
import org.judeukkim.springbootKDJ.entity.Board;
import org.judeukkim.springbootKDJ.entity.FileEntity;
import org.judeukkim.springbootKDJ.entity.Member;
import org.judeukkim.springbootKDJ.repository.BoardRepository;
import org.judeukkim.springbootKDJ.repository.FileRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final FileRepository fileRepository;


    public Page<BoardDto> boardPagingList(Pageable pageable) {
        //DB -> Entity
        Page<Board> boardEntityList = boardRepository.findAll(pageable);//1개의 DB 전체데이터 가져오기

        //Dto List
        Page<BoardDto> boardDtoList = boardEntityList.map(BoardDto::entityToDto);

        return boardDtoList;
    }

//    @Transactional
//    public void boardWrite(BoardDto boardDto) {
//        Board boardEntity = Board.dtoToEntity(boardDto);
//        boardRepository.save(boardEntity);
//    }
    @Transactional
    public void boardWrite(BoardDto boardDto) throws IOException {
            //파일이 없으면
        if (boardDto.getBoardFile().isEmpty()){
            Board boardEntity = Board.dtoToEntity(boardDto);
            boardRepository.save(boardEntity);
        }else {
            //파일이 있을 경우 -> 파일저장 별도의 테이블 생성 1:N
            // 원본파일,이름 -> 서버 저장이름
            // 저장경로(실제경로) -> 파일 저장
            // 테이블 저장(게시글, 파일)
            //////////////////////////////파일이 실제 경로에 저장///////////////////////////////////////
            MultipartFile boardFile = boardDto.getBoardFile();// 1. 파일 을 가져온다.  Dto
            String originalFilename = boardFile.getOriginalFilename();//원본파일이름
            UUID uuid = UUID.randomUUID(); //random id ->랜덤한 값을 추출하는 클래스
            // "gafdsagfasd_shop0.jpg"
            // "gafdsagfasd"+"_"+"shop0.jpg"
            String newFileName = uuid + "_" + originalFilename; // 저장파일이름 (보안)
            //String filePath="D:\\saveFiles\\";
            //String filePath="D:/saveFiles/"+newFileName; //실제 파일 저장경로+이름
            String filePath="C:/saveFiles/"+newFileName; //실제 파일 저장경로+이름
            // 실제 파일 저장 실행
            boardFile.transferTo(new File(filePath));// 저장  ,예외처리 -> 경로에 파일 저장
            //////////////////////////////파일이 실제 경로에 저장///////////////////////////////////////


            //////////////////////////////파일을 DB 테이블에 저장///////////////////////////////////////
            // 게시글에 저장
            //Dto-> Entity변경

            //1. 게시글 저장 -> 게시글 아이디 get
            //2. -> 게시글 아이디에 해당하는 게시글에 파일을 저장

            //1.게시글 저장
            Board boardEntity=Board.dtoToEntityFile(boardDto);  //게시글에 저장 한후 파일저장
            Long boardId= boardRepository.save(boardEntity).getId();//저장 실행-> 게시글의 아이디를 가져온다.

            //2.저정 한후 ID에 해당하는 게시글 조회
            Optional<Board> boardEntity2=boardRepository.findById(boardId);// id에 해당하는 게시글
            Board boardEntity3=boardEntity2.get();// id에 해당하는 게시글

            // 3. 게시글 ID에 해당하는 파일테이블에 저장     //게시글 id, 원래 파일이름, 새파일이름
            //Dto-Entity                                 // boardId
            FileEntity fileEntity=FileEntity.toFileEntity(boardEntity3,originalFilename,newFileName);
            fileRepository.save(fileEntity);// 파일 저장

            //////////////////////////////파일을 DB 테이블에 저장///////////////////////////////////////

        }

    }

    @Transactional
    public void boardUpdate(BoardDto boardDto) throws IOException {

        if (boardDto.getBoardFile().isEmpty()){
            Board boardEntity = Board.dtoToEntity(boardDto);
            boardRepository.save(boardEntity);
        }else {
            MultipartFile boardFile = boardDto.getBoardFile();// 1. 파일 을 가져온다.  Dto
            String originalFilename = boardFile.getOriginalFilename();//원본파일이름
            UUID uuid = UUID.randomUUID(); //random id ->랜덤한 값을 추출하는 클래스

            String newFileName = uuid + "_" + originalFilename; // 저장파일이름 (보안)

            String filePath="C:/saveFiles/"+newFileName; //실제 파일 저장경로+이름

            boardFile.transferTo(new File(filePath));// 저장  ,예외처리 -> 경로에 파일 저장

            Board boardEntity=Board.dtoToEntityFile(boardDto);  //게시글에 저정 한후 파일저정
            Long boardId= boardRepository.save(boardEntity).getId();//저장 실행-> 게시글의 아이디를 가져온다.

            Optional<Board> boardEntity2=boardRepository.findById(boardId);// id에 해당하는 게시글
            Board boardEntity3=boardEntity2.get();// id에 해당하는 게시글

            FileEntity fileEntity=FileEntity.toFileEntity(boardEntity3,originalFilename,newFileName);
            fileRepository.save(fileEntity);// 파일 저장

         }

    }


    //조회수
    @Transactional
    public void hitCount(Long id) {
        boardRepository.hitCountUp(id);
    }

    //게시글 상세보기
    public BoardDto boardDetailView(Long id) {
        Optional<Board> optionalBoard=boardRepository.findById(id);
        if(optionalBoard.isPresent()){
            BoardDto boardDto = BoardDto.entityToDto(optionalBoard.get());
            return boardDto;
        }else{
            return null;
        }
    }


    public int deleteOk(Long id) {
        Board boardEntity= boardRepository.findById(id).get();
        boardRepository.delete(boardEntity);
        if (boardRepository.findById(id)!=null){
            return 0;
        }
        return 1;
    }
}

