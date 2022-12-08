package com.sparta.hanghaememo.dto;

import com.sparta.hanghaememo.entity.MemberRoleEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class MemberRequestDto {

    private String memberId;
    private String memberPassword;
    //private MemberRoleEnum role;


    public MemberRequestDto(String memberId, String memberPassword) {
        this.memberId = memberId;
        this.memberPassword = memberPassword;
    }
}
