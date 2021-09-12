package com.juanyee.websocketproject.server.endpoint;

import com.google.gson.Gson;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/service")
public class AppServerEndpoint {
    private static Map<String, Session> sessionContext = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, EndpointConfig conf) {
        System.out.println(String.format("[onOpen][%s]", session.getId()));
        add(session);
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        if (session != null) {
            try {
                processRequest(session, message);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            System.out.println("session null");
        }
    }

    @OnMessage
    public void onMessage(Session session, ByteBuffer byteBuffer) {
        System.out.println(String.format("[onMessage_ByteBuffer][%s]", session.getId()));
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        System.out.println(String.format("[onClose][%s]: %s", session.getId(), closeReason));
        delete(session);
    }

    @OnError
    public void onError(Session session, Throwable thr) throws IOException {
        System.out.println(
                String.format("[onError][%s]: %s\n%s", session.getId(), thr.getMessage(), thr.getStackTrace()));
    }

    private void processRequest(Session session, String jsonString) {
        // Descompresion del json
        if (jsonString != null && !jsonString.isEmpty()) {
            try {
                Gson gson = new Gson();
                Request req = gson.fromJson(jsonString, Request.class);
                Request resp = new Request();
                if (req.getCommand() != null) {
                    switch (req.getCommand()) {
                        case CLIENT_TO_SERVER__SEND_MESSAGE_TO_SERVER:
                            // servidor recibe los datos del clier
                            System.out.println(String.format("[onMessage][%s][%s]: Received message: %s", session.getId(),
                                    req.getCommand(), req.getMessage()));

                            resp.setCommand(Request.TYPE.SERVER_TO_CLIENT__SEND_MESSAGE_TO_CLIENT);
                            resp.setMessage("Yeah, I'm here");
                            sendRequestByTextMessage(session, resp);
                            break;
                        default:
                            System.out.println("Default");
                            break;
                    }
                } else {
                    System.out.println("Command not found");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void sendRequestByTextMessage(Session session, Request req) {
        Gson gson = new Gson();
        session.getAsyncRemote().sendText(gson.toJson(req));
    }

    private void sendRequestByBinaryMessage(Session session, Request req) {
        Gson gson = new Gson();
        session.getAsyncRemote().sendBinary(ByteBuffer.wrap(gson.toJson(req).getBytes()));
    }

    public void sendRequestToClient(String sessionId, Request req) throws IOException {
        System.out.println("ENDPOINT/sendRequestToClient>\tsendRequestToClient!!");

        Session session = get(sessionId);
        if (session != null) {
            sendRequestByTextMessage(session, req);
        } else {
            System.out.println("ENDPOINT/sendRequestToClient>\tsession null");
        }
    }

    public void sendRequestToClientByBinary(String sessionId, Request req) throws IOException {
        System.out.println("ENDPOINT/sendRequestToClient>\tsendRequestToClient!!");
        Session session = get(sessionId);
        if (session != null) {
            sendRequestByBinaryMessage(session, req);
        } else {
            System.out.println("ENDPOINT/sendRequestToClient>\tsession null");
        }
    }

    private void add(Session session) {
//      System.out.println("SESSIONCONTEXT: ADD");
        if (!sessionContext.containsKey(session.getId())) {
            sessionContext.put(session.getId(), session);
        }
//      printSessions();
    }

    private Session get(String sessionId) {
//      System.out.println("SESSIONCONTEXT: GET");
//      printSessions();
        if (sessionContext.containsKey(sessionId)) {
            return sessionContext.get(sessionId);
        }
        return null;
    }

    private void delete(Session session) {
//      System.out.println("SESSIONCONTEXT: CLOSE");
        if (sessionContext.containsKey(session.getId())) {
            sessionContext.remove(session.getId());
        }
//      printSessions();
    }

    private void printSessions() {
        System.out.println("-------");
        for (Map.Entry<String, Session> entry : sessionContext.entrySet()) {
            System.out.println("- key: " + entry.getKey());
        }
        System.out.println("-------");
    }

    private String readAllBytesTemporal(String filePath) {
        String content = "";
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(filePath));
            System.out.println("BYTES-LENGTH: " + bytes.length);
            content = new String(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }
}
