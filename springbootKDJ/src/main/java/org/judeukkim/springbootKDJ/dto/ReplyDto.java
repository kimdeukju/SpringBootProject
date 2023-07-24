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
import java.time.LocalDateTime;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReplyDto {


    public Long id;
    private String replyContent;
    private String replyWriter;
    private Long boardId;    // BoardEntity  의  board_id  //게시글 조회시
    private Member replyJoinMember;
    private LocalDateTime createTime;

    public static ReplyDto toReplyDto(Reply replyEntity, Long boardId) {

        ReplyDto replyDto=new ReplyDto();

        replyDto.setId(replyEntity.getId());
        replyDto.setReplyContent(replyEntity.getReplyContent());
        replyDto.setReplyWriter(replyEntity.getReplyWriter());
        replyDto.setCreateTime(replyEntity.getCreateTime());
        replyDto.setBoardId(boardId);
        replyDto.setReplyJoinMember(replyEntity.getReplyJoinMember());

        return replyDto;
    }


}
