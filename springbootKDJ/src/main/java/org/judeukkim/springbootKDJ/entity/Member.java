package org.judeukkim.springbootKDJ.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.judeukkim.springbootKDJ.dto.MemberDto;
import org.judeukkim.springbootKDJ.role.Role;
import org.springframework.security.crypto.password.PasswordEncoder;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data   //@Getter, @Setter, @RequiredArgsConstructor, @ToString, @EqualsAndHashCode을 한꺼번에 설정
@NoArgsConstructor   //파라미터가 없는 기본 생성자를 생성
@AllArgsConstructor  //모든 필드 값을 파라미터로 받는 생성자를 만들어줍니다
@Builder //해당 클래스에 해당하는 엔티티 객체를 만들 때 빌더 패턴을 이용해서 만들 수 있도록 지정해주는 어노테이션이다.
// 이렇게 선언해놓으면 나중에 다른 곳에서 Board.builder(). {여러가지 필드의 초기값 선언 }. build() 형태로 객체를 만들 수 있다.
@Entity   //여기 클래스를 테이블로 생성
@Table(name = "member")  //테이블 이름
public class Member {

    @Id  //기본키(Primary key)
    @GeneratedValue(strategy = GenerationType.IDENTITY) //프로젝트에 연결된 DB의 넘버링 따라감
    @Column(name = "member_no")      //칼럼명설정
    private Long no;

    @Column(nullable = false,unique = true)   //unique=True 조건은 해당 필드의 값이 겹치지 않게 하는 조건이다.
    private String email;

    @Column(nullable = false)    //column -> null 값을 허용하지 않는다
    private String password;

    //카카오 우편번호 서비스
    private String zip_code;
    private String homeAddress;
    private String DetailAddress;

    @Column(nullable = false)
    private String userName;
    @Column(nullable = false)
    private String phone;

    @Enumerated(EnumType.STRING)
    private Role role;    //enum 클래스 추가

    @CreationTimestamp  //시간자동입력
    private Timestamp createDate;

    @OneToMany(mappedBy = "boardJoinMember",cascade = CascadeType.ALL)
    private List<Board> boardList = new ArrayList<>();

    @OneToMany(mappedBy = "replyJoinMember",cascade = CascadeType.ALL)
    private List<Reply> replyList = new ArrayList<>();

    public static Member memberEntity(MemberDto memberDto,
                                      PasswordEncoder passwordEncoder){
        Member memberEntity=new Member();

        memberEntity.setNo(memberDto.getNo());
        memberEntity.setEmail(memberDto.getEmail());
        memberEntity.setPassword(passwordEncoder.encode(memberDto.getPassword()));
        memberEntity.setZip_code(memberDto.getZip_code());
        memberEntity.setHomeAddress(memberDto.getHomeAddress());
        memberEntity.setDetailAddress(memberDto.getDetailAddress());
        memberEntity.setUserName(memberDto.getUserName());
        memberEntity.setPhone(memberDto.getPhone());
        memberEntity.setRole(Role.MEMBER);
        return memberEntity;
    }
}
