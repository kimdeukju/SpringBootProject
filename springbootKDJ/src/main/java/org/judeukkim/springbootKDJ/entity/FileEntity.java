package org.judeukkim.springbootKDJ.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data   //@Getter, @Setter, @RequiredArgsConstructor, @ToString, @EqualsAndHashCode을 한꺼번에 설정
@NoArgsConstructor   //파라미터가 없는 기본 생성자를 생성
@AllArgsConstructor  //모든 필드 값을 파라미터로 받는 생성자를 만들어줍니다
@Builder //해당 클래스에 해당하는 엔티티 객체를 만들 때 빌더 패턴을 이용해서 만들 수 있도록 지정해주는 어노테이션이다.
// 이렇게 선언해놓으면 나중에 다른 곳에서 Board.builder(). {여러가지 필드의 초기값 선언 }. build() 형태로 객체를 만들 수 있다.
@Entity   //여기 클래스를 테이블로 생성
@Table(name = "fileEntity")  //테이블 이름
public class FileEntity {

    @Id // 기본키
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increament
    @Column(name = "file_id")
    public Long id;  //파일 번호

    @Column(nullable = false)
    private String oldFileName;// 원래이름

    @Column(nullable = false)
    private String newFileName;// DB 저장이름

    //파일 N :1게시글
    @ManyToOne() //fetch = FetchType.LAZY 기본
    @JoinColumn(name = "board_no") //참조하는 테이블의 기본키또는 기본키 성절
    private Board boardId;


    @CreationTimestamp  // 처음 파일 생성 시 자동으로 시간이 설정
    @Column(updatable = false)   // update -> 적용 X
    private LocalDateTime createTime; // 파일 생성 시간1


    public static FileEntity toFileEntity(Board boardEntity3, String originalFilename, String newFileName) {
        FileEntity fileEntity=new FileEntity();
        fileEntity.setBoardId(boardEntity3);
        fileEntity.setOldFileName(originalFilename);
        fileEntity.setNewFileName(newFileName);
        return fileEntity;
    }
}
