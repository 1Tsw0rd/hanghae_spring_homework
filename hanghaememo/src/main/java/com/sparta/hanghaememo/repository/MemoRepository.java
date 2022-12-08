package com.sparta.hanghaememo.repository;

import com.sparta.hanghaememo.entity.Memo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MemoRepository extends JpaRepository<Memo, Long> {

    //@Query(value = "SELECT * FROM memo ORDER BY modified_at DESC", nativeQuery = true)
    List<Memo> findAllByOrderByModifiedAtDesc();
}
