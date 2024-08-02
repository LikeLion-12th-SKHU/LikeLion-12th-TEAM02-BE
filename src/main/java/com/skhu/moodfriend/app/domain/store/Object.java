package com.skhu.moodfriend.app.domain.store;

import lombok.Getter;

@Getter
public enum Object {
    LEFT_WALL("Left Wall", 50),
    FURNITURE("Furniture", 50),
    SOFA("Sofa", 50),
    RIGHT_WALL("Right Wall", 50),
    LEFT_PROP("Left Prop", 50),
    TABLE("Table", 50),
    TOY("Toy", 50),
    RIGHT_PROP("Right Prop", 50),
    WALL_PAPER("Wall Paper", 50),
    FLOOR("Floor", 50),
    LUG("Lug", 50),
    GIFT("Gift", 50);

    private final String displayName;
    private final int price;

    Object(String displayName, int price) {
        this.displayName = displayName;
        this.price = price;
    }
}
