package com.sparta.hanghaememo.auth;

import com.sparta.hanghaememo.dto.MemoRequestDto;
import com.sparta.hanghaememo.entity.Member;
import com.sparta.hanghaememo.entity.MemberRoleEnum;
import com.sparta.hanghaememo.entity.Memo;
import com.sparta.hanghaememo.jwt.JwtUtil;
import com.sparta.hanghaememo.repository.MemberRepository;
import com.sparta.hanghaememo.repository.MemoRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class ValueChecker {  //내 생의 첫 Bean 등록 모듈, 의존성 주입 스스로 사용 2022-12-06 09:49... ㅠ_ㅠ
    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;
    private final MemoRepository memoRepository;


    /*
    회원가입 ID & PWD 체크
    - ID 생성 조건 : 길이 4자이상 10자이하 알파벳 소문자, 숫자
    - PWD 생성 조건 : 길이 8자이상 15자이하, 알파벳 대소문자, 숫자
    */
    public void signupCheck(String memberId, String memberPwd) {

        boolean signupCheck = false; //회원가입 ID & PWD 체크용 변수

        String pattern1 = "^(?=.*[a-z])(?=.*[0-9])[a-z0-9]{4,10}$"; //한글 [가-힣],
        String pattern2 = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,15}$";
        //조건1 : (?=.*[a-z]) 소문자 체크(무조건 1개이상 포함)
        //조건2 : (?=.*[A-Z]) 대문자 체크(무조건 1개이상 포함)
        //조건3 : (?=.*[0-9]) 숫자 체크(무조건 1개이상 포함)
        //조건4 : (?=.*[!@#$%^&*]) 특수문자 체크(무조건 1개이상 포함)
        //조건5 : [a-zA-Z0-9!@#$%^&*]{8,15} 대소문자+숫자+특수문자 포함해서 길이 8자이상 15자이하

        //1. ID 생성조건 부합 여부 체크
        signupCheck = Pattern.matches(pattern1, memberId);

        if (!signupCheck) {
            throw new IllegalArgumentException(
                    "\nID 생성 조건에 부적합니다." +
                            "\n조건1 : 4자이상 10자이하" +
                            "\n조건2 : 영문 소문자, 숫자 포함"
            );
        }

        //2. PWD 생성조건 부합 여부 체크
        signupCheck = Pattern.matches(pattern2, memberPwd);

        if (!signupCheck) {
            throw new IllegalArgumentException(
                    "\n비밀번호 생성 조건에 부적합니다." +
                            "\n조건1 : 8자이상 15자이하" +
                            "\n조건2 : 영문 대소문자, 숫자 포함"
            );
        }
    }


    public Member tokenCheck(HttpServletRequest request) {
        //1. Request에서 Token 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;
        Member member = new Member();

        //2. 토큰이 있는 경우에만 메모 추가 가능
        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                //2-1. 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
            }

            //2-2. 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
            member = memberRepository.findById(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );
        }
        return member;
    }


    //3. 수정/삭제 메모의 회원 권한 체크
    public Memo MemoAdminRoleCheck(Long mno, MemoRequestDto memoReqDto, HttpServletRequest request, Member member) {

        //1. 대상메모 정보 조회
        Memo mnoMemo = memoRepository.findById(mno).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 없습니다.")
        );

        //2. 계정 권한 조회
        if (member.getRole() == MemberRoleEnum.ADMIN && request.getHeader("WhoAreU").equals("IamAdmin")) {
            //2-1. 관리자인 경우 request 객체 내 필요한 json 키만 사용 - 아래는 일반회원 메모 수정/삭제 작업 시 필요한 키
            if (memoReqDto.getMemberId() != null) throw new IllegalArgumentException("원하는 대로~");
            if (memoReqDto.getMPassword() != null) throw new IllegalArgumentException("다 가질거야~");
        } else if (member.getRole() == MemberRoleEnum.MEMBER) {
            //2-2-1. 일반 회원일 경우 메모 작성자 체크
            String memoJsonId = memoReqDto.getMemberId(); //(외부 memberId 조작 가능) 사용자가 전달한 json 키 memberId
            String memoMnoId = mnoMemo.getMember().getMemberId(); //(외부 mno 조작 위험) DB 안에 메모 작성자
            String memoJwtId = member.getMemberId(); //(외부 토큰 조작 위험) 토큰 복호화 후 select한 DB 안에 회원ID

            boolean isId1 = memoJwtId.equals(memoJsonId);
            boolean isId2 = memoJwtId.equals(memoMnoId);

            if (!(isId1 && isId2)) { //둘다 true일 경우만 true << equals 쓰려다 복잡해보여서 새로운 시도봄해
                throw new IllegalArgumentException("메모 작성자가 틀립니다.");
            }

            //2-2-2. 메모 비밀번호 체크
            if (!mnoMemo.getMPassword().equals(memoReqDto.getMPassword())) { //비밀번호 불일치할 경우
                throw new IllegalArgumentException("메모 비밀번호가 일치하지 않습니다.");
            }
        } else {
            throw new RuntimeException("비정상적인 요청입니다.");
        }

        //성공, 실패에 대한 메시지 처리 구현 필요
        return mnoMemo;
    }
}