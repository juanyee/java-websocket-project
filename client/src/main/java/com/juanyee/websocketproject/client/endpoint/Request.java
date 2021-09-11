package com.juanyee.websocketproject.client.endpoint;

public class Request {
    public enum TYPE {
        CLIENT_TO_SERVER__SEND_MESSAGE_TO_SERVER,
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
