package com.vk.api.passbot;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;

class BotRequestHandler {

    private static final Logger LOG = LoggerFactory.getLogger(BotRequestHandler.class);

    private final static String GET = "get";
    private final static String SET = "set";

    private final VkApiClient apiClient;

    private final GroupActor actor;
    private final Random random = new Random();

    private Map<Integer, String> passwords = new HashMap<>();

    BotRequestHandler(VkApiClient apiClient, GroupActor actor) {
        this.apiClient = apiClient;
        this.actor = actor;
    }

    void handle(int userId, String message) {
        if (message.toLowerCase().startsWith(GET)) {
            getPassword(userId);
        } else if(message.toLowerCase().startsWith(SET)) {
            setPassword(userId, message.replace(SET, "").trim());
        }
    }

    private void setPassword(int userId, String password) {
        passwords.put(userId, password);
        sendMessage(userId, "New password saved!");
    }

    private void getPassword(int userId) {
        LocalTime time = LocalTime.now();
        if (time.getHour() < 9 || time.getHour() > 22) {
            String password = passwords.get(userId);
            if (password == null) {
                sendMessage(userId, "Password not found!");
            } else {
                sendMessage(userId, password);
            }
        } else {
            sendMessage(userId, "Sorry! Try later!");
        }
    }

    private void sendMessage(int userId, String message) {
        try {
            apiClient.messages().send(actor)
                    .message(message)
                    .userId(userId)
                    .randomId(random.nextInt())
                    .execute();
        } catch (ApiException e) {
            LOG.error("INVALID REQUEST", e);
        } catch (ClientException e) {
            LOG.error("NETWORK ERROR", e);
        }
    }
}
