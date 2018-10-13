package com.catanio.chromie.slack;

import com.catanio.chromie.services.KarmaService;
import com.catanio.chromie.util.KarmaRegex;
import com.fasterxml.jackson.core.JsonProcessingException;
import me.ramswaroop.jbot.core.common.Controller;
import me.ramswaroop.jbot.core.common.EventType;
import me.ramswaroop.jbot.core.common.JBot;
import me.ramswaroop.jbot.core.slack.Bot;
import me.ramswaroop.jbot.core.slack.models.Event;
import me.ramswaroop.jbot.core.slack.models.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * A simple Slack Bot. You can create multiple bots by just
 * extending {@link Bot} class like this one. Though it is
 * recommended to create only bot per jbot instance.
 *
 * @author ramswaroop, joncatanio
 * @version 0.1.0, 09/29/2018
 */
@JBot
@Profile("slack")
public class SlackBot extends Bot {
    private static final Logger logger = LoggerFactory.getLogger(SlackBot.class);

    private KarmaService karmaService;

    private KarmaRegex karmaRegex;

    private KarmaReaction karmaReaction;

    @Value("${slackBotToken}")
    private String slackToken;

    @Autowired
    public SlackBot(KarmaService karmaService,
                    KarmaRegex karmaRegex,
                    KarmaReaction karmaReaction) {
        this.karmaService = karmaService;
        this.karmaRegex = karmaRegex;
        this.karmaReaction = karmaReaction;
    }

    @Override
    public String getSlackToken() {
        return slackToken;
    }

    @Override
    public Bot getSlackBot() {
        return this;
    }

    /**
     * Method to send an unencoded reply back to Slack after receiving an {@link Event},
     * the {@link #reply(WebSocketSession, Event, Message)} method encodes the text and
     * escapes the &, <, >, characters which Slack uses as control sequences.
     *
     * @param session websocket session between bot and Slack
     * @param event   received from slack
     * @param text    the text to send to Slack
     */
    private void replyUnencoded(WebSocketSession session, Event event, String text) {
        Message reply = new Message(text);

        try {
            if (StringUtils.isEmpty(reply.getType())) {
                reply.setType(EventType.MESSAGE.name().toLowerCase());
            }
            if (reply.getChannel() == null && event.getChannelId() != null) {
                reply.setChannel(event.getChannelId());
            }

            session.sendMessage(new TextMessage(reply.toJSONString()));
            if (logger.isDebugEnabled()) {
                logger.debug("Reply (Message): {}", reply.toJSONString());

            }
        } catch (IOException e) {
            logger.error("Error sending event: {}. Exception: {}", event.getText(), e.getMessage());
        }
    }

    @Controller(events = EventType.MESSAGE)
    public void onKarma(WebSocketSession session, Event event) {
        consolidateKarma(session, event);
    }

    @Controller(events = EventType.REACTION_ADDED)
    public void onReactionAdded(WebSocketSession session, Event event) {
        String donorSlackId = event.getUserId();
        String recipientSlackId = event.getItemUser();
        Integer points = karmaReaction.getKarmaForReaction(event.getReaction());

        logger.debug("[onReactionAdded] - karma reaction from: " + donorSlackId + " to: "
            + recipientSlackId + " for " + points + " points");
        if (points != 0) {
            StringBuilder sb = new StringBuilder();
            if (!donorSlackId.equals(recipientSlackId)) {
                Long totalKarma = karmaService.updateKarma(donorSlackId, recipientSlackId, points);
                sb.append("<@" + recipientSlackId + "> [" + totalKarma + " points]\n");
            } else {
                sb.append("Nice try, you can't give yourself karma!");
            }

            // Get the channel id from the Item object and update the event.
            event.setChannelId(event.getItem().getChannel());

            replyUnencoded(session, event, sb.toString());
        }
    }

    @Controller(events = EventType.REACTION_REMOVED)
    public void onReactionRemoved(WebSocketSession session, Event event) {
        String donorSlackId = event.getUserId();
        String recipientSlackId = event.getItemUser();
        Integer points = -karmaReaction.getKarmaForReaction(event.getReaction());

        logger.debug("[onReactionRemoved] - karma reaction from: " + donorSlackId + " to: "
            + recipientSlackId + " for " + points + " points");
        if (points != 0 && !donorSlackId.equals(recipientSlackId)) {
            StringBuilder sb = new StringBuilder();
            Long totalKarma = karmaService.updateKarma(donorSlackId, recipientSlackId, points);
            sb.append("<@" + recipientSlackId + "> [" + totalKarma + " points]\n");

            // Get the channel id from the Item object and update the event.
            event.setChannelId(event.getItem().getChannel());

            replyUnencoded(session, event, sb.toString());
        }
    }

    private void consolidateKarma(WebSocketSession session, Event event) {
        // If a message was edited the information will be attached as a message object.
        // Probably need to make this a bit more robust to ensure all fields are there.
        if (event.getText() == null) {
            if (event.getMessage() == null)
                return;
            event.setUserId(event.getMessage().getUser());
            event.setText(event.getMessage().getText());
        }

        Map<String, Integer> karmaMap = new HashMap<>();
        String donorSlackId = event.getUserId();

        Matcher incMatcher = karmaRegex.getIncMatcher(event.getText());
        Matcher decMatcher = karmaRegex.getDecMatcher(event.getText());
        while (incMatcher.find()) {
            String recipientSlackId = incMatcher.group("uid");
            karmaMap.put(recipientSlackId, karmaMap.getOrDefault(recipientSlackId, 0) + 1);
        }

        while (decMatcher.find()) {
            String recipientSlackId = decMatcher.group("uid");
            karmaMap.put(recipientSlackId, karmaMap.getOrDefault(recipientSlackId, 0) - 1);
        }

        if (karmaMap.isEmpty()) {
            return;
        }
        logger.debug("[consolidateKarma] - karma Map: " + karmaMap.toString());

        StringBuilder sb = new StringBuilder();
        karmaMap.forEach((key, value) -> {
            if (!key.equals(donorSlackId)) {
                Long totalKarma = karmaService.updateKarma(donorSlackId, key, value);
                sb.append("<@" + key + "> [" + totalKarma + " points]\n");
            } else {
                sb.append("Nice try, you can't give yourself karma!");
            }
        });

        replyUnencoded(session, event, sb.toString());
    }
}
