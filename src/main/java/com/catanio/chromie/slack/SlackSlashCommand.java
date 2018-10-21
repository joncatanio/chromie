package com.catanio.chromie.slack;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.ramswaroop.jbot.core.slack.models.Attachment;
import me.ramswaroop.jbot.core.slack.models.RichMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Profile("slack")
public class SlackSlashCommand {
    private static final Logger logger = LoggerFactory.getLogger(SlackSlashCommand.class);

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
                                        @RequestParam("user_id") String user_id,
                                        @RequestParam("user_name") String userName,
                                        @RequestParam("command") String command,
                                        @RequestParam("text") String text,
                                        @RequestParam("response_url") String responseUrl) {
        // Validate Token
        if (!token.equals(this.slackToken)) {
            logger.info("onKarmaBreakdown -- invalid Slack token provided.");
            return null;
        }
        logger.info("User " + userName + " (" + user_id + ") has requested a karma breakdown.");

        RichMessage richMessage = new RichMessage("Karma Breakdown");
        richMessage.setResponseType("in_channel");
        Attachment[] attachments = new Attachment[1];
        attachments[0] = new Attachment();
        attachments[0].setText("test attachment");
        richMessage.setAttachments(attachments);

        if (logger.isDebugEnabled()) {
            try {
                logger.debug("Reply (RichMessage): {}", new ObjectMapper().writeValueAsString(richMessage));
            } catch (JsonProcessingException e) {
                logger.debug("Error parsing RichMessage: ", e);
            }
        }

        return richMessage.encodedMessage();
    }
}
