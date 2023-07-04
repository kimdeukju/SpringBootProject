package org.judeukkim.springbootKDJ.service;

import lombok.RequiredArgsConstructor;
import org.judeukkim.springbootKDJ.dto.MemberDto;
import org.judeukkim.springbootKDJ.entity.Member;
import org.judeukkim.springbootKDJ.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.*;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;




    @Transactional
    public void insertMember(MemberDto memberDto) {
        Member memberEntity=Member.memberEntity(memberDto,passwordEncoder);
        memberRepository.save(memberEntity);
    }
    @Transactional              //회원가입 이메일 중복체크
    public int emailcheck(String email) {
            Optional<Member> memberEntity =memberRepository.findByEmail(email);
            if(memberEntity.isPresent()){
                //이메일이 있으면(중복)
                return 0;
            }else {
                //이메일이 없으면(중복x)
                return 1;
            }
    }
    public MemberDto NameSearch(String userName) {
        Optional<Member> memberEntity=memberRepository.findByUserName(userName);
        if (!memberEntity.isPresent()){
            return null;
        }
        MemberDto memberDto=MemberDto.idSearch(memberEntity.get());
        return memberDto;
    }



    @Value("${spring.mail.username}")  //yml
    private String sendFrom;
    private final JavaMailSender javaMailSender;
    @Transactional
    public void sendSmtp(MemberDto memberDto) {
        char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
        String smtpPw = "";
        // 문자 배열 길이의 값을 랜덤으로 10개를 뽑아 구문을 작성함
        int idx = 0;
        for (int i = 0; i < 10; i++) {
            idx = (int) (charSet.length * Math.random());
            smtpPw += charSet[idx];
        }
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(memberDto.getEmail());
            message.setFrom(sendFrom);
            message.setSubject("KDJ 임시 비밀번호 안내 이메일입니다.");
            message.setText("안녕하세요.\n"
                    + "임시비밀번호 안내 관련 이메일 입니다.\n"
                    + "임시 비밀번호를 발급하오니 사이트에 접속하셔서 로그인 하신 후\n"
                    + "반드시 비밀번호를 변경해주시기 바랍니다.\n\n"
                    + "임시 비밀번호 : " + smtpPw);
            javaMailSender.send(message);
        } catch (MailParseException e) {
            e.printStackTrace();
        } catch (MailAuthenticationException e) {
            e.printStackTrace();
        } catch (MailSendException e) {
            e.printStackTrace();
        } catch (MailException e) {
            e.printStackTrace();
        }
        Member user = memberRepository.findByUserName(memberDto.getUserName()).orElseThrow(() -> {
            return new IllegalArgumentException("임시 비밀번호 변경 실패: 사용자 이름을 찾을 수 없습니다.");
        });
        user.setPassword(passwordEncoder.encode(smtpPw));
    }

    public MemberDto memberMypage(String email) {
        Optional<Member> memberEntity=memberRepository.findByEmail(email);
        if(!memberEntity.isPresent()){
            return null;
        }
        MemberDto memberDto=MemberDto.updateMemberDto(memberEntity.get());
        return memberDto;
    }

    @Transactional
    public int deleteOk(String email) {
        Member memberEntity= memberRepository.findByEmail(email).get();
        memberRepository.delete(memberEntity);
        if (memberRepository.findByEmail(email)!=null){
            return 0;
        }
        return 1;
    }
}
