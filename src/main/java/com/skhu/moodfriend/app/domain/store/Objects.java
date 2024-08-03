package com.skhu.moodfriend.app.domain.store;

import lombok.Getter;

@Getter
public enum Objects {
    ITEM_ONE("Item One", 1),
    ITEM_TWO("Item Two", 1),
    ITEM_THREE("Item Three", 1),
    ITEM_FOUR("Item FOUR", 1),
    ITEM_FIVE("Item FIVE", 1);

    private final String displayName;
    private final int price;

    Objects(String displayName, int price) {
        this.displayName = displayName;
        this.price = price;
    }
}
