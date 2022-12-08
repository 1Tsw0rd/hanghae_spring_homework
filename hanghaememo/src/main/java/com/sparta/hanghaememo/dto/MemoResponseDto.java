package com.sparta.hanghaememo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sparta.hanghaememo.entity.Member;
import com.sparta.hanghaememo.entity.Memo;
import lombok.*;

import java.time.LocalDateTime;

@ToString
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemoResponseDto {
    @JsonProperty("mNo")
    private Long mNo;

    @JsonProperty("mTitle")
    private String mTitle;

    @JsonProperty("mContents")
    private String mContents;
    private LocalDateTime modifiedAt; //수정날짜

    private String memberId; //ID값만 전달할거라

    //Entity -> Dto
    public MemoResponseDto(Memo memo) {
        this.mNo = memo.getMNo();
        this.mTitle = memo.getMTitle();
        this.mContents = memo.getMContents();
        this.modifiedAt = memo.getModifiedAt();
        this.memberId = memo.getMember().getMemberId();
    }
}


//예전 방식 Dto -> Entity
// public Memo toEntity() {
//        return new Memo(mNo, mTitle, mUsername, mPassword, mContents);
//    }