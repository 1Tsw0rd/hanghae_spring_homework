package com.sparta.hanghaememo.entity;

import com.sparta.hanghaememo.dto.MemberRequestDto;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Member extends Timestamped{

    @Id
    @Column(name = "member_id")
    private String memberId;

    @Column(name = "member_pwd", nullable = false)
    private String memberPassword;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private MemberRoleEnum role;

    @OneToMany(mappedBy = "member")
    private List<Memo> memos = new ArrayList<>();

//    @OneToMany(mappedBy = "member")
//    private List<Comment> comments = new ArrayList<>();


    public Member(MemberRequestDto memberReqDto, MemberRoleEnum role) {
        this.memberId = memberReqDto.getMemberId();
        this.memberPassword = memberReqDto.getMemberPassword();
        this.role = role;
    }

    public Member(MemberRequestDto memberReqDto) {
        this.memberId = memberReqDto.getMemberId();
        this.memberPassword = memberReqDto.getMemberPassword();
        this.role = MemberRoleEnum.MEMBER;
    }


    //createdAt 생성일자
    //modifiedAt 수정일자

}
