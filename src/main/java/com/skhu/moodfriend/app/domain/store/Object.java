package com.skhu.moodfriend.app.domain.store;

import lombok.Getter;

@Getter
public enum Object {
    ITEM_ONE("Item One", 10),
    ITEM_TWO("Item Two", 20),
    ITEM_THREE("Item Three", 30),
    ITEM_FOUR("Item FOUR", 40),
    ITEM_FIVE("Item FIVE", 50);

    private final String displayName;
    private final int price;

    Object(String displayName, int price) {
        this.displayName = displayName;
        this.price = price;
    }
}
