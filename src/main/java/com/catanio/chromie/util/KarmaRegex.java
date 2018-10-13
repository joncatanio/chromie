package com.catanio.chromie.util;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class KarmaRegex {
    // Regular Expressions
    private static final String INC_RE = "<@(?<uid>[\\p{Alpha}0-9]+)>\\s?\\+\\+";
    private static final String DEC_RE = "<@(?<uid>[\\p{Alpha}0-9]+)>\\s?--";
    private Pattern incPattern;
    private Pattern decPattern;

    public KarmaRegex() {
        this.incPattern = Pattern.compile(INC_RE);
        this.decPattern = Pattern.compile(DEC_RE);
    }

    public Matcher getIncMatcher(String text) {
        return incPattern.matcher(text);
    }

    public Matcher getDecMatcher(String text) {
        return decPattern.matcher(text);
    }
}
