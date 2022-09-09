package com.readonlydev.common.utils;

import java.awt.Color;

public enum ResultLevel {

    SUCCESS(Color.GREEN),
    WARNING(Color.YELLOW),
    ERROR(Color.RED);

    private Color color;

    ResultLevel(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public int getColorInt() {
        String hex = ColorUtil.getHexValue(getColor());
        return ColorUtil.parseColor(hex);
    }
}
