package com.sparta.hanghaememo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sparta.hanghaememo.entity.Member;
import com.sparta.hanghaememo.entity.Memo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter //게터
@Setter //세터
@NoArgsConstructor //기본생성자
public class CommentRequestDto {
    @JsonProperty("cNo")
    private Long cNo;
    @JsonProperty("cPassword")
    private String cPassword;
    @JsonProperty("cContents")
    private String cContents;
    private LocalDateTime modifiedAt; //수정날짜

    private Memo memo; //메모 있는지 확인하고 사용할거라

}
