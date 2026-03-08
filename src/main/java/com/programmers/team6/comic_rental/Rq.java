package com.programmers.team6.comic_rental;

public class Rq {
    private String actionPath;
    private int comicId;
    private int memberId;
    private int rentalId;

    public Rq(String command) {
        String[] bits = command.split(" ");
        /*
            스페이스 단위로 배열에 넣음(예시 명령어 기반)
            ex)rent 3 1 시
            bits[0] = rent;
            bits[1] = 3;
            bits[2] = 1;
         */
        this.actionPath = bits[0];

        if((actionPath.equals("comic-detail") ||
                /*  ex)comic-detail 1 / comic-update 7 / comic-delete 3
                    comic-detail [id] 명령어로 특정 만화책 상세 정보 확인
                    comic-update [id] 명령어로 제목/권수/작가 수정
                    comic-delete [id] 명령어로 해당 만화책 삭제
                 */
                        actionPath.equals("comic-update") ||
                        actionPath.equals("comic-delete"))
                        && bits.length >= 2) {
            comicId = Integer.parseInt(bits[1]);        //comic_id 값 가져옴
        }

        if(actionPath.equals("rent") && bits.length >= 3) {
            /* ex) rent 3 1
               rent [comicId] [memberId] 명령어로 대여 처리(대여중이면 불가)
             */
            comicId = Integer.parseInt(bits[1]);       //comic_id 값 가져옴
            memberId = Integer.parseInt(bits[2]);      //member_id 값 가져옴
        }

        if(actionPath.equals("return") && bits.length >= 2) {
            /*  ex) return 1
                return [rentalId] 명령어로 반납 처리(이미 반납이면 불가)
            */
            rentalId = Integer.parseInt(bits[1]);      //rentalId 값 가져옴

        }
    }
    public String getActionPath() { return actionPath; }
    public int getComicId() { return comicId; }
    public int getMemberId() { return memberId; }
    public int getRentalId() { return rentalId; }

}

