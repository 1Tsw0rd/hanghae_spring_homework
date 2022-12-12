package com.sparta.hanghaememo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.hanghaememo.dto.MemberRequestDto;
import com.sparta.hanghaememo.dto.MemoRequestDto;
import com.sparta.hanghaememo.entity.Member;
import com.sparta.hanghaememo.entity.MemberRoleEnum;
import com.sparta.hanghaememo.entity.Memo;
import com.sparta.hanghaememo.repository.MemberRepository;
import com.sparta.hanghaememo.repository.MemoRepository;
import lombok.Builder;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.util.ResourceUtils;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@EnableJpaAuditing
@SpringBootApplication
public class HanghaememoApplication {

    public static void main(String[] args) {
        SpringApplication.run(HanghaememoApplication.class, args);
    }

    @Resource
    private MemberRepository memberRepository;

    @Resource
    private MemoRepository memoRepository;

    @Bean
    public ApplicationRunner applicationRunnerMember(){
        return args -> {
            //노트북 전용(file:)
            //File json = ResourceUtils.getFile("classpath:json/MemberData.json");

            //jar패키지 전용(jar:)인데 노트북에서도 잘되네
            InputStream json = this.getClass().getClassLoader().getResourceAsStream("json/MemberData.json");
            //BufferedReader json = new BufferedReader(new InputStreamReader(inputStream));

            List<MemberRequestDto> memberReqDtoList = new ObjectMapper().readValue(json, new TypeReference<>() {});
            List<Member> memberList = memberReqDtoList.stream().map(Member::new).collect(Collectors.toCollection(ArrayList::new));
            memberRepository.saveAll(memberList);

            //관리자 계정 : admin/admin
            MemberRequestDto memberReqDto = new MemberRequestDto("admin","admin");
            Member admin = new Member(memberReqDto, MemberRoleEnum.ADMIN);
            memberRepository.save(admin);
        };
    }

    @Bean
    public ApplicationRunner applicationRunnerMemo(){
        return args -> {
            //노트북 전용(file:)
            //File json = ResourceUtils.getFile("classpath:json/MemoData.json");

            //jar패키지 전용(jar:) 노트북에서도 잘되네
            //InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("json/MemoData.json");
            //BufferedReader json = new BufferedReader(new InputStreamReader(inputStream));

            InputStream json = this.getClass().getClassLoader().getResourceAsStream("json/MemoData.json");

            List<MemoRequestDto> memoDtolist = new ObjectMapper().readValue(json, new TypeReference<>() {});
            List<Memo> memoList = memoDtolist.stream().map(memoReqDto -> new Memo(memoReqDto)).collect(Collectors.toCollection(ArrayList::new));
            memoRepository.saveAll(memoList);
        };
    }



}
