package com.sparta.hanghaememo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sparta.hanghaememo.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter //게터
@Setter //세터
@NoArgsConstructor //기본생성자
public class MemoRequestDto {
    @JsonProperty("mNo")
    private Long mNo;
    @JsonProperty("mTitle")
    private String mTitle;

    @JsonProperty("mPassword")
    private String mPassword;
    @JsonProperty("mContents")
    private String mContents;
    private LocalDateTime modifiedAt; //수정날짜

    private Member member; //계정 있는지 확인하고 사용할거라

    private String memberId;

}
