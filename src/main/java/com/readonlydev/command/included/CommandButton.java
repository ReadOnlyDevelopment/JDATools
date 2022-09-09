package com.readonlydev.command.included;

import java.util.HashMap;
import java.util.Map;

public enum CommandButton {

    LEFT("\u25C0"),
    STOP("\u23F9"),
    RIGHT("\u25B6"),
    LEAVE("\uD83D\uDEAA");

    private final String emote;

    public final static Map<String, CommandButton> CONSTANTS = new HashMap<String, CommandButton>();

    static {
        for (CommandButton c: values()) {
            CONSTANTS.put(c.emote, c);
        }
    }

    CommandButton(String fromFormatted) {
        this.emote = fromFormatted;
    }

    public String get() {
        return emote;
    }

    public static CommandButton get(String value) {
        CommandButton constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }
}