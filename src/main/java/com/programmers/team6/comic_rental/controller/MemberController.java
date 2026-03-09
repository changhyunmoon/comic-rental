package com.programmers.team6.comic_rental.controller;

import com.programmers.team6.comic_rental.service.MemberService;
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
}