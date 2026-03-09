package com.programmers.team6.comic_rental.controller;

import com.programmers.team6.comic_rental.entity.Member;
import com.programmers.team6.comic_rental.service.MemberService;

import java.util.List;
import java.util.Scanner;

public class MemberController {
    private final Scanner sc;
    private final MemberService memberService = new MemberService();

    public MemberController(Scanner sc) {
        this.sc = sc;
    }

    public void addMember() {
        System.out.print("이름: ");
        String name = sc.nextLine().trim();
        System.out.print("전화번호: ");
        String phone = sc.nextLine().trim();

        long result = memberService.add(name, phone);

        if (result > 0) {
            System.out.printf("=> 회원이 등록되었습니다. (id=%d)\n", result);
        } else if (result == -2) {
            System.out.println("=> 실패: 이름은 2글자 이상이어야 합니다.");
        } else if (result == -3) {
            System.out.println("=> 실패: 이미 존재하는 전화번호입니다.");
        } else {
            System.out.println("=> 시스템 오류로 등록에 실패했습니다.");
        }
    }

    public void findAllMembers() {
        List<Member> memberList = memberService.findAll();

        if(memberList.isEmpty()){
            System.out.println("등록된 회원이 없습니다.");
            return;
        }

        System.out.println(" 번호  |   이름   |    전화 번호    |        등록일");
        System.out.println("=======================================================");
        for( Member member : memberList) {
            System.out.printf("%-4d | %-6s | %-12s | %s \n"
                    , member.getId()
                    , member.getName()
                    , member.getPhone()
                    , member.getCreateDate());
        }
    }
}