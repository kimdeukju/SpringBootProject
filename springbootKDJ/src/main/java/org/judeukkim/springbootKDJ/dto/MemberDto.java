package org.judeukkim.springbootKDJ.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.judeukkim.springbootKDJ.entity.Member;
import org.judeukkim.springbootKDJ.role.Role;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberDto {

    private Long no;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$", message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotBlank
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    private String password;

    //카카오 우편번호 서비스
    private String zip_code;
    private String homeAddress;
    private String DetailAddress;

    @NotBlank
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣]{3,4}$", message = "정확한 이름을 입력해주세요")
    private String userName;

    @NotBlank
    @Pattern(regexp = "^\\d{3}\\d{3,4}\\d{4}$",message = "정확한 핸드폰번호를입력하세요")
    private String phone;

    private Role role;

    private Timestamp createDate;


    public static MemberDto idSearch(Member memberEntity) {
        MemberDto memberDto= new MemberDto();
        memberDto.setEmail(memberEntity.getEmail());
        memberDto.setUserName(memberEntity.getUserName());
        return memberDto;
    }

    public static MemberDto updateMemberDto(Member member) {
        MemberDto memberDto=new MemberDto();

        memberDto.setNo(member.getNo());
        memberDto.setEmail(member.getEmail());
        memberDto.setPassword(member.getPassword());
        memberDto.setZip_code(member.getZip_code());
        memberDto.setHomeAddress(member.getHomeAddress());
        memberDto.setDetailAddress(member.getDetailAddress());
        memberDto.setUserName(member.getUserName());
        memberDto.setPhone(member.getPhone());
        return memberDto;
    }
}
