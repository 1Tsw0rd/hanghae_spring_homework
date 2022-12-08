package com.sparta.hanghaememo.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@Getter
public class MemoListResponseDto extends ResponseDto {

    List<MemoResponseDto> memoList = new ArrayList<>();

    public MemoListResponseDto() {
        super("메모 전체 조회 성공", HttpStatus.OK.value()); //성공 시 OK 200반환
    }

    public void addCourse(MemoResponseDto memoReqDto) {
        memoList.add(memoReqDto);
    }
}
