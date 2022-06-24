package com.app.articlePage.payload.response;




public class MessageResponse {
    private String Message;

    public String getMessage() {
        return Message;
    }

    public MessageResponse(String message) {
        Message = message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    @Override
    public String toString() {
        return "MessageResponse{" +
                "Message='" + Message + '\'' +
                '}';
    }
}
