package com.devjy.devel.evermeal.Auth;

/**
 * Created by devel on 2016-07-14.
 */
public enum Gender
{
    Unknown("-1"),
    Male("male"),
    FeMale("female");

    private String text;

    Gender(String text)
    {
        this.text = text;
    }

    public String getText()
    {
        return this.text;
    }

    public static Gender fromString(String text) {
        if (text != null) {
            for (Gender g : Gender.values()) {
                if (text.equalsIgnoreCase(g.text)) {
                    return g;
                }
            }
        }
        return Gender.Unknown;
    }
}
