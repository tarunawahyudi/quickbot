package com.quickbot.app.component.model.enums;

import lombok.AllArgsConstructor;

import java.util.Random;

@AllArgsConstructor
public enum Gender {
    MALE("Pria"),
    FEMALE("Wanita");

    public final String label;

    private static final Gender[] VALUES = values();
    private static final Random RANDOM = new Random();

    public static String getRandomGender() {
        return VALUES[RANDOM.nextInt(VALUES.length)].label;
    }
}
