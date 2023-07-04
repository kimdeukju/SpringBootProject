package org.judeukkim.springbootKDJ.repository;

import org.judeukkim.springbootKDJ.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {

    Optional<Member> findByEmail(String email);

    Optional<Member> findByUserName(String userName);


    //불리언(boolean)은 논리학에서 참(true)과 거짓(false)을 나타내는 데 사용됩니다
    // 논리 연산(logical operation)이란 주어진 논리식을 판단하여 참(true)과 거짓(false)을 결정하는 연산입니다.
    boolean existsByUserName(String userName);
}
