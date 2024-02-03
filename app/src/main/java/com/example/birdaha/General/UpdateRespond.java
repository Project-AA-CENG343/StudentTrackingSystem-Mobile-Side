package com.example.birdaha.General;

import com.google.gson.annotations.SerializedName;

public class UpdateRespond {
    @SerializedName("success")
    private String message;

    @SerializedName("error")
    private String error;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

}
