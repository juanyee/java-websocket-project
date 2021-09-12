package com.juanyee.websocketproject.client.endpoint;

import org.glassfish.tyrus.client.ClientManager;

import javax.websocket.DeploymentException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class ClientManagerSingleton {
    private static ClientManagerSingleton instance;
    private AppClientEndpoint endpoint;
    private String wsUrl;
    private ClientManager client;

    private ClientManagerSingleton() {
        wsUrl = "ws://localhost:8025/central/service";
        endpoint = new AppClientEndpoint();
        client = new ClientManager();
        client.getProperties().put("org.glassfish.tyrus.incomingBufferSize", 524288000);
    }

    public static ClientManagerSingleton getInstance() {
        if (instance == null) {
            instance = new ClientManagerSingleton();
        }
        return instance;
    }

    public boolean isWebSocketOpen() {
        return endpoint.isSessionOpen();
    }

    public void connectToWebSocket() throws URISyntaxException, DeploymentException, IOException {
        System.out.println("SINGLETON>\t\tConnecting...");
        try {
            client.connectToServer(endpoint, new URI(wsUrl));
        } catch (Exception e) {
                e.printStackTrace();
        }
    }

    public void sendMessage(String message) throws IOException {
        Request req = new Request();
        req.setCommand(Request.TYPE.CLIENT_TO_SERVER__SEND_MESSAGE_TO_SERVER);
        req.setMessage(message);
        endpoint.sendRequestByTextMessage(req);
    }
}
