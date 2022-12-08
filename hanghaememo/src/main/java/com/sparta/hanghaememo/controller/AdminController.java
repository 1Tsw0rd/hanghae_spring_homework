package com.sparta.hanghaememo.controller;

import com.sparta.hanghaememo.dto.MemberRequestDto;
import com.sparta.hanghaememo.dto.ResponseDto;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/oyabong")
public class AdminController {
    //일반회원과 관리자계정을 같은 로직에서 검사하는 것은 지양해야될 것 같은데, 일단 인지만 하고 컨트롤러만 남겨봄...
    //처음 개발하게되면 이런 부분 고려해서 설계해야지...

    // 1.관리자 로그인
//    @ResponseBody
//    @PostMapping("/bosslogin")
//    public ResponseDto loginMember(@RequestBody MemberRequestDto memberReqDto, HttpServletRequest request, HttpServletResponse response){
//        //HttpServletResponse는 토큰 만들고, 응답 헤더에 붙이기 위해 미리 만들어 놓음
//        return adminService.loginAdmin(memberReqDto, response);
//    }
}
