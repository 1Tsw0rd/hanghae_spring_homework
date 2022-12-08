package com.sparta.hanghaememo.controller;

import com.sparta.hanghaememo.dto.MemoListResponseDto;
import com.sparta.hanghaememo.dto.MemoRequestDto;
import com.sparta.hanghaememo.dto.MemoResponseDto;
import com.sparta.hanghaememo.dto.ResponseDto;
import com.sparta.hanghaememo.service.MemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemoController {

    private final MemoService memoService;

    @GetMapping("/")
    public ModelAndView home(){
        return new ModelAndView("index");
    }

    //2. 전체 게시글 목록 조회 API
    @GetMapping("/memos")
    public MemoListResponseDto getMemos(){
        return memoService.getMemos();
    }

    //3. 게시글 작성 API
    @PostMapping("/memo")
    public ResponseDto createMemo(@RequestBody MemoRequestDto memoReqDto, HttpServletRequest request){

        return memoService.createMemo(memoReqDto, request);
    }

    //4. 선택한 게시글 조회 API
    @GetMapping("/memo/{mno}")
    public MemoResponseDto getMemo(@PathVariable Long mno){
        return memoService.getMemo(mno);
    }

    //5. 선택한 게시글 수정 API
    @PatchMapping("/memo/{mno}")
    public MemoResponseDto updateMemo(@PathVariable Long mno, @RequestBody MemoRequestDto memoReqDto, HttpServletRequest request){
        return memoService.updateMemo(mno, memoReqDto, request);
    }

    //6. 선택한 게시글 삭제 API
    @DeleteMapping("/memo/{mno}")
    public ResponseEntity<String> deleteMemo(@PathVariable Long mno, @RequestBody MemoRequestDto memoReqDto, HttpServletRequest request) {
        String result = memoService.deleteMemo(mno,  memoReqDto, request);
        String success = "{\"msg\" : \"메모 삭제 완료\"}";
        String failzz = "{\"msg\" : \"메모 삭제 실패\"}";
        return (result.equals("OK")) ?
                ResponseEntity.status(HttpStatus.OK).body(success) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(failzz);
    }
    //성공 : 상태코드 + 데이터
    //실패 : 상태코드 + 에러
}
