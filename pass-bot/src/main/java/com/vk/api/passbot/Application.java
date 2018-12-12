package com.vk.api.passbot;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.eclipse.jetty.server.Server;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.httpclient.HttpTransportClient;

public class Application {

    private final static String PROPERTIES_FILE = "config.properties";

    public static void main(String[] args) throws Exception {
        Properties properties = readProperties();

        HttpTransportClient client = new HttpTransportClient();
        VkApiClient apiClient = new VkApiClient(client);

        GroupActor actor = initVkApi(apiClient, readProperties());
        BotRequestHandler botHandler = new BotRequestHandler(apiClient, actor);

        Server server = new Server(8080);
        server.setAttribute("contextPath", "bot");

        server.setHandler(new RequestHandler(botHandler, properties.getProperty("confirmationCode")));

        server.start();
        server.join();
    }

    private static GroupActor initVkApi(VkApiClient apiClient, Properties properties) {
        int groupId = Integer.parseInt(properties.getProperty("groupId"));
        String token = properties.getProperty("token");
        int serverId = Integer.parseInt(properties.getProperty("serverId"));
        if (groupId == 0 || token == null || serverId == 0) throw new RuntimeException("Params are not set");

        return new GroupActor(groupId, token);
    }

    private static Properties readProperties() throws FileNotFoundException {
        InputStream inputStream = Application.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE);
        if (inputStream == null)
            throw new FileNotFoundException("property file '" + PROPERTIES_FILE + "' not found in the classpath");

        try {
            Properties properties = new Properties();
            properties.load(inputStream);
            return properties;
        } catch (IOException e) {
            throw new RuntimeException("Incorrect properties file");
        }
    }
}
