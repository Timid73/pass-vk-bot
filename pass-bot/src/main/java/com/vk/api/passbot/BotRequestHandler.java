package com.vk.api.passbot;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Random;

class BotRequestHandler {

    private static final Logger LOG = LoggerFactory.getLogger(BotRequestHandler.class);

    private final static String[] GET = {"дай", "пароль", "от", "напомни", "get", "at"};
    private final static String[] VPS = {"vps", "впс", "сервер", "сервера", "виртуалки", "виртуалка"};
    private final static String[] LK = {"лк", "кабинета", "кабинет", "почта", "почты", "lk", "mail", "email", "office"};

    private final static int BEFORE_HOUR = 11;
    private final static int AFTER_HOUR = 6;

    private final static String WRONG_TIME_VPS = "Извините. Пароль доступен c " + AFTER_HOUR +":00 до " + BEFORE_HOUR + ":00!";
    private final static String WRONG_TIME_LK = "Извините. Пароль доступен только по выходным!";

    private final static String RETRY_REQUEST = "Не понял вас!";

    private final static String SECRET_VPS = "axkavtckpwhpojp";
    private final static String SECRET_LK = "nZO7um3E2W%wA7*tsW%vV6PzFqX50";

    private final VkApiClient apiClient;

    private final GroupActor actor;
    private final Random random = new Random();

    BotRequestHandler(VkApiClient apiClient, GroupActor actor) {
        this.apiClient = apiClient;
        this.actor = actor;
    }

    void handle(int userId, String message) {
        if (isCollect(message, GET)) {
            if (isCollect(message, VPS)) {
                getPasswordVps(userId);
            } else if (isCollect(message, LK)) {
                getPasswordLk(userId);
            } else {
                retryRequest(userId);
            }
        } else {
            retryRequest(userId);
        }
    }

    private void retryRequest(int userId) {
        sendMessage(userId, RETRY_REQUEST);
    }

    private void getPasswordVps(int userId) {
        LocalTime time = LocalTime.now();
        if (time.getHour() < BEFORE_HOUR && time.getHour() >= AFTER_HOUR) {
            sendMessage(userId, SECRET_VPS);
        } else {
            sendMessage(userId, WRONG_TIME_VPS);
        }
    }

    private void getPasswordLk(int userId) {
        LocalDateTime time = LocalDateTime.now();
        if (time.getDayOfWeek().equals(DayOfWeek.SATURDAY) || time.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
            sendMessage(userId, SECRET_LK);
        } else {
            sendMessage(userId, WRONG_TIME_LK);
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

    private boolean isCollect(String message, String[] variables) {
        String[] words = message.split(" ");
        for (String word: Arrays.asList(words)) {
            if (Arrays.asList(variables).contains(word.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}
