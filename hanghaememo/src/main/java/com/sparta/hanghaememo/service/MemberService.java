package com.sparta.hanghaememo.service;

import com.sparta.hanghaememo.auth.ValueChecker;
import com.sparta.hanghaememo.dto.MemberRequestDto;
import com.sparta.hanghaememo.dto.ResponseDto;
import com.sparta.hanghaememo.entity.Member;
import com.sparta.hanghaememo.entity.MemberRoleEnum;
import com.sparta.hanghaememo.jwt.JwtUtil;
import com.sparta.hanghaememo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@ResponseStatus
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final ValueChecker valueChecker; //(자작)값 검사 전용 모듈

    //관리자 로그인 구현하지 않았으니 추가 개발 시 참고
    @Transactional
    public ResponseDto signupMember(MemberRequestDto memberReqDto) {
        String memberId = memberReqDto.getMemberId();
        String memberPwd = memberReqDto.getMemberPassword();

        //1. ID & 비밀번호 조건 체크
        valueChecker.signupCheck(memberId, memberPwd);

        //1. 회원 중복 확인
        Optional<Member> overlapMember = memberRepository.findById(memberId);
        if (overlapMember.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        //2. 사용자 ROLE 확인 - 관리자계정은 회원가입으로 생성불가
        MemberRoleEnum role = MemberRoleEnum.MEMBER;
        /** 관리자 로그인까지 구현하면 활용...
         *         if (signupRequestDto.isAdmin()) {
         *             if (!signupRequestDto.getAdminToken().equals(ADMIN_TOKEN)) {
         *                 throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
         *             }
         *             role = UserRoleEnum.ADMIN;
         *         }
         * */

        //3. Dto -> Entity
        Member member = new Member(memberReqDto, role);

        //4. 저장(회원 생성)
        memberRepository.save(member);


        //5. 결과 반환
        return new ResponseDto("회원가입 성공", HttpStatus.OK.value());
    }

    @Transactional(readOnly = true)
    public ResponseDto loginMember(MemberRequestDto memberReqDto, HttpServletResponse response) {
        String memberId = memberReqDto.getMemberId();
        String memberPwd = memberReqDto.getMemberPassword();

        //1. 사용자 확인
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
        );

        //2. 비밀번호 확인
        if(!member.getMemberPassword().equals(memberPwd)){
            throw  new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(member.getMemberId(), member.getRole()));

        return new ResponseDto("로그인 성공", HttpStatus.OK.value());
    }
}
