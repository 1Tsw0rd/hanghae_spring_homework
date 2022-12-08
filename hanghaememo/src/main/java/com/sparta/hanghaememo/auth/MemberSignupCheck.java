package com.sparta.hanghaememo.auth;

import lombok.Builder;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class MemberSignupCheck {  //내 생의 첫 Bean 등록 모듈, 의존성 주입 스스로 사용 2022-12-06 09:49... ㅠ_ㅠ

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

        if (!signupCheck){
            throw new IllegalArgumentException(
                    "\nID 생성 조건에 부적합니다." +
                            "\n조건1 : 4자이상 10자이하" +
                            "\n조건2 : 영문 소문자, 숫자 포함"
            );
        }

        //2. PWD 생성조건 부합 여부 체크
        signupCheck =  Pattern.matches(pattern2, memberPwd);

        if (!signupCheck){
            throw new IllegalArgumentException(
                    "\n비밀번호 생성 조건에 부적합니다." +
                            "\n조건1 : 8자이상 15자이하" +
                            "\n조건2 : 영문 대소문자, 숫자 포함"
            );
        }
    }
}
