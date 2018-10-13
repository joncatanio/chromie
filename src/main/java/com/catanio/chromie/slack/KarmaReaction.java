package com.catanio.chromie.slack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides an interface for determining how much karma to distribute
 * when a reaction is added or removed from a message.
 */
@Component
public class KarmaReaction {

    private static Logger logger = LoggerFactory.getLogger(KarmaReaction.class);

    private Map<String, Integer> reactionMap;

    public KarmaReaction() {
        reactionMap = new HashMap<>();
        reactionMap.put("1up", 1);
        reactionMap.put("+1", 1);
        reactionMap.put("-1", -1);
        reactionMap.put("thumbsup", 1);
        reactionMap.put("thumbsdown", -1);
    }

    /**
     * Provides karma points for given reaction.
     *
     * @param reaction the reaction id for karma points
     * @return         the points for a given reaction
     */
    public Integer getKarmaForReaction(String reaction) {
        String stemmedReaction = reaction.replaceAll("::skin-tone.*$", "").trim();
        Integer points = reactionMap.get(stemmedReaction);

        logger.debug("Karma Reaction: (\"" + stemmedReaction + "\", " + points + ")");
        return points == null ? 0 : points;
    }
}
