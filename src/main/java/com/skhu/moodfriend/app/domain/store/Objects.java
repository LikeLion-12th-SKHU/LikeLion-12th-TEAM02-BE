package com.skhu.moodfriend.app.domain.store;

import lombok.Getter;

@Getter
public enum Objects {
    ITEM_ONE("Item One", 1),
    ITEM_TWO("Item Two", 2),
    ITEM_THREE("Item Three", 3),
    ITEM_FOUR("Item Four", 4),
    ITEM_FIVE("Item Five", 5);

    private final String name;
    private final int price;

    Objects(String name, int price) {
        this.name = name;
        this.price = price;
    }
}
