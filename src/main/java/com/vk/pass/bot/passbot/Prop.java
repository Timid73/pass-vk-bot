package com.vk.pass.bot.passbot;

import org.springframework.stereotype.Component;

@Component
public class Prop {

    private int groupId = 175153907;

    private String token = "1b85ead07fe419173870a99d511f3a62d64235b9b4e09a0612fcce5a0d07aea89208d2e733ac2abf13804";

    private int serverId = 1;

    private String confirmationCode = "b837a80b";

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public String getConfirmationCode() {
        return confirmationCode;
    }

    public void setConfirmationCode(String confirmationCode) {
        this.confirmationCode = confirmationCode;
    }
}
