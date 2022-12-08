package com.sparta.hanghaememo.service;

import com.sparta.hanghaememo.auth.ValueChecker;
import com.sparta.hanghaememo.dto.MemoListResponseDto;
import com.sparta.hanghaememo.dto.MemoRequestDto;
import com.sparta.hanghaememo.dto.MemoResponseDto;
import com.sparta.hanghaememo.dto.ResponseDto;
import com.sparta.hanghaememo.entity.Member;
import com.sparta.hanghaememo.entity.Memo;
import com.sparta.hanghaememo.jwt.JwtUtil;
import com.sparta.hanghaememo.repository.MemberRepository;
import com.sparta.hanghaememo.repository.MemoRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
@Builder
@RequiredArgsConstructor
public class MemoService {

    private final MemoRepository memoRepository;
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final ValueChecker valueChecker; //(자작)값 검사 전용 모듈

    @Transactional(readOnly = true)  //읽기 전용
    public MemoListResponseDto getMemos() {
        //1. 내림차순 모든 메모 조회
        List<Memo> memoList = memoRepository.findAllByOrderByModifiedAtDesc();

        MemoListResponseDto memoListResDto = new MemoListResponseDto();

        //2. Entity List -> Dto List
        if (memoList.size() != 0) {
            for (Memo memo : memoList) {
                memoListResDto.addCourse(new MemoResponseDto(memo));
            }
        }

        //엔티티 List -> Dto List 변환... 이렇게 해야하나
        //List<MemoDto> memoDtoList = memoList.stream().map(Memo::toMemoDto).collect(Collectors.toList());

        //3. 결과 반환
        return memoListResDto;
    }

    @Transactional
    public ResponseDto createMemo(MemoRequestDto memoReqDto, HttpServletRequest request) {
        //1. Request 내 Token 검증 후 사용자 정보 가져오기
        Member member = valueChecker.tokenCheck(request);

        //2. 조작 검증 << 메모 생성 전용 검증 만들자]]]
        String memoId1 = memoReqDto.getMemberId(); //(외부조작 가능)(사용자가 전달)한 json 키 memberId
        String memoId4 = member.getMemberId(); //(비교적 안전)토큰 복호화 후 select한 DB 안에 회원ID

        boolean isId1 = memoId4.equals(memoId1);

        if (!isId1) {
            throw new IllegalArgumentException("사용자 정보가 올바르지 않습니다.");
        }

        //3. Dto -> Entity
        Memo memo = new Memo(memoReqDto, member);

        //4. 저장(메모 생성)
        memoRepository.save(memo);

        //5-1. 결과 반환(성공)
        return new ResponseDto("메모 등록 성공", HttpStatus.OK.value());

        //5-2. 결과 반환(실패) - 객체지향 생활 체조 원칙 - else 쓰지마라 실천....
//            return new ResponseDto("메모 등록 실패", HttpStatus.NO_CONTENT.value());
    }

    @Transactional(readOnly = true)
    public MemoResponseDto getMemo(Long mno) {
        //1. 단일 메모 조회
        Memo memo = memoRepository.findById(mno).orElseThrow(
                () -> new IllegalArgumentException("메모가 존재하지 않습니다.")
        );

        //2. Entity -> Dto
        MemoResponseDto memoResDto = new MemoResponseDto(memo);

        //3. 결과 반환
        return memoResDto;
    }

    @Transactional
    public MemoResponseDto updateMemo(Long mno, MemoRequestDto memoReqDto, HttpServletRequest request) {
        //1. Request 내 Token 검증 후 사용자 정보 가져오기
        Member member = valueChecker.tokenCheck(request);

        //2. 사용자 권한 체크 - 이상없는 경우 처리대상 메모 entity 반환
        Memo memo = valueChecker.MemoAdminRoleCheck(mno, memoReqDto, request, member);

        //3. JPA 영속성 컨텍스트 활용한 update
        memoReqDto.setMNo(mno);
        memo.update(memoReqDto);

        //6-1. 결과반환-성공
        MemoResponseDto memoResDto = new MemoResponseDto(memo);
        return memoResDto;

//        //6-2. 결과반환-실패
//        return null;
    }

    @Transactional
    public String deleteMemo(Long mno, MemoRequestDto memoReqDto, HttpServletRequest request) {
        //필드
        String result = "";

        //1. Request 내 Token 검증 후 사용자 정보 가져오기
        Member member = valueChecker.tokenCheck(request);

        //2. 사용자 권한 체크 - 이상없는 경우 처리대상 메모 entity 반환
        Memo memo = valueChecker.MemoAdminRoleCheck(mno, memoReqDto, request, member);

        memoRepository.deleteById(mno);
        result = "OK";

        //3. 결과 반환
        return result;
    }
}