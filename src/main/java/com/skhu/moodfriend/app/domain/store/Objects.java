package com.skhu.moodfriend.app.domain.store;

import lombok.Getter;

@Getter
public enum Objects {
    TROPICAL_TREE_WINDOW("열대나무 창문", 50),
    FAN("선풍기", 30),
    WHITE_SOFA("화이트 소파", 100),
    POSTER("포스터", 20),
    CACTUS("선인장", 25),
    TODAY_DESSERT("오늘의 디저트", 15),
    BOOK("책", 40),
    SHOE_RACK("신발장", 35),
    OCEAN_WALLPAPER("오션 벽지", 80),
    BEIGE_FLOORING("베이지 바닥재", 70),
    CHECK_RUG("체크 러그", 60),
    HEART_CUSHION("하트 쿠션", 30);

    private final String name;
    private final int price;

    Objects(String name, int price) {
        this.name = name;
        this.price = price;
    }
}
