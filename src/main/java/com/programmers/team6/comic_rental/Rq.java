package com.programmers.team6.comic_rental;

public class Rq {
    private String actionPath;
    private long rentalId;
    private long comicId;
    private long memberId;

    public Rq(String command) {
        String[] bits = command.split(" ");
    /*
        스페이스 단위로 배열에 넣음(예시 명령어 기반)
        ex) rent 3 1 시
        bits[0] = rent
        bits[1] = 3
        bits[2] = 1
     */
        this.actionPath = bits[0].toLowerCase();

        if((actionPath.equals("comic-detail") ||
                actionPath.equals("comic-update") ||
                actionPath.equals("comic-delete"))
                && bits.length >= 2) {
            try {
                comicId = Long.parseLong(bits[1]);        // comic_id 값 가져옴
            } catch (NumberFormatException e) {
                System.out.println("comicId는 숫자를 입력해야 합니다.");
            }
        }

        if(actionPath.equals("rent") && bits.length >= 3) {
            try {
                comicId = Long.parseLong(bits[1]);
                memberId = Long.parseLong(bits[2]);
            } catch (NumberFormatException e) {
                System.out.println("comicId와 memberId는 숫자를 입력해야 합니다.");
            }
        }

        if(actionPath.equals("return") && bits.length >= 2) {
            try {
                rentalId = Long.parseLong(bits[1]);      // rentalId 값 가져옴
            } catch (NumberFormatException e) {
                System.out.println("rentalId는 숫자를 입력해야 합니다.");
            }
        }
    }

    public String getActionPath() { return actionPath; }
    public long getComicId() { return comicId; }
    public long getMemberId() { return memberId; }
    public long getRentalId() { return rentalId; }

}

