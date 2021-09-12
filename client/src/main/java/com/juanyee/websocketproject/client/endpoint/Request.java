package com.juanyee.websocketproject.client.endpoint;

public class Request {
    public enum TYPE {
        CLIENT_TO_SERVER__SEND_MESSAGE_TO_SERVER,
        SERVER_TO_CLIENT__SEND_MESSAGE_TO_CLIENT
    }

    public TYPE command;
    private String message;

    public TYPE getCommand() {
        return command;
    }

    public void setCommand(TYPE command) {
        this.command = command;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
