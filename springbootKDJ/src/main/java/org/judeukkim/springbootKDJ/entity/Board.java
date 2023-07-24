package org.judeukkim.springbootKDJ.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.judeukkim.springbootKDJ.dto.BoardDto;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data   //@Getter, @Setter, @RequiredArgsConstructor, @ToString, @EqualsAndHashCode을 한꺼번에 설정
@NoArgsConstructor   //파라미터가 없는 기본 생성자를 생성
@AllArgsConstructor  //모든 필드 값을 파라미터로 받는 생성자를 만들어줍니다
@Builder //해당 클래스에 해당하는 엔티티 객체를 만들 때 빌더 패턴을 이용해서 만들 수 있도록 지정해주는 어노테이션이다.
// 이렇게 선언해놓으면 나중에 다른 곳에서 Board.builder(). {여러가지 필드의 초기값 선언 }. build() 형태로 객체를 만들 수 있다.
@Entity   //여기 클래스를 테이블로 생성
@Table(name = "board_tb")  //테이블 이름
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_no")
    public Long id;

    @Column(nullable = false)
    private String title;   //제목

    @Column(nullable = false, length = 10000)
    private String content;  //내용

    @Column(nullable = false)
    private String writer;  //작성자

    @Column(columnDefinition = "integer default 0" , nullable = false)
    private int hit; //조회수

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createTime; //작성시간

    @ManyToOne  //(fetch = FetchType.LAZY) 기본
    @JoinColumn(name = "member_no")
    private Member boardJoinMember;  //Member 테이블과 연결, 작성한 사용자 불러오기

    //   게시글 조회 시  덧글목록도 보이게
    // mappedBy = "board"  // 연관관계의 주인(외래키설정 테이블)
    // 1: 다 -> 1의 Entity에 작성
    @OneToMany(mappedBy = "boardId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reply> reply = new ArrayList<>();

    //게시글 1:N 파일
    @OneToMany(mappedBy = "boardId"
            // 게시글이 삭제되면 파일목로도 삭제
            ,cascade = CascadeType.REMOVE,orphanRemoval = true)
    private List<FileEntity> fileEntities=new ArrayList<>();

    @Column(nullable = false)
    private int attachFile;// 파일 유무(1,0)



    public static Board dtoToEntity(BoardDto boardDto){
        Board boardEntity = new Board();

        boardEntity.setId((boardDto.getId()));
        boardEntity.setTitle(boardDto.getTitle());
        boardEntity.setContent(boardDto.getContent());
        boardEntity.setWriter(boardDto.getWriter());
        boardEntity.setHit(boardDto.getHit());
        boardEntity.setBoardJoinMember(boardDto.getBoardJoinMember());
        boardEntity.setAttachFile(0);  //파일이 없다 0

        return boardEntity;
    }
    public static Board dtoToEntityFile(BoardDto boardDto){
        Board boardEntity = new Board();

        boardEntity.setId((boardDto.getId()));
        boardEntity.setTitle(boardDto.getTitle());
        boardEntity.setContent(boardDto.getContent());
        boardEntity.setWriter(boardDto.getWriter());
        boardEntity.setHit(boardDto.getHit());
        boardEntity.setBoardJoinMember(boardDto.getBoardJoinMember());
        //파일 있다. -> 파일이 있으면 -> FileEntity에서 파일 정보를 가져온다.
        boardEntity.setAttachFile(1);  //파일이 있다 1

        return boardEntity;
    }


}
