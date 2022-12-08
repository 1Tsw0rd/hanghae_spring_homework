package com.sparta.hanghaememo.controller;

import com.sparta.hanghaememo.dto.MemberRequestDto;
import com.sparta.hanghaememo.dto.MemoRequestDto;
import com.sparta.hanghaememo.dto.ResponseDto;
import com.sparta.hanghaememo.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 1. 회원가입
    @PostMapping("/signup")
    public ResponseDto signupMember(@RequestBody MemberRequestDto memberReqDto){
        return memberService.signupMember(memberReqDto);
    }

    // 2. 로그인
    @ResponseBody
    @PostMapping("/login")
    public ResponseDto loginMember(@RequestBody MemberRequestDto memberReqDto, HttpServletResponse response){
        //HttpServletResponse는 토큰 만들고, 응답 헤더에 붙이기 위해 미리 만들어 놓음
        return memberService.loginMember(memberReqDto, response);
    }

}
