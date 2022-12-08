package com.sparta.hanghaememo.entity;

import com.sparta.hanghaememo.dto.CommentRequestDto;
import com.sparta.hanghaememo.dto.MemoRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Comment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "c_no")
    private Long cNo;

    @Column(name="c_password", nullable = false)
    private String cPassword;

    @Column(name = "c_contents", nullable = false)
    private String cContents;

    @ManyToOne(fetch = FetchType.LAZY) //지연로딩
    @JoinColumn(name = "m_no", nullable = false)
    private Memo memo;

    @ManyToOne(fetch = FetchType.LAZY) //지연로딩
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    //createdAt 생성일자
    //modifiedAt 수정일자

}
