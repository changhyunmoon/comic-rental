package com.programmers.team6.comic_rental;

public class Rq {
    private String actionPath;
    private long rentalId;
    private long comicId;
    private long memberId;

    public Rq(String command) {

        String[] bits = command.trim().split("\\s+");

        if(bits.length == 0) {
            throw new IllegalArgumentException("명령어가 비어 있습니다.");
        }

        this.actionPath = bits[0].toLowerCase();

        // comic 관련 명령
        if((actionPath.equals("comic-detail") ||
                actionPath.equals("comic-update") ||
                actionPath.equals("comic-delete"))
                && bits.length >= 2) {

            try {
                comicId = Long.parseLong(bits[1]);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("comicId는 숫자를 입력해야 합니다.");
            }
        }

        // 대여
        if(actionPath.equals("rent") && bits.length >= 3) {

            try {
                comicId = Long.parseLong(bits[1]);
                memberId = Long.parseLong(bits[2]);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("comicId와 memberId는 숫자를 입력해야 합니다.");
            }
        }

        // 반납
        if(actionPath.equals("return") && bits.length >= 2) {

            try {
                rentalId = Long.parseLong(bits[1]);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("rentalId는 숫자를 입력해야 합니다.");
            }
        }
    }

    public String getActionPath() { return actionPath; }
    public long getComicId() { return comicId; }
    public long getMemberId() { return memberId; }
    public long getRentalId() { return rentalId; }
}