package com.juanyee.websocketproject.client.endpoint;


import com.google.gson.Gson;

import javax.websocket.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;

@ClientEndpoint
public class AppClientEndpoint extends Endpoint {

    private Session session;

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        System.out.println("ENDPOINT>\tonOpen!!");
        System.out.println("max size:" + session.getMaxBinaryMessageBufferSize());
        this.session = session;
        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            @OnMessage
            @Override
            public void onMessage(String message) {
                processRequest(message);
            }
        });
        this.session.addMessageHandler(new MessageHandler.Whole<ByteBuffer>() {
            @OnMessage
            @Override
            public void onMessage(ByteBuffer t) {
                System.out.println("buffer");
            }
        });
    }

    @OnClose
    @Override
    public void onClose(Session session, CloseReason closeReason) {
        System.out.println("ENDPOINT>\tonClose!! id: "+session.getId());
        System.out.println("ENDPOINT>\tRazon: " + String.format("Session %s close because of %s", session.getId(), closeReason));
    }

    @OnError
    @Override
    public void onError(Session session, Throwable thr) {
        System.out.println("ENDPOINT>\tonError!!");
        System.out.println("ENDPOINT>\tgetMessage: "+thr.getMessage());
        System.out.println("ENDPOINT>\tgetStackTrace: "+thr.getStackTrace());
    }

    public void sendRequestByTextMessage(Request req) throws IOException {
        System.out.println("ENDPOINT>\tsendMessage!! comando: "+req.getCommand());
        if(session!=null) {
            Gson gson = new Gson();
            session.getAsyncRemote().sendText(gson.toJson(req));
        }
    }

    private void sendRequestByBinaryMessage(Request req) {
        Gson gson = new Gson();
        if(session!=null) {
            session.getAsyncRemote().sendBinary(ByteBuffer.wrap(gson.toJson(req).getBytes()));
        }
    }

    public void sendSonar() throws IOException, URISyntaxException, DeploymentException {
        System.out.println("ENDPOINT>\tsendSonar!!");
        if(session!=null) {
            if(session.isOpen()) {
                try {
                    session.getAsyncRemote().sendText("");
                } catch (Exception e) {
                    if(e.getMessage()!=null) {
                        System.out.println("ENDPOINT>\tCentralClientEndpoint/e.getMessage: "+e.getMessage());
                    } else {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void processRequest(String jsonString) {
        if(jsonString!=null && !jsonString.isEmpty()) {
            Gson gson = new Gson();
            Request req = gson.fromJson(jsonString, Request.class);
            Request resp = new Request();
            try {
                switch(req.getCommand()) {
                    case SERVER_TO_CLIENT__SEND_MESSAGE_TO_CLIENT:
                        System.out.println("ENDPOINT>\tMessage received: "+req.getMessage());
                        break;

                    default:
                        System.out.println("ENDPOINT>\tDefault");
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isSessionOpen() {
        return (session!=null) ? session.isOpen() : false;
    }
}
