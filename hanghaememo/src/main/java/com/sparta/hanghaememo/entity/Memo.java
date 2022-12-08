package com.sparta.hanghaememo.entity;

import com.sparta.hanghaememo.dto.MemoRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Memo extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "m_no")
    private Long mNo;

    @Column(name = "m_title", nullable = false)
    private String mTitle;

    @Column(name="m_password", nullable = false)
    private String mPassword;

    @Column(name = "m_contents", nullable = false)
    private String mContents;

    @ManyToOne(fetch = FetchType.LAZY) //지연로딩
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "memo")
    private List<Comment> comments = new ArrayList<>();

    //createdAt 생성일자
    //modifiedAt 수정일자

    //Dto -> entity
    public Memo(MemoRequestDto memoReqDto, Member member) {
        this.mTitle = memoReqDto.getMTitle();
        this.mPassword = memoReqDto.getMPassword();
        this.mContents = memoReqDto.getMContents();
        this.member = member; //찾고 집어넣어야되
    }

    //MemberData.json 전용
    public Memo(MemoRequestDto memoReqDto) {
        this.mTitle = memoReqDto.getMTitle();
        this.mPassword = memoReqDto.getMPassword();
        this.mContents = memoReqDto.getMContents();
        this.member = memoReqDto.getMember(); //찾고 집어넣어야되
    }

//    // memo.passwordMatch(password)
//    public boolean passwordMatch(String password) {
//        if (password.equals(this.mPassword)) {
//            return true;
//        }
//        return false;
//    }

    //JPA 영속성 컨텍스트 이용
    public void update(MemoRequestDto memoReqDto) {
        this.mTitle = memoReqDto.getMTitle();
        this.mContents = memoReqDto.getMContents();
    }
}
