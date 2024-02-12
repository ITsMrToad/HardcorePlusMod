package com.mr_toad.h_plus.core.config;

public enum SuffixType {
    HARDCORE("Hardcore mode"),
    HARD("'HARD' Difficulty"),
    NORMAL_OR_HARDER("'NORMAL' or harder");

    public final String path;

    SuffixType(String path) {
        this.path = path;
    }

    public String getPath() {
        return this.path;
    }

    public static String commentWithSuffix(String comment, SuffixType suffixType) {
        String inOrOn = suffixType == SuffixType.HARDCORE ? "in" : "on";
        return comment + " || Works ONLY " + inOrOn + ": " + suffixType.getPath();
    }


}