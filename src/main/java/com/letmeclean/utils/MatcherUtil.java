package com.letmeclean.utils;

import java.util.regex.Pattern;

public class MatcherUtil {

    private static final int MIN = 8;
    private static final int MAX = 20;
    private static final String REGEX =
            "^((?=.*\\d)(?=.*[a-zA-Z])(?=.*[\\W]).{" + MIN + "," + MAX + "})$";

    private static Pattern pattern = Pattern.compile(REGEX);

    public static boolean isMatch(String password) {
        return pattern.matcher(password).find();
    }
}
