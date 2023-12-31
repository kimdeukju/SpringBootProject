package org.judeukkim.springbootKDJ.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.judeukkim.springbootKDJ.dto.ReplyDto;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data   //@Getter, @Setter, @RequiredArgsConstructor, @ToString, @EqualsAndHashCode을 한꺼번에 설정
@NoArgsConstructor   //파라미터가 없는 기본 생성자를 생성
@AllArgsConstructor  //모든 필드 값을 파라미터로 받는 생성자를 만들어줍니다
@Builder //해당 클래스에 해당하는 엔티티 객체를 만들 때 빌더 패턴을 이용해서 만들 수 있도록 지정해주는 어노테이션이다.
// 이렇게 선언해놓으면 나중에 다른 곳에서 Board.builder(). {여러가지 필드의 초기값 선언 }. build() 형태로 객체를 만들 수 있다.
@Entity   //여기 클래스를 테이블로 생성
@Table(name = "reply_tb")  //테이블 이름
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_no")
    public Long id;

    @Column(nullable = false, length = 200)
    private String replyContent;

    @Column(nullable = false)
    private String replyWriter;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createTime; //작성시간

    @ManyToOne
    @JoinColumn(name = "board_no")
    private Board boardId;

    @ManyToOne
    @JoinColumn(name = "member_no")
    private Member replyJoinMember;

    public static Reply toReplyEntity(ReplyDto replyDto, Board boardEntity) {
        Reply replyEntity = new Reply();

        replyEntity.setReplyContent(replyDto.getReplyContent());
        replyEntity.setReplyWriter(replyDto.getReplyWriter());
        replyEntity.setBoardId(boardEntity); // 게시글 설정 추가
        replyEntity.setReplyJoinMember(replyDto.getReplyJoinMember());

        return replyEntity;
    }

    public static Reply toReplyUpdateEntity(ReplyDto replyDto, Board boardEntity) {
        Reply replyEntity= new Reply();

        replyEntity.setId(replyDto.getId());
        replyEntity.setReplyContent(replyDto.getReplyContent());
        replyEntity.setReplyWriter(replyDto.getReplyWriter());
        replyEntity.setBoardId(boardEntity);
        replyEntity.setReplyJoinMember(replyDto.getReplyJoinMember());

        return replyEntity;
    }
}
