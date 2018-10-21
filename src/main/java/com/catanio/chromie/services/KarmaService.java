package com.catanio.chromie.services;

public interface KarmaService {
    Long updateKarma(String donorSlackId, String recipientSlackId, Integer points);
    String getKarmaBreakdown(String slackId);
}
