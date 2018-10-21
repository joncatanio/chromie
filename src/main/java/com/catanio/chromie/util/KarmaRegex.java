package com.catanio.chromie.util;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class KarmaRegex {
    // Regular Expressions
    private static final String INC_RE = "<@(?<uid>[\\p{Alpha}0-9]+)>\\s?\\+\\+";
    private static final String DEC_RE = "<@(?<uid>[\\p{Alpha}0-9]+)>\\s?--";
    private static final String SLACK_ID_RE = "^<@(?<slackId>[\\p{Alpha}0-9]+)|(?<slackUsername>.*)>$";
    private Pattern incPattern;
    private Pattern decPattern;
    private Pattern slackIdPattern;

    public KarmaRegex() {
        this.incPattern = Pattern.compile(INC_RE);
        this.decPattern = Pattern.compile(DEC_RE);
        this.slackIdPattern = Pattern.compile(SLACK_ID_RE);
    }

    public Matcher getIncMatcher(String text) {
        return incPattern.matcher(text.trim());
    }

    public Matcher getDecMatcher(String text) {
        return decPattern.matcher(text.trim());
    }

    public Matcher getSlackIdMatcher(String text) {
        return slackIdPattern.matcher(text.trim());
    }
}
