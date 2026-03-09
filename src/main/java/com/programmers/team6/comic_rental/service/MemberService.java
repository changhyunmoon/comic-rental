package com.programmers.team6.comic_rental.service;

import com.programmers.team6.comic_rental.entity.Member;
import com.programmers.team6.comic_rental.repository.MemberRepository;

import java.util.List;

public class MemberService {
    private MemberRepository memberRepository = new MemberRepository();

    public long add(String name, String phone) {
        // 1. 이름 길이 검사
        if (name.length() < 2) return -2;

        // 2. 전화번호 중복 검사 (비즈니스 로직의 핵심)
        if (memberRepository.isPhoneExists(phone)) {
            return -3; // 중복된 번호일 경우 -3 반환
        }

        // 3. 모든 통과 시 저장
        return memberRepository.save(name, phone);
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }
}