package com.stubulika.resource;

public class Message {

    private String message;
    private MessageType type;

    public Message() {
        super();
    }

    public Message(MessageType type, String message) {
        super();
        this.message = message;
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

}
