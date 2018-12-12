package com.vk.pass.bot.passbot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.httpclient.HttpTransportClient;

@Component
public class Server {

    @Autowired
    private Prop prop;

    public void start() throws Exception {
        HttpTransportClient client = new HttpTransportClient();
        VkApiClient apiClient = new VkApiClient(client);

        GroupActor actor = initVkApi(apiClient);
        BotRequestHandler botHandler = new BotRequestHandler(apiClient, actor);

        org.eclipse.jetty.server.Server server = new org.eclipse.jetty.server.Server(8080);

        server.setHandler(new RequestHandler(botHandler, prop.getConfirmationCode()));

        server.start();
        server.join();
    }

    private GroupActor initVkApi(VkApiClient apiClient) {
        if (prop.getGroupId() == 0 || prop.getToken() == null || prop.getServerId() == 0) throw new RuntimeException("Params are not set");

        return new GroupActor(prop.getGroupId(), prop.getToken());
    }
}
