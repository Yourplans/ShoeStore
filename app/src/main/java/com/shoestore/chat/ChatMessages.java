package com.shoestore.chat;

/**
 * Created by Daniel on 28/04/2017.
 */
public class ChatMessages {

    private  String message ="";
    private  boolean isType = false;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isType() {
        return isType;
    }

    public void setType(boolean type) {
        isType = type;
    }
}
