package org.judeukkim.springbootKDJ.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.judeukkim.springbootKDJ.entity.Board;
import org.judeukkim.springbootKDJ.entity.Member;
import org.judeukkim.springbootKDJ.entity.Reply;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BoardDto {

    public Long no;
    @NotBlank(message = "제목을 입력 해주세요")
    private String title;   //제목
    @NotBlank(message = "내용을 입력 해주세요")
    private String content;  //내용
    private String writer;  //작성자
    private int hit; //조회수
    private LocalDateTime createTime; //작성시간

    public static BoardDto entityToDto(Board board) {
        BoardDto boardDto= new BoardDto();

        boardDto.setNo(board.getNo());
        boardDto.setTitle(board.getTitle());
        boardDto.setContent(board.getContent());
        boardDto.setWriter(board.getWriter());
        boardDto.setHit(board.getHit());
        boardDto.setCreateTime(board.getCreateTime());

        return boardDto;
    }
}
