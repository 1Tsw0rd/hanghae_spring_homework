package com.sparta.hanghaememo.repository;

import com.sparta.hanghaememo.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, String> {
}
