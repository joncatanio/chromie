package com.catanio.chromie.slack;

import com.catanio.chromie.services.KarmaService;
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

    // Regular Expressions
    private static final String INC_RE = "<@(?<uid>[\\p{Alpha}0-9]+)>\\s?\\+\\+";
    private static final String DEC_RE = "<@(?<uid>[\\p{Alpha}0-9]+)>\\s?--";

    @Autowired
    KarmaService karmaService;

    @Value("${slackBotToken}")
    private String slackToken;

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

    /**
     * Invoked when the bot receives a direct mention (@botname: message)
     * or a direct message. NOTE: These two event types are added by jbot
     * to make your task easier, Slack doesn't have any direct way to
     * determine these type of events.
     *
     * @param session
     * @param event
     */
    @Controller(events = {EventType.DIRECT_MENTION, EventType.DIRECT_MESSAGE})
    public void onReceiveDM(WebSocketSession session, Event event) {
        reply(session, event, "Hi, I am " + slackService.getCurrentUser().getName());
    }

    @Controller(events = EventType.MESSAGE, pattern = INC_RE)
    public void onIncrement(WebSocketSession session, Event event, Matcher matcher) {
        Map<String, Integer> karmaMap = new HashMap<>();
        String donorSlackId = event.getUserId();

        do {
            String recipientSlackId = matcher.group("uid");

            karmaMap.put(recipientSlackId, karmaMap.getOrDefault(recipientSlackId, 0) + 1);
        } while (matcher.find());

        logger.info("Karma Map: " + karmaMap.toString());

        StringBuilder sb = new StringBuilder();
        karmaMap.forEach((key, value) -> {
            Long totalKarma = karmaService.updateKarma(donorSlackId, key, value);

            sb.append("<@" + key + "> [" + totalKarma + " points]\n");
        });

        replyUnencoded(session, event, sb.toString());
    }

    @Controller(events = EventType.MESSAGE, pattern = DEC_RE)
    public void onDecrement(WebSocketSession session, Event event, Matcher matcher) {
        do {
            logger.info("Group [DECREMENT]: " + matcher.group("uid"));
        } while (matcher.find());
    }
}
