package org.game.entity;

public enum LandscapeType {

    GREENGRASS(1),
    BLACKGRASS(2),
    ROCK(3),
    WATER(4),
    EXPLOSION(5),
    BOMB(6),
    WALL(7),
    TREE(9);

    private Integer value;

    LandscapeType(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
