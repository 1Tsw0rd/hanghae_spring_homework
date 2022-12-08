package com.sparta.hanghaememo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResponseDto { //결과 메시지 및 상태코드 전달용 Dto
    private String msg;
    private int statusCode;

    public ResponseDto(ResponseDto resDto) {
        this.msg = resDto.getMsg();
        this.statusCode = resDto.getStatusCode();
    }

    public ResponseDto(String msg, int statusCode) {
        this.msg = msg;
        this.statusCode = statusCode;
    }
}
