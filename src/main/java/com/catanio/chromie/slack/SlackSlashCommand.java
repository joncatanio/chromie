package com.catanio.chromie.slack;

import com.catanio.chromie.services.KarmaService;
import com.catanio.chromie.util.KarmaRegex;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.ramswaroop.jbot.core.slack.models.RichMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.regex.Matcher;

@RestController
@Profile("slack")
public class SlackSlashCommand {
    private static final Logger logger = LoggerFactory.getLogger(SlackSlashCommand.class);

    @Autowired
    KarmaService karmaService;

    @Autowired
    KarmaRegex karmaRegex;

    @Value("${slackSlashCommandToken")
    private String slackToken;

    @RequestMapping(value = "/karma/breakdown",
                    method = RequestMethod.POST,
                    consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public RichMessage onKarmaBreakdown(@RequestParam("token") String token,
                                        @RequestParam("team_id") String teamId,
                                        @RequestParam("team_domain") String teamDomain,
                                        @RequestParam("channel_id") String channelId,
                                        @RequestParam("channel_name") String channelName,
                                        @RequestParam("user_id") String userId,
                                        @RequestParam("user_name") String userName,
                                        @RequestParam("command") String command,
                                        @RequestParam("text") String text,
                                        @RequestParam("response_url") String responseUrl) {
        RichMessage richMessage = new RichMessage();
        richMessage.setResponseType("in_channel");
        logger.info("User " + userName + " (" + userId + ") has requested a karma breakdown.");

        Matcher slackIdMatcher = karmaRegex.getSlackIdMatcher(text);
        if (slackIdMatcher.find()) {
            richMessage.setText(karmaService.getKarmaBreakdown(slackIdMatcher.group("slackId")));
        } else {
            logger.info("No match found for Slack ID, text: \"" + text + "\"");
            richMessage.setText("I wasn't able to decipher a user from your command, " +
                "perhaps it was incorrectly formatted");
            return richMessage;
        }

        if (logger.isDebugEnabled()) {
            try {
                logger.debug("Reply (RichMessage): {}", new ObjectMapper().writeValueAsString(richMessage));
            } catch (JsonProcessingException e) {
                logger.debug("Error parsing RichMessage: ", e);
            }
        }

        return richMessage;
    }
}
