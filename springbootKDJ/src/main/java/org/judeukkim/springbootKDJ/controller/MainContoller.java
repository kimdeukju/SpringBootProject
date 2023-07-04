package org.judeukkim.springbootKDJ.controller;


import lombok.RequiredArgsConstructor;
import org.judeukkim.springbootKDJ.dto.MemberDto;
import org.judeukkim.springbootKDJ.dto.SmtpDto;
import org.judeukkim.springbootKDJ.entity.Member;
import org.judeukkim.springbootKDJ.repository.MemberRepository;
import org.judeukkim.springbootKDJ.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Controller
@RequiredArgsConstructor
public class MainContoller {

    private final MemberService memberService;
    private final MemberRepository memberRepository;

    @GetMapping("/")   //기본페이지
    public String index() {
        return "index";
    }

    @GetMapping("login")   //로그인
    public String login() {
        return "login";
    }

    @GetMapping("join")   //회원가입
    public String join(Model model) {
        model.addAttribute("memberDto", new MemberDto());
        return "join";
    }

    @PostMapping("/join")
    public String joinPost(@Valid MemberDto memberDto, BindingResult result) {
        if (result.hasErrors()) {
            return "/join";
        }
        memberService.insertMember(memberDto);
        System.out.println("회원가입 성공!!");
        return "redirect:/";
    }

    @PostMapping("/emailChecked")                        //회원가입 email 중복체크버튼
    public @ResponseBody int Checked(
            @RequestParam String email) {
        int rs = memberService.emailcheck(email);
        return rs;
    }

    @GetMapping("/loginfail")                               //로그인실패 핸들러
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "exception", required = false) String exception,
                        Model model) {
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);
        return "/login";
    }

    @GetMapping("/idSearch")
    public String idSearch() {
        return "idSearch";
    }

    @PostMapping("/idSearch")
    public String userNameSearch(@RequestParam String userName, Model model) {
        MemberDto memberDto = memberService.NameSearch(userName);
        model.addAttribute("userName", memberDto);
        if (memberDto == null) {
            System.out.println("조회실패");
            return "idSearch";
        } else {
            System.out.println("조회성공");
            return "idSearch1";
        }
    }

    @GetMapping("/pwSearch")
    public String smtpPwSearch() {
        return "/pwSearch";
    }

    @PostMapping("/pwSearch")
    public @ResponseBody SmtpDto<?> search(@RequestBody MemberDto memberDto) {
        if (!memberRepository.existsByUserName(memberDto.getUserName()) || //or 연산자 어느 하나가 true 인경우 true 둘중 하나가 거짓이면이라는뜻
                !Pattern.matches("^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$", memberDto.getEmail())) {
            Map<String, String> mapResult = new HashMap<>();
            if (!memberRepository.existsByUserName(memberDto.getUserName())) {
                mapResult.put("userName", "존재하지 않는 이름입니다");
            }
            if (!Pattern.matches("^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$", memberDto.getEmail())) {
                mapResult.put("email", "존재하지 않는 이메일 형식입니다");
            }
            return new SmtpDto<>(HttpStatus.BAD_REQUEST.value(), mapResult);
        }
        memberService.sendSmtp(memberDto);
        return new SmtpDto<Integer>(HttpStatus.OK.value(), 1);
    }

    @GetMapping("/mypage/{email}")
    public String mypage(@PathVariable String email, Model model){
        MemberDto memberDto = memberService.memberMypage(email);
        model.addAttribute("mypage", memberDto);
        return "/mypage";
    }
    @GetMapping("/update/{email}")   //나의 정보를 가져온 후 회원수정페이지 입장
    public String update(@PathVariable String email, Model model){
        MemberDto memberDto = memberService.memberMypage(email);
        model.addAttribute("mypage1", memberDto);
        return "/update";
    }
    @PostMapping("/update")                                  //회원수정 실행
    public String updatePost(@ModelAttribute @Valid MemberDto memberDto, BindingResult result) {
        if (result.hasErrors()) {
            return "/update";
        }
        memberService.insertMember(memberDto);
        return "redirect:/";
    }
    @GetMapping("/delete/{email}")
    public String delete(@PathVariable String email){
        int rs= memberService.deleteOk(email);
        if (rs==1){
            System.out.println("회원탈퇴 실패");
            return null;
        }
        System.out.println("회원탈퇴 성공");
        return "redirect:/logout";
    }
}
