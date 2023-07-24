package org.judeukkim.springbootKDJ.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.judeukkim.springbootKDJ.entity.Board;
import org.judeukkim.springbootKDJ.entity.Member;
import org.judeukkim.springbootKDJ.entity.Reply;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BoardDto {

    public Long id;
    @NotBlank(message = "제목을 입력 해주세요")
    private String title;   //제목
    @NotBlank(message = "내용을 입력 해주세요")
    private String content;  //내용
    private String writer;  //작성자
    private int hit; //조회수
    private LocalDateTime createTime; //작성시간
    private Member boardJoinMember;  //Member 테이블과 연결, 작성한 사용자 불러오기

    // 파일 업로드 파일을 저장 할수 있는 객체
    //MultipartFile은 Spring Framework에서 파일 업로드를 처리하기 위해 제공되는 인터페이스입니다. 이를 사용하면 웹 애플리케이션에서 클라이언트가 업로드한 파일을 처리할 수 있습니다.
    private MultipartFile boardFile;

    // 파일이 있을 경우 처리
    private String oldFileName;// 원래이름

    private String newFileName;// DB 저장이름

    private int attachFile;// 파일 유무(1,0)

    public static BoardDto entityToDto(Board board) {
        BoardDto boardDto= new BoardDto();

        boardDto.setId(board.getId());
        boardDto.setTitle(board.getTitle());
        boardDto.setContent(board.getContent());
        boardDto.setWriter(board.getWriter());
        boardDto.setHit(board.getHit());
        boardDto.setCreateTime(board.getCreateTime());
        boardDto.setBoardJoinMember(board.getBoardJoinMember());
        if (board.getAttachFile()==0) {
            boardDto.setAttachFile(board.getAttachFile());//파일이 없을경우 0
        }else {
            boardDto.setAttachFile(board.getAttachFile()); //파일이 있을경우 1
            //파일이 있을경우
            //원래 파일이름 가져온다 -> DB에서 파일의 원래이름 가져오는방법
            boardDto.setOldFileName(board.getFileEntities().get(0).getOldFileName());
            //새파일이름(DB저장이름) 가져온다 -> DB에서 파일의 새파일이름 가져오는 방법
            boardDto.setNewFileName(board.getFileEntities().get(0).getNewFileName());
        }

        return boardDto;
    }
}
