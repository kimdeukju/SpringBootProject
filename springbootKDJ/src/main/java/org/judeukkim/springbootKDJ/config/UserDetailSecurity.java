package org.judeukkim.springbootKDJ.config;

import lombok.RequiredArgsConstructor;
import org.judeukkim.springbootKDJ.entity.Member;
import org.judeukkim.springbootKDJ.repository.MemberRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailSecurity implements UserDetailsService {

    private final MemberRepository memberRepository;
    @Override                                 //로그인할 email
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<Member> memberEntityEmail= memberRepository.findByEmail(email);
        if (!memberEntityEmail.isPresent()){
            throw new UsernameNotFoundException("사용자가 없습니다.");
        }
        Member memberEntity= memberEntityEmail.get(); //사용자가있으면 get

        //인증된 회원의 인가(권한설정)
        return User.builder()      //Spring Security 에서는 UserDetails의 User라는 구현체를 기본으로 제공
                .username(memberEntity.getEmail())
                .password(memberEntity.getPassword())
                .roles(memberEntity.getRole().toString())
                .build();
    }
}
